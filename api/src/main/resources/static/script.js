        let videoStoppedTime = 0;
        const setVideoStoppedTime = () => {
            videoStoppedTime = document.getElementById("videoStoppedTime").value;
            console.log(videoStoppedTime)

        }

        document.getElementById('video-play-form')
        .addEventListener('submit', function() {
        fetch('/api/videos/1', {
            method: "POST",
            mode: "cors",
            cache: "no-cache",
            credentials: "same-origin",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                "videoStoppedTime" : videoStoppedTime,
            })
        }).then(res => {
            console.log(res);
        })
        .catch(error => {
        console.error('API 요청 오류:', error);
        });
        });