const rightBtnToggle = document.getElementById("right-menu-toggle");
rightBtnToggle.addEventListener("click", (e)=>{
    e.preventDefault();
    const rightWrapper = document.getElementById("wrapper");
    const rightImgSidebarClose = document.getElementById("right-sidebar-close-img");
    const rightImgSidebarOpen = document.getElementById("right-sidebar-open-img");

    rightWrapper.classList.toggle("right-toggled");
    rightImgSidebarClose.classList.toggle("d-none");
    rightImgSidebarOpen.classList.toggle("d-none");
});