
window.addEventListener('DOMContentLoaded', (event) => {
  const user = document.getElementById('see-post');
  const admin = document.getElementById('write-post');
  const role = new URL(document.URL).searchParams.get('user');
  const id = new URL(document.URL).searchParams.get('id');

  if (role !== "admin" && id !== null) {
    getID(id);
    getPost(id);
    getComments(id);
    user.style.display = 'block';
  }
  else {
    setAdmin(admin);
  }
});

function getID(id) {
    const idElement = document.getElementById('current-id');
    idElement.value = id;
}

function getPost(id) {
  fetch('/post?id=' + id).then(response => response.json()).then((post) => {  
    const postElement = document.getElementById('post-container');
    const titleElement = document.getElementById('title-container');
    titleElement.innerHTML = '';
    postElement.innerHTML = '';
    postElement.append(post[0].blogpost);
    titleElement.append(post[0].title);
  });
}

function getComments(id) {
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

function setAdmin(admin) {
  fetch('/auth').then(response => response.json()).then((log) => {  
    if (log.logoutUrl === '' || log.email !== 'Kaylamyhome@gmail.com') {
      admin.innerHTML = '<h1> Not Authorized </h1>';
    }
  });
  admin.style.display = 'block';
}
