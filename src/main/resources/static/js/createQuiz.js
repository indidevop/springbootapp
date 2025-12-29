// CREATE NEW QUIZ
document.getElementById("createQuizForm").addEventListener("submit", function (e) {
  e.preventDefault();

  const category = document.getElementById("category").value;
  const title = document.getElementById("title").value;
  const numQ = document.getElementById("numQ").value;

  if (!category || !title || !numQ) {
    alert("Please fill all fields");
    return;
  }

  fetch(`/quiz/create?category=${category}&numQ=${numQ}&title=${title}`, {
    method: "POST"
  })
    .then(res => {
      if (!res.ok) throw new Error();
      window.location.href = `quiz.html?title=${title}`;
    })
    .catch(() => alert("Error creating quiz"));
});

// PLAY EXISTING QUIZ
function playExistingQuiz() {
  const title = document.getElementById("existingQuizTitle").value;

  if (!title) {
    alert("Please enter quiz title");
    return;
  }

  // Redirect directly — backend already supports this
  window.location.href = `quiz.html?title=${title}`;
}
