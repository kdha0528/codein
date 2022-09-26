const containerSigninCard = document.getElementById("container-signin-card");
const containerSignupCard = document.getElementById("container-signup-card");
const btnSignup = document.getElementById("btn-signup");

btnSignup.addEventListener("click",()=>{
    containerSigninCard.classList.add('fade-out');
});

containerSigninCard.addEventListener("animationend",()=>{
    containerSigninCard.style.display = "none";
    containerSignupCard.classList.add('fade-in');
    containerSignupCard.style.display = "block";
})
