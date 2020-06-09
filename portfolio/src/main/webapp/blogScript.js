window.addEventListener('DOMContentLoaded', (event) => {
    getURL();
    getPost();
    getComments();
    getUser();
});

const number = parent.document.URL.substring(parent.document.URL.indexOf('tag='), parent.document.URL.length);
const tagStr = number.substring(4);

function getURL() {
    const urlElement = document.getElementById('current-url');
    urlElement.value = '/blog.html?tag=' + tagStr;
}

function getUsert() {
  fetch('/auth').then(response => response.json()).then((user) => {  
  });

}
function getPost() {

  fetch('/post?tag=' + tagStr).then(response => response.json()).then((posts) => {  
    const postElement = document.getElementById('post-container');
    const titleElement = document.getElementById('title-container');
    postElement.innerHTML = '';
    titleElement.innerHTML ='';

    for (post of posts) {
      if ((post.tag) === tagStr) {
        postElement.append(post.blogpost);
        titleElement.append(post.title);
      }
    }
  });
}

function getComments() {
    fetch('/comment?tag=' + tagStr).then(response => response.json()).then((comments) => {
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
