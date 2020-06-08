window.addEventListener('DOMContentLoaded', (event) => {

    let element = document.getElementsByClassName('menu')[0];
    element.addEventListener('click', (tab) => {
        toggleActiveSection(tab);
    });
    getBlogs();

    const selectElement = document.getElementById('mySelect');     
    selectElement.addEventListener("change", function() {
      getBlogs();
      
  });
  
});



function toggleActiveSection(tab) {
<<<<<<< HEAD
  
    let elems = document.querySelector(".active");
    if (elems != null) {
      elems.classList.remove("active");
    }
    if (tab.target.nodeName === 'I') {
        tab.target.parentNode.classList.add("active");
    }
    else {
      tab.target.classList.add("active");
    }
}

function getBlogs() {

  fetch('/post').then(response => response.json()).then((posts) => {
   
    const blogListElement = document.getElementById('blog-posts');     
    const selectedElement = document.getElementById('mySelect');

    blogListElement.innerHTML = '';
    console.log(selectedElement.value);

    posts.forEach((post) => {

        if ( selectedElement.value === "All") {
          blogListElement.appendChild(createBlogElement(post));
          console.log("equals all");
        }
        else if (post.category === selectedElement.value) {
          blogListElement.appendChild(createBlogElement(post));
          console.log("category match found");

        }
    })

  });
}

function createBlogElement(post) {
  const cardElement = document.createElement('div');
  cardElement.className = 'blog-card';

  const imgElement = document.createElement('img');
  imgElement.src = 'images/ai.jpg';
  imgElement.width="300";
  imgElement.height="200";
 
  const contentElement = document.createElement('div');
  contentElement.className = 'content';
  const titleElement = document.createElement('h3');
  titleElement.innerText = post.title;
  const blurbElement = document.createElement('p');
  blurbElement.innerText = (post.blogpost).length < 25 ? post.blogpost : post.blogpost.substring(0,200);
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
=======
      let elems = document.querySelector('.active');
      if (elems != null) {
        elems.classList.remove('active');
      }
      if (tab.target.nodeName === 'I') {
        tab.target.parentNode.classList.add('active');
      }
      else {
        tab.target.classList.add('active');
      }
>>>>>>> master
}
