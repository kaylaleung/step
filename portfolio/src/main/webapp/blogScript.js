function getComments() {
    fetch('/data').then(response => response.json()).then((comments) => {
        
        const commentListElement = document.getElementById('comment-list');

        comments.forEach((comment) => {
          commentListElement.appendChild(createCommentElement(comment));
        }) 
    });
}

function createCommentElement(task) {
  const commentElement = document.createElement('li');
  commentElement.className = 'comment';
  

  const textElement = document.createElement('span');
  textElement.innerText = task.name;

  const nameElement = document.createElement('span');
  nameElement.innerText = task.text;

  commentElement.appendChild(textElement);
  commentElement.appendChild(nameElement);
  return commentElement;
}
