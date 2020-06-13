window.addEventListener('DOMContentLoaded', (event) => {
    getURL();
    getPost();
    getComments();
});

const id = new URL(document.URL).searchParams.get('id');

function getURL() {
    const urlElement = document.getElementById('current-id');
    urlElement.value = id;
}

function getPost() {
  fetch('/post?id=' + id).then(response => response.json()).then((post) => {  
    const postElement = document.getElementById('post-container');
    const titleElement = document.getElementById('title-container');
    titleElement.innerHTML = '';
    postElement.innerHTML = '';
    postElement.append(post[0].blogpost);
    titleElement.append(post[0].title);
  });
}

function getComments() {
  fetch('/comment?id=' + id).then(response => response.json()).then((comments) => {
    const commentListElement = document.getElementById('comment-list');
    for (comment of comments) {
      commentListElement.appendChild(createCommentElement(comment));
    }
  });
}

function createCommentElement(comment) {
  const commentElement = document.createElement('li');
  commentElement.className = 'comment';

  const nameElement = document.createElement('span');
  nameElement.innerText = comment.name;
  nameElement.className = 'username';

  const timeElement = document.createElement('span');
  const date = new Date(comment.timestamp);
  timeElement.innerText = date.toDateString();
  timeElement.className = 'time';

  const textElement = document.createElement('div');
  textElement.innerText = comment.text;
  textElement.className = 'response';

  commentElement.appendChild(nameElement);
  commentElement.appendChild(timeElement);
  commentElement.appendChild(textElement);

  return commentElement;
}
