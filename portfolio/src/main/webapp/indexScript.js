window.addEventListener('DOMContentLoaded', (event) => {
  getBlogs();
  const selectElement = document.getElementById('cat-select');     
  selectElement.addEventListener('change', function() {
    getBlogs();
  });
});

function getBlogs() {
  fetch('/post').then(response => response.json()).then((posts) => {
    const blogListElement = document.getElementById('blog-posts');     
    const selectedElement = document.getElementById('cat-select');
    blogListElement.innerHTML = '';  
    for (post of posts) {
      if (selectedElement.value === 'All'|| post.category === selectedElement.value) {
          blogListElement.appendChild(createBlogElement(post));
      }
    }
  });
}

function createBlogElement(post) {
  const cardElement = document.createElement('div');
  cardElement.className = 'blog-card';

  const imgElement = document.createElement('img');
  imgElement.src = 'images/ai.jpg';
  imgElement.width = '300';
  imgElement.height = '200';

  const contentElement = document.createElement('div');
  contentElement.className = 'content';

  const titleElement = document.createElement('h3');
  titleElement.innerText = post.title;

  const blurbElement = document.createElement('p');
  blurbElement.innerText = post.blogpost.length < 200 ? post.blogpost : post.blogpost.substring(0,200);

  contentElement.appendChild(titleElement);
  contentElement.appendChild(blurbElement);

  const linkElement = document.createElement('a');
  linkElement.className = 'link';
  linkElement.href = '/blog.html?tag=' + post.tag;
  linkElement.innerText = 'Read More';

  cardElement.appendChild(imgElement);
  cardElement.appendChild(contentElement);
  cardElement.appendChild(linkElement);

  return cardElement;
}
