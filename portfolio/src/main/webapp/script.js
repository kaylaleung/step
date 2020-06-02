window.addEventListener('DOMContentLoaded', (event) => {

    let element = document.getElementsByClassName("menu")[0];
    element.addEventListener("click", (tab) => {
        toggleActiveSection(tab);
    });

    let commentButton = document.getElementById("btn");
    nameButton.addEventListener('click', (event) => {
        getComments();
    });

});

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

function getComments() {
    fetch('/data').then(response => response.json()).then((comments) => {
        
        const commentElement = document.getElementById('comment-container');
        commentElement.innerHTML = '';

        for (let comment of comments) {
            commentElement.append(comment + " ");           
        }
    });
}
