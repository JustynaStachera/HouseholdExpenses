$(function () {
    var divErrorMsg = $(".div-error-msg");
    var divSuccessMsg = $(".div-success-msg");

    divErrorMsg.delay(7500).fadeOut(1000);
    divSuccessMsg.delay(7500).fadeOut(1000);

    $('.remove').click(function () {
        return confirm("Jeśli usuniesz ten rekord, to usuniesz także wszystkie powiązane z nim rekordy. " +
            "Czy chcesz kontynuować?");
    });

    $('.a-remove-user').click(function () {
        return confirm("Jeśli usuniesz konto, to stracisz wszystkie informacje z nim związane. " +
            "Czy chcesz kontynuować?");
    });

    if ($('#dateOfPurchase').val() === 0) {
        $('#dateOfPurchase').val(moment().format('YYYY-MM-DD'));
    }

    if ($('#beginDate').val() === 0) {
        $('#beginDate').val(moment().format('YYYY-MM-DD'));
    }

    if ($('#forecastingDate').val() === 0) {
        $('#forecastingDate').val(moment().format('YYYY-MM-DD'));
    }

    if ($("#kindOfOperation option:selected").text() === 'Jednorazowa') {
        $('#daysLeft').prop("disabled", true).val("");
    }

    $('#kindOfOperation').change(function () {
        if ($("#kindOfOperation option:selected").text() === 'Cykliczna') {
            $('#daysLeft').prop("disabled", false);
        } else if ($("#kindOfOperation option:selected").text() === 'Jednorazowa') {
            $('#daysLeft').prop("disabled", true).val("");
        }
    });
});