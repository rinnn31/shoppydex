window.onload = async function() {
    let info = await getUserInfo(true);

    const userProfile = this.document.getElementById('user-profile');
    const userPoint = this.document.getElementById('user-point');

    if (info) {
        userPoint.children[0].innerText = `${info.points} `;
        userProfile.children[1].innerText = info.username;
        userProfile.onclick = function() {
            const popupMenu = document.getElementById('popupMenu');
            popupMenu.classList.toggle('active');
        };
        this.document.addEventListener('click', function(event) {
            const popupMenu = document.getElementById('popupMenu');
            if (!userProfile.contains(event.target)) {
                popupMenu.classList.remove('active');
            }
        });
        if (info.isAdmin) {
            this.document.getElementsByClassName('main-navbar')[0].insertAdjacentHTML("beforeend", `
                <div class="popup-menu" id="popupMenu">
                    <a href="/don-hang">Xem đơn hàng</a>
                    <a href="/quan-ly">Quản lí sản phẩm</a>
                    <a href="/thong-tin">Cập nhật thông tin</a>
                    <a href="#" onclick="logout()">Đăng xuất</a>
                </div>`);
        } else {
            this.document.getElementsByClassName('main-navbar')[0].insertAdjacentHTML("beforeend", `
                <div class="popup-menu" id="popupMenu">
                    <a href="/don-hang">Xem đơn hàng</a>
                    <a href="/thong-tin">Cập nhật thông tin</a>
                    <a href="#" onclick="logout()">Đăng xuất</a>
                </div>`);
        }
    } else {
        userPoint.children[0].innerText = `0 `;
        userProfile.onclick = function() {
            window.location.href = '/dang-nhap';
        };
        userProfile.children[1].innerText = 'Đăng nhập';
    }
}

function logout() {
    localStorage.removeItem('authToken');
    cookieStore.delete('authToken');
    sessionStorage.removeItem('userInfo');
    window.location.href = '/';
}

function isLoggedIn() {
    let cachedUserInfo = sessionStorage.getItem('userInfo');
    return cachedUserInfo != null;
}

async function getUserInfo(force = false) {
    if (force || !isLoggedIn()) {
        let res = await UserAPI.getProfile();
        if (res && res.code === 0) {
            sessionStorage.setItem('userInfo', JSON.stringify(res.data));
            return res.data;
        } else {
            sessionStorage.removeItem('userInfo');
            return null;
        }
    }
    return JSON.parse(sessionStorage.getItem('userInfo'));
}