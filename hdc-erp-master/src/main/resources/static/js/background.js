const BACKGROUND_CONTAINER = document.querySelector(".js-background");
const imageUrl = "https://source.unsplash.com/1920x1080/?";
function init() {
    BACKGROUND_CONTAINER.style = `background:url("${generateSeasonalQuery()}") no-repeat center/cover`;
}

function getMonth() {
    return new Date().getMonth();
}

function generateSeasonalQuery() {
    //zero is january
    const month = getMonth();
    if (2 <= month && month <= 4) {
        return `${imageUrl}spring,dark,season`;
    } else if (5 <= month && month <= 7) {
        return `${imageUrl}summer,dark,season`;
    } else if (8 <= month && month <= 10) {
        return `${imageUrl}fall,dark,season`;
    } else if (11 === month || (0 <= month && month <= 1)) {
        return `${imageUrl}winter,dark,season`;
    } else {
        console.error("month classifier error");
        return `${imageUrl}season`;
    }
}

init();