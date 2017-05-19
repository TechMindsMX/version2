$( function() {
  $( "#datepicker" ).datepicker({
    dateFormat: 'dd/mm/yy',
  });
} );

$("#type").change( function() {
  if ($("#type").val() == "CREDITO") {
    $("#reconcilable").show()
  } else {
    $("#reconcilable").hide()
  }
})
