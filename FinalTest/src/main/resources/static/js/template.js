	document.addEventListener("DOMContentLoaded", function () {
    // 菜單展開、關閉功能
    window.openSidenav = function () {
        document.getElementById("sidenav").style.width = "100%";
        document.body.style.overflow = "hidden";
    };

    window.closeSidenav = function () {
        document.getElementById("sidenav").style.width = "0%";
        document.body.style.overflow = "";
    };

    // 展開菜單選項
    var dropdowns = document.getElementsByClassName("tem-dropdown-btn");
    for (var i = 0; i < dropdowns.length; i++) {
        dropdowns[i].addEventListener("click", function () {
            this.classList.toggle("tem-active");
            var dropdownContent = this.nextElementSibling;
            dropdownContent.style.display = dropdownContent.style.display === "block" ? "none" : "block";
        });
    }

    // menu toggle up/down 圖案
    window.togglePic1 = function () {
        var margin1 = document.getElementById("add");
        var img1 = document.getElementById("updown1");
        if (img1.src.includes("down.png")) {
            img1.src = "./material/icon/up.png";
            margin1.style.margin = "40px 0";
        } else {
            img1.src = "./material/icon/down.png";
            margin1.style.margin = "";
        }
    };

    window.togglePic2 = function () {
        var margin2 = document.getElementById("add");
        var img2 = document.getElementById("updown2");
        if (img2.src.includes("down.png")) {
            img2.src = "./material/icon/up.png";
            margin2.style.margin = "40px 0";
        } else {
            img2.src = "./material/icon/down.png";
            margin2.style.margin = "";
        }
    };

/*
    document.getElementById('togglePic1').addEventListener('click', () => {
        var margin1 = document.getElementById("add");
        var img1 = document.getElementById("updown1");
        if (img1.src.includes("down.png")) {
            img1.src = "./material/icon/up.png";
            margin1.style.margin = "40px 0";
        } else {
            img1.src = "./material/icon/down.png";
            margin1.style.margin = "";
        }
    });

    document.getElementById('togglePic2').addEventListener('click', () => {
        var margin2 = document.getElementById("add");
        var img2 = document.sgetElementById("updown2");
        if (img2.src.includes("down.png")) {
            img2.src = "./material/icon/up.png";
            margin2.style.margin = "40px 0";
        } else {
            img2.src = "./material/icon/down.png";
            margin2.style.margin = "";
        }
    });
*/
    // 會員功能菜單顯示/隱藏
    let memberInfoDiv = document.getElementById("memberInfoDiv");
	let slideshowContainer =document.getElementById("slideshow-container");
    let isMemberDivVisible = false; // 初始為隱藏

    document.getElementById('memberIcon').addEventListener('click', (e) => {
        e.stopPropagation(); // 防止點擊會員圖標時觸發頁面其他地方的點擊事件
        if (isMemberDivVisible) {
            memberInfoDiv.style.display = "none";
			slideshowContainer.style.zIndex=5;
            isMemberDivVisible = false;
        } else {
            memberInfoDiv.style.display = "block";
            isMemberDivVisible = true;
			slideshowContainer.style.zIndex=-1;
        }
    });

    // 點擊頁面其他地方時隱藏會員功能菜單
    document.addEventListener('click', function (e) {
        e.stopPropagation();
        if (isMemberDivVisible && !memberInfoDiv.contains(e.target)) {
            memberInfoDiv.style.display = "none";
			slideshowContainer.style.zIndex=5;
            isMemberDivVisible = false;
        }
    });

    // 搜索框顯示/隱藏
    let isOpen = false;
    let searchDiv = document.getElementById('searchDiv');
    let searchIcon = document.getElementById('searchIcon');
    
    searchIcon.addEventListener('click', () => {
        if (!isOpen) {
            searchDiv.style.width = '200px';
            searchDiv.style.border = '1px solid #a1c14b';
            isOpen = true;
        } else {
            searchDiv.style.width = '0';
            searchDiv.style.border = '0';
            isOpen = false;
        }
    });

    // 搜索框的 Enter 事件
    let searchInput = document.getElementById('searchInput');
    searchInput.addEventListener('keydown', (event) => {
        if (event.key === "Enter") {
            let keyword = searchInput.value;
            alert('您輸入的關鍵字是 : ' + keyword);
        }
    });

    // 點擊頁面其他地方時隱藏搜索框和會員功能菜單
    document.getElementById('myContainer').addEventListener('click', () => {

        if (isOpen) {
            searchDiv.style.width = '0';
            searchDiv.style.border = '0';
            isOpen = false;
        }
        if (isMemberDivVisible) {
            memberInfoDiv.style.display = "none";
			slideshowContainer.style.zIndex=5;
            isMemberDivVisible = false;
        }

    });

    //header的logo點了會跳到首頁
    document.getElementById('logoDiv').addEventListener('click', () => {
        window.location.href = '/enjoyum';
    });

    //判斷使用者是否為登入狀態
    let loginDiv = document.getElementById('loginDiv');
    let logoutDiv = document.getElementById('logoutDiv');

    loginDiv.style.display = 'none';
    logoutDiv.style.display = 'none';

    fetch('http://localhost:8080/users/checkSession',  {
        method : 'GET'
    }).then(response => {
        if(!response.ok){
            throw new Error('Error : ');
        }
        return response.json();
    }).then(data => {
        if(data){
            loginDiv.style.display = 'none';
            logoutDiv.style.display = 'block';
        }else{
            loginDiv.style.display = 'block';
            logoutDiv.style.display = 'none';
        }
    }).catch(error => {
        console.log('Error :' + error);
    })
	
	//登出
	document.getElementById('logout').addEventListener('click', () => {
		fetch('http://localhost:8080/users/logout',{
			method : 'GET'
		}).then(response => {
			if(!response.ok){
		      throw new Error('Error : ');
		    }
			return response.json();
			
	    }).then(data => {
			if (typeof data === 'boolean') {
	            if (data) {
					alert('成功登出');
					window.location.href = '/enjoyum';
	            } 
	        } else {
	            console.log('Unexpected response data:', data);
	        }
		}).catch(error => {
	        console.error('Error:', error);
	    })
	});
});
