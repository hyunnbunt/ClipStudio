function showVideo() = document.getElementById('video-button')
.addEventListener('click', function() {
  fetch('/api/videos/1')
    .then(response => response.json())
    .then(data => {
      // API 응답 처리
      console.log(data);
    })
    .catch(error => {
      console.error('API 요청 오류:', error);
    });
});