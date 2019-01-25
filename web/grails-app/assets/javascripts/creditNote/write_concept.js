$("#selectWriteConcept").click( function() {
  var select = document.getElementById("name");
  var input = document.getElementById("selectName");
  if ($("#selectWriteConcept").is(":checked")) {
    $("#writeConcept").show()
    $("#selectConcept").hide()
    $("#sku").attr("readonly", false)
    $("#unitType").attr("readonly", false)
    select.id="selectName"
    select.name ="selectName"
    input.id = "name"
    input.name = "name"
  } else {
    $("#selectConcept").show()
    $("#writeConcept").hide()
    $("#sku").attr("readonly", true)
    $("#unitType").attr("readonly", true)
    select.id="selectName"
    select.name ="selectName"
    input.id = "name"
    input.name = "name"
  }
});

$("form").on("submit", function() {
  if ($("#name").val() == "") {
    alert("No ha llenado el concepto")
    return false
  }
  if ($("#amount").val() == "0.00") {
    alert("El importe del concepto no puede ser cero, verifique los datos")
    return false
  }
  if ($("#price").val() == "") {
    alert("No ha llenado el cantidad")
    return false
  }

  if ($("#unitType").val() == "") {
    alert("No ha llenado la medida")
    return false
  }
  return true
} )


$("#btnSubmit").click( function(){
  if ($("#selectWriteConcept").is(":checked")) {
    $("#selectConcept").remove()
  }
})