$( function() {
  $( "#stampedDateInit" ).datepicker({
    dateFormat: 'dd/mm/yy',
    maxDate: 0
  });
} );

$( function() {
  $( "#stampedDateEnd" ).datepicker({
    dateFormat: 'dd/mm/yy',
    maxDate: 0
  });
} );

$("#formFilters").submit(function (event) {
  if ($("#stampedDateInit").val() == "" || $("#stampedDateEnd").val() == "") {
    return true
  }

  var init = $("#stampedDateInit").datepicker("getDate");
  var end = $("#stampedDateEnd").datepicker("getDate");
  if (init.getTime() > end.getTime()) {
    $("#messageAlert").text("La fecha inicial es posterior a la fecha final");
    $("#modalAlert").modal('show');
    event.preventDefault();
  }

});

$("#stampedDateInit").change( function () {
  $("#status").removeAttr("disabled");
  if ($("#stampedDateInit").val() != '' || $("#stampedDateEnd").val() != '') {
    $("#status").attr("disabled", "true");
  }
});

$("#stampedDateEnd").change( function () {
  $("#status").removeAttr("disabled");
  if ($("#stampedDateInit").val() != '' || $("#stampedDateEnd").val() != '') {
    $("#status").attr("disabled", "true");
  }
});
