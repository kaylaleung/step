window.addEventListener('DOMContentLoaded', (event) => {
    const element = document.getElementsByClassName('menu')[0];
    element.addEventListener('click', (tab) => {
        toggleActiveSection(tab);
    });
    setLogin();
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
      login.innerHTML = '<i class="fas fa-user"></i>Login';
      console.log("logged out, now set button to log in");
    }
    else {
      login.href = log.logoutUrl;
      login.innerHTML = '<i class="fas fa-user"></i>Logout';
      console.log("logged in, now set button to log out");
    }
  });
}