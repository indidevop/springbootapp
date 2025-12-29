function addQuestion() {
  const payload = {
    questionTitle: document.getElementById("questionTitle").value,
    option1: document.getElementById("option1").value,
    option2: document.getElementById("option2").value,
    option3: document.getElementById("option3").value,
    option4: document.getElementById("option4").value,
    rightAnswer: document.getElementById("rightAnswer").value,
    difficultyLevel: document.getElementById("difficulty").value,
    category: document.getElementById("category").value
  };

  fetch("/question/add", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  })
    .then(() => alert("Question Added Successfully"))
    .catch(() => alert("Error adding question"));
}
