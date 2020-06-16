window.addEventListener('DOMContentLoaded', (event) => {
  getAllBlogs();
  const selectElement = document.getElementById('cat-select');     
  selectElement.addEventListener('change', function() {
    getAllBlogs();
  });
  createMap();
});

function getAllBlogs() {
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
  blurbElement.innerText = post.blogpost.substring(0, 200);

  contentElement.appendChild(titleElement);
  contentElement.appendChild(blurbElement);

  const linkElement = document.createElement('a');
  linkElement.className = 'link';
  linkElement.href = '/blog.html?id=' + post.id;
  linkElement.innerText = 'Read More';

  cardElement.appendChild(imgElement);
  cardElement.appendChild(contentElement);
  cardElement.appendChild(linkElement);

  return cardElement;
}

function createMap() {
  const cmu = {lat: 40.443378, lng: -79.944404};
  const map = new google.maps.Map(document.getElementById('map'), {
    zoom: 18,
    center: cmu,
    mapTypeId: 'satellite',
    tilt: 45,
  });

  marker = new google.maps.Marker({
    map: map,
    draggable: true,
    animation: google.maps.Animation.DROP,
    position: cmu,
  });

  const infowindow = new google.maps.InfoWindow({
    content: 'Carnegie Mellon University: School of Computer Science',
    maxWidth: 400,
  });

  marker.addListener('click', function() {
    infowindow.open(map, marker);
  });
}
