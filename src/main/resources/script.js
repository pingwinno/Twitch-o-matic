var path = "http://localhost:20000/management_api/";

function write() {
    $("#table").empty();
    $.getJSON(path + "streams", function (data) {
        jQuery.each(data, function (index, value) {
            $("#table").append(
                "<tr>" +
                "<th scope='row'>" + value.uuid + "</th>" +
                "<td><textarea style='height:115px;' class='form-control' id='" + value.uuid + "Title' readonly>" + value.title + "</textarea></td>" +

                "<td><textarea style='height:115px;' class='form-control' " +
                "title='" + timeConverter(value.date) + "' onkeyup='titleReWrite(this)' " +
                "id='" + value.uuid + "Date' readonly>" + value.date + "</textarea></td>" +

                "<td><textarea style='height:115px;' class='form-control' id='" + value.uuid + "Game' readonly>" + value.game + " </textarea></td>" +
                "<td class='form text-right'>" +
                "<button class='btn btn-primary' onclick='edit(this,\"" + value.uuid + "\")' style='width: 80px'>Edit</button><br>" +
                "<input type='checkbox' id='" + value.uuid + "Mute'> Skip muted " +
                "<button class='btn btn-warning' onclick='validate(\"" + value.uuid + "\")' style='width: 80px'>Validate</button><br>" +
                "<input type='checkbox' value='true' id='" + value.uuid + "Media'> Delete media " +
                "<button class='btn btn-danger' onclick='deleteStrem(\"" + value.uuid + "\")' style='width: 80px'>Delete</button>" +
                "</td>" +
                "</tr>"
            );
        });
    });
}

function search(sender) {
    var value = sender.value;
    var hidden = [];

    $("#table").children().each(function () {
        var data = {
            uuid: $(this).children()[0].innerHTML,
            title: $(this).find('td textarea')[0].value,
            game: $(this).find('td textarea')[2].value
        };

        hidden.push((data.uuid.toLowerCase().includes(value.toLowerCase().trim()) || data.title.toLowerCase().includes(value.toLowerCase().trim()) || data.game.toLowerCase().includes(value.toLowerCase().trim())));
    });

    var trs = $("#table").children();
    for (var i = 0; i < trs.length; i++) {
        if (hidden[i])
            trs[i].removeAttribute("hidden");
        else
            trs[i].setAttribute("hidden", "");
    }
}

write();

function titleReWrite(sender) {
    sender.setAttribute("title", timeConverter(sender.value));
}

function edit(sender, uuid) {
    document.getElementById(uuid + "Title").removeAttribute('readOnly');
    document.getElementById(uuid + "Date").removeAttribute('readOnly');
    document.getElementById(uuid + "Game").removeAttribute('readOnly');

    var prevData = {
        title: document.getElementById(uuid + "Title").value,
        date: document.getElementById(uuid + "Date").value,
        game: document.getElementById(uuid + "Game").value
    };
    sender.onclick = function () {
        confirmEddition(sender, uuid, prevData)
    };
}

function confirmEddition(sender, uuid, prevData) {

    sender.onclick = function () {
        edit(sender, uuid)
    };

    var Title = document.getElementById(uuid + "Title");
    Title.setAttribute('readOnly', '');
    var Date = document.getElementById(uuid + "Date");
    Date.setAttribute('readOnly', '');
    var Game = document.getElementById(uuid + "Game");
    Game.setAttribute('readOnly', '');

    var answer = confirm("Update data?");
    if (answer) {
        var data = {
            uuid: uuid,
            date: Date.value,
            title: Title.value,
            game: Game.value
        };

        $.ajax({
            type: "POST",
            url: path + "update",
            data: JSON.stringify(data),
            dataType: "json",
            contentType: "application/json; charset=utf-8"
        });
    } else {
        Date.value = prevData.date;
        Title.value = prevData.title;
        Game.value = prevData.game;
    }
}

function deleteStrem(uuid) {
    axios.delete(path + uuid, {
        params: {
            deleteMedia: document.getElementById(uuid + "Media").checked
        }
    });
    write();
}

function validateMain() {
    var data = {
        uuid: document.getElementById("validateUUID").value,
        vodId: document.getElementById("validateVOD").value,
        skipMuted: document.getElementById("validateSkipMuted").checked
    };

    $.ajax({
        type: "POST",
        url: path + "validate",
        data: JSON.stringify(data),
        dataType: "json",
        contentType: "application/json; charset=utf-8"
    });
    write();
}

function validate(uuid) {
    var vodId = prompt("Please enter video Id");

    if (vodId == null || vodId.trim() == "") {
        alert("You cancelled the validation");
        return;
    }

    var data = {
        uuid: uuid,
        vodId: vodId,
        skipMuted: document.getElementById(uuid + "Mute").checked
    };

    $.ajax({
        type: "POST",
        url: path + "validate",
        data: JSON.stringify(data),
        dataType: "json",
        contentType: "application/json; charset=utf-8"
    });
    write();
}

function add() {
    var value = document.querySelector('input[name="value"]').value;
    if (value.trim() == "" && value == null) {
        alert("You forgot to enter value");
        return;
    }
    var data = {
        type: document.querySelector('input[name="type"]:checked').value,
        value: value,
        skipMuted: document.querySelector('input[name="skipMuted"]').checked
    };
    $.ajax({
        type: "POST",
        url: path + "add",
        data: JSON.stringify(data),
        dataType: "json",
        contentType: "application/json; charset=utf-8"
    });
    write();
}

function timeConverter(UNIX_timestamp) {
    var a = new Date();
    a.setTime(UNIX_timestamp);
    var months = ['янв', 'фев', 'мар', 'апр', 'мая', 'июн', 'июл', 'авг', 'сен', 'окт', 'ноя', 'дек'];
    var year = a.getFullYear();
    var month = months[a.getMonth()];
    var date = a.getDate();
    return date + ' ' + month + ' ' + year;
}
