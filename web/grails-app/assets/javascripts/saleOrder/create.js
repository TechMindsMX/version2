$( function() {
  $( "#datepicker" ).datepicker({
    dateFormat: 'dd/mm/yy'
  });
} );

$("#currencyUsd").click(function() {
  if ($("#currencyUsd").prop("checked")) {
    $("#changeType").val("");
    $("#changeType").focus();
    $("#changeType").prop("readOnly", false);
  } else {
    $("#changeType").val("0.00");
    $("#changeType").prop("readOnly", true);
  }
});

