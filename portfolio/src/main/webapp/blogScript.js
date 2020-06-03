function getComments() {
    fetch('/data').then(response => response.json()).then((comments) => {
        
        const commentListElement = document.getElementById('comment-list');

        comments.forEach((comment) => {
          commentListElement.appendChild(createCommentElement(comment));
        }) 
    });
}

function createCommentElement(comment) {
  const commentElement = document.createElement('li');
  commentElement.className = 'comment';
  

  const nameElement = document.createElement('span');
  nameElement.innerText = comment.name;
  nameElement.className = "username"

  const timeElement = document.createElement('span');
  var date = new Date(comment.timestamp);
  timeElement.innerText = date.toDateString(); 
  timeElement.className = "time"

  const textElement = document.createElement('div');
  textElement.innerText = comment.text;
  textElement.className = "response"

  commentElement.appendChild(nameElement);
  commentElement.appendChild(timeElement);
  commentElement.appendChild(textElement);

  return commentElement;
}