function getPost() {
  fetch('/post').then(response => response.json()).then((posts) => {

      // console.log(location.search);
      var number = parent.document.URL.substring(parent.document.URL.indexOf('postNum='), parent.document.URL.length);
      console.log(number.substring(8))

      const postElement = document.getElementById('post-container');

      console.log(postElement);
      postElement.innerHTML = '';

      posts.forEach((post) => {
        console.log(post.title);
        postElement.innerHTML = post.blogpost;        
      }) 
      
  });
}

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
  let date = new Date(comment.timestamp);
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
