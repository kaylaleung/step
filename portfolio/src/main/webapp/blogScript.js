window.addEventListener('DOMContentLoaded', (event) => {
    getComments();
});

function getComments() {
    fetch('/data').then(response => response.json()).then((comments) => {
        
        const commentElement = document.getElementById('comment-container');
        commentElement.innerHTML = '';

        for (let comment of comments) {
            commentElement.append(comment +  ", ");           
        }
    });
}
