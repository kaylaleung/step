window.addEventListener('DOMContentLoaded', (event) => {
    const element = document.getElementsByClassName('menu')[0];
    element.addEventListener('click', (tab) => {
        toggleActiveSection(tab);
    });
    setLogin();
    createMap();
});

function toggleActiveSection(tab) {
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
}

function setLogin() {
  fetch('/auth').then(response => response.json()).then((log) => {  
    let login = document.getElementById('auth');
    if ("" === log.logoutUrl) {
      login.href = log.loginUrl;
      login.innerHTML = '<i class="fas fa-user-lock"></i>Login';
    }
    else {
      login.href = log.logoutUrl;
      login.innerHTML = '<i class="fas fa-user-lock"></i>Logout';
    }
  });
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
