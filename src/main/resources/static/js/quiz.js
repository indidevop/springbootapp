// Get quiz title from URL
const params = new URLSearchParams(window.location.search);
const quizTitle = params.get("title");

document.getElementById("quizTitle").innerText = quizTitle;

let quizId;
let responses = {};
let questionMap = {}; // questionId → question div

// Disable submit button until quiz loads
const submitBtn = document.getElementById("submitBtn");
submitBtn.disabled = true;

// Fetch quiz from backend
fetch(`/quiz/getQuiz/${quizTitle}`)
  .then(res => res.json())
  .then(data => {
    console.log("Quiz data received:", data);
    quizId = data.id;

    // Enable submit button now that quiz is loaded
    submitBtn.disabled = false;

    const form = document.getElementById("quizForm");

    data.questions.forEach(q => {
      const div = document.createElement("div");
      div.classList.add("mb-4", "p-3", "border", "rounded");
      div.id = `question-${q.id}`;

      questionMap[q.id] = div;

      div.innerHTML = `
        <p><strong>${q.questionTitle}</strong></p>
        ${createOption(q.id, q.option1)}
        ${createOption(q.id, q.option2)}
        ${createOption(q.id, q.option3)}
        ${createOption(q.id, q.option4)}
      `;
      form.appendChild(div);
    });
  })
  .catch(err => {
    console.error("Error loading quiz:", err);
    alert("Failed to load quiz. Please try again.");
  });

function createOption(qId, option) {
  return `
    <div class="form-check">
      <input class="form-check-input" 
             type="radio"
             name="q${qId}"
             value="${option}"
             onchange="responses[${qId}]='${option}'">
      <label class="form-check-label">${option}</label>
    </div>`;
}

function submitQuiz() {

  if (quizId === undefined || quizId === null) {
    alert("Quiz not loaded yet. Please wait.");
    return;
  }

  if (Object.keys(responses).length === 0) {
    alert("Please answer at least one question");
    return;
  }

  // Ensure numeric keys
  const numericResponses = {};
  Object.keys(responses).forEach(k => {
    numericResponses[Number(k)] = responses[k];
  });

  const payload = {
    quizId: Number(quizId),
    responseMap: numericResponses
  };

  console.log("Sending payload to backend:", JSON.stringify(payload, null, 2));

  fetch("/quiz/result", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(payload)
  })
    .then(res => res.json())
    .then(result => {
      console.log("Result received:", result);
      showResult(result);
    })
    .catch(err => console.error("Submit error:", err));
}

function showResult(result) {
  // Disable all options
  document.querySelectorAll("input[type=radio]").forEach(r => r.disabled = true);

  const correctMap = result.correctAnswerMap;
  const markedMap = result.markedAnswerMap;

  if (!correctMap || !markedMap) {
    alert("Result data missing from backend");
    return;
  }

  // Iterate over the keys of the object
  // Helper: parse question key robustly. Backend may send JSON-stringified objects
  // or a toString() like "QuestionWrapper(id=10, ...)". Return an object with an `id`.
  const parseQuestionKey = key => {
    try {
      return JSON.parse(key);
    } catch (e) {
      const m = String(key).match(/id=(\d+)/);
      if (m) return { id: Number(m[1]) };
      const n = Number(key);
      if (!Number.isNaN(n)) return { id: n };
      return { id: key };
    }
  };

  const norm = s => (s === null || s === undefined) ? '' : String(s).trim().replace(/\s+/g, ' ').toLowerCase();

  Object.keys(correctMap).forEach(qKey => {
    const question = parseQuestionKey(qKey);
    const correctAnswer = correctMap[qKey];
    const markedAnswer = markedMap[qKey] || null;

    const qDiv = document.getElementById(`question-${question.id}`);
    if (!qDiv) return;

    const labels = qDiv.querySelectorAll("label");

    labels.forEach(label => {
      const optionText = label.innerText || label.textContent || '';

      if (norm(optionText) === norm(correctAnswer)) {
        label.classList.add("correct-answer");
      }

      if (markedAnswer && norm(optionText) === norm(markedAnswer) && norm(markedAnswer) !== norm(correctAnswer)) {
        label.classList.add("wrong-answer");
      }
    });
  });

  // Show score
  const resultDiv = document.getElementById("result");
  resultDiv.classList.remove("d-none");
  resultDiv.classList.add("result-box");
  resultDiv.innerHTML = `
    <h4>Score</h4>
    <p><strong>${result.score} / ${Object.keys(correctMap).length}</strong></p>
  `;
}

