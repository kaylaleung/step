window.addEventListener('DOMContentLoaded', (event) => {

  var element = document.getElementsByClassName("menu")[0];
  element.addEventListener("click", toggleActiveSection);

  function toggleActiveSection(tab) {
      var elems = document.querySelector(".active");
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
