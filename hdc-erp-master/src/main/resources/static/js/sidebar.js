const btnToggle = document.getElementById("menu-toggle");
btnToggle.addEventListener("click", (e)=>{
    e.preventDefault();
    const wrapper = document.getElementById("wrapper");
    const imgSidebarClose = document.getElementById("sidebar-close-img");
    const imgSidebarOpen = document.getElementById("sidebar-open-img");

    wrapper.classList.toggle("toggled");
    imgSidebarClose.classList.toggle("d-none");
    imgSidebarOpen.classList.toggle("d-none");
});