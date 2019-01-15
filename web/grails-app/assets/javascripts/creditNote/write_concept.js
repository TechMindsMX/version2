$("#selectWriteConcept").click( function() {
  if ($("#selectWriteConcept").is(":checked")) {
    $("#selectConcept").hide()
    $("#writeConcept").show()
    $("#sku").attr("readonly", false)
  } else {
    $("#selectConcept").show()
    $("#writeConcept").hide()
    $("#sku").attr("readonly", true)
  }
});
