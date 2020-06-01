window.addEventListener('DOMContentLoaded', (event) => {

  let element = document.getElementsByClassName("menu")[0];
  element.addEventListener("click", toggleActiveSection);

  function toggleActiveSection(tab) {
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
});

async function getName() {
  const response = await fetch('/data');
  const name = await response.text();
  document.getElementById('name-container').innerText = name;
}

