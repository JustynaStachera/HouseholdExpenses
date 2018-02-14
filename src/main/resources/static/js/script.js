var containerEl = document.getElementById('div-container');

containerEl.addEventListener('click', function (event) {
    var divDropdown = document.getElementById("div-dropdown-list");
    if (event.target.id !== 'img-chevron-down') {
        divDropdown.classList.add('hide');
        divDropdown.classList.remove('show');
    }
    else {
        divDropdown.classList.toggle('show');
        divDropdown.classList.remove('hide');
    }
}, false);

function checkBoxes() {
    var checkboxAdmin = document.getElementById('admin');
    var checkboxRead = document.getElementById('read');
    var checkboxAdd = document.getElementById('add');
    var checkboxModify = document.getElementById('modify');

    if (checkboxAdmin !== null) {
        checkboxAdmin.addEventListener('change', function () {
            if (checkboxAdmin.checked === true) {
                checkboxRead.checked = true;
                checkboxAdd.checked = true;
                checkboxModify.checked = true;
            }

            if (checkboxAdmin.checked === false) {
                checkboxRead.checked = false;
                checkboxAdd.checked = false;
                checkboxModify.checked = false;
            }

            if (checkboxRead.checked === false
                || checkboxModify === false
                || checkboxAdd.checked === false) {
                checkboxAdmin.checked = false;
            }
        }, false);
    }

    if (checkboxRead !== null) {
        checkboxRead.addEventListener('change', function () {
            if (checkboxRead.checked === false) {
                checkboxAdmin.checked = false;
            }

            if (checkboxRead.checked === false && checkboxAdd.checked === false) {
                checkboxModify.checked = false;
            }
        }, false);
    }

    if (checkboxAdd !== null) {
        checkboxAdd.addEventListener('change', function () {
            if (checkboxAdd.checked === false) {
                checkboxAdmin.checked = false;
            }

            if (checkboxAdd.checked === false && checkboxRead.checked === false) {
                checkboxModify.checked = false;
            }
        }, false);
    }

    if (checkboxModify !== null) {
        checkboxModify.addEventListener('change', function () {
            if (checkboxModify.checked === false) {
                checkboxAdmin.checked = false;
                checkboxAdd.checked = false;
                checkboxRead.checked = false;
            }

            if (checkboxModify.checked === true
                && checkboxRead.checked === false
                && checkboxAdd.checked === false) {
                checkboxAdd.checked = true;
                checkboxRead.checked = true;
            }
        }, false);
    }
};

checkBoxes();
