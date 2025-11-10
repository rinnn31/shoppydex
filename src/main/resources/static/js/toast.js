/**
 * Hiển thị Toast Message.
 * @param {string} message - Nội dung thông báo.
 * @param {string} iconClass - Class của icon (ví dụ: 'fa-circle-check', 'fa-triangle-exclamation').
 * @param {string} iconColor - Màu của icon (ví dụ: '#4CAF50', '#f90').
 */
function showToast(message, iconClass = 'fa-circle-check', iconColor = '#4CAF50') {
    const toastElement = document.getElementById("toast");
    const toastMessage = document.getElementById("toast-message");
    const toastIcon = toastElement.querySelector('i');

    // Cập nhật nội dung và icon
    toastMessage.textContent = message;
    
    // Đảm bảo chỉ có một icon class
    toastIcon.className = '';
    toastIcon.classList.add('fa-solid', iconClass);
    toastIcon.style.color = iconColor;

    // 1. Thêm class 'show' để kích hoạt CSS animation
    toastElement.classList.add("show");

    // 2. Thiết lập hẹn giờ để loại bỏ class 'show' sau 3 giây (3000ms)
    setTimeout(function(){ 
        toastElement.classList.remove("show"); 
    }, 3000); // 3 giây hiển thị
}