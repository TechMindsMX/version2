$("#selectWriteConcept").click( function() {
  if ($("#selectWriteConcept").is(":checked")) {
    $("#selectConcept").hide()
    $("#writeConcept").show()
    $("#sku").attr("readonly", false)
    $("#unitType").attr("readonly", false)
  } else {
    $("#selectConcept").show()
    $("#writeConcept").hide()
    $("#sku").attr("readonly", true)
    $("#unitType").attr("readonly", true)
  }
});
