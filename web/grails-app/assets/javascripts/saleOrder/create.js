$( function() {
  $( "#datepicker" ).datepicker({
    dateFormat: 'dd/mm/yy'
  });
} );

$("#currencyUsd").click( function() {
  if ($("#currencyUsd").is(":checked")) {
    $("#changeTypeSection").show()
    $("#changeType").attr("required", true);
  } else {
    $("#changeTypeSection").hide()
    $("#changeType").attr("required", false);
  }
});
