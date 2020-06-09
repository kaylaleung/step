window.addEventListener('DOMContentLoaded', (event) => {
    const element = document.getElementsByClassName('menu')[0];
    element.addEventListener('click', (tab) => {
        toggleActiveSection(tab);
    });
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
