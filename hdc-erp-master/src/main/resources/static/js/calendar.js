function scheduleAddFunc(){
    const scheduleForm = document.getElementById("scheduleAdd")
    const startScheduleDate = document.getElementById("startScheduleDate");
    const endScheduleDate = document.getElementById("endScheduleDate");
    const scheduleDetail = document.getElementById("scheduleDetail");

    /*console.log(startScheduleDate.value);
    console.log(endScheduleDate.value);
    console.log(scheduleDetail.value);*/

    scheduleForm.submit();

    $("#exampleModal").modal('hide');
}

/*function actionHandler(e = event){
    const scheduleId = e.target.previousElementSibling.value;
    console.log(scheduleId);
}*/

//해당 날짜를 가져와서 사이드 바에 출력
function tdActionHandler(e = event) {

    if(e.target == e.currentTarget){
        const workList = document.getElementById("work-list");
        if(workList.children.length != 0 ){ //만약 이전에 클릭이벤트가 일어나서 이미 사이드바에 결과가 출력되어있을경우 해당 내용을 삭제
            while (workList.hasChildNodes()) {
                workList.removeChild(workList.firstChild);
            }
        }

        //클릭한 날짜의 모든 내용 출력
        for (let icChildren = 0; icChildren < e.target.children.length; icChildren++) {
            if(e.target.children[icChildren].localName == "p"){
                const createPTag = document.createElement("p");

                createPTag.appendChild(document.createTextNode(e.target.children[icChildren].innerText));
                workList.appendChild(createPTag);

                // console.log(e.target.children[icChildren].innerText);
                // console.log(e.target.children[icChildren].nextElementSibling.defaultValue);
            }
        }
    }
}