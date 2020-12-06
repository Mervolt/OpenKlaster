const possible_slides = ['intro', 'trees', 'power_chart'];
const urlParams = new URLSearchParams(window.location.search);
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
    const slideChangeTimeout = urlParams.get('slideChangeTimeout') != null ? urlParams.get('slideChangeTimeout') : config["slideChangeTimeout"];
    setTimeout(showSlides, slideChangeTimeout);
}

function getSlides() {
    let classSlides = document.getElementsByClassName("slide");
    let slidesToShow = [];
    let filteredSlides = [];
    possible_slides.forEach(value => {
        const bool = urlParams.get(value) != null ? urlParams.get(value) : config["slides"][value];
        if (bool === true || bool === 'true') slidesToShow.push(value)
    });
    for (i = 0; i < classSlides.length; i++) {
        classSlides[i].style.display = "none";
        if (slidesToShow.includes(classSlides[i].id)) filteredSlides.push(classSlides[i])
    }
    return filteredSlides;
}
