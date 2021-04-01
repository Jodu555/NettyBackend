const table = document.getElementById('table');

let mondaySubjects = [{
        subject: "English",
        time: "08:30"
    },
    {
        subject: "Mathe",
        time: "09:40"
    }
];

filltable();

function filltable() {
    var mondaystate = 0;


    //2 == biggest day with subjects
    for (let index = 0; index < 2; index++) {
        var row = table.insertRow(-1);
        var monday = row.insertCell(0);
        var tuesday = row.insertCell(1);
        monday.innerHTML = mondaySubjects[mondaystate].subject + " um " + mondaySubjects[mondaystate].time;
        tuesday.innerHTML = "NEW CELL2";
        mondaystate++;
    }

}