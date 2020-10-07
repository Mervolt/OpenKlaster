let slideIndex = 0;
const slides = getSlides();
showSlides();

function showSlides() {
    for (let i = 0; i < slides.length; i++) {
        slides[i].style.display = "none";
    }
    slideIndex++;
    if (slideIndex > slides.length) {
        slideIndex = 1
    }
    slides[slideIndex - 1].style.display = "block";
    setTimeout(showSlides, config["slideChangeTimeout"]);
}

function getSlides() {
    let classSlides = document.getElementsByClassName("slide");
    let slidesFromConfig = config["slides"];
    let slidesToShow = [];
    let filteredSlides = [];
    for (let key of Object.keys(slidesFromConfig)) {
        if (slidesFromConfig[key] === true) slidesToShow.push(key);
    }
    for (i = 0; i < classSlides.length; i++) {
        classSlides[i].style.display = "none";
        if (slidesToShow.includes(classSlides[i].id)) filteredSlides.push(classSlides[i])
    }
    return filteredSlides;
}