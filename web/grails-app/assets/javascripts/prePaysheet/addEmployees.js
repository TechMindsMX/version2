$("#add").click ( function() {
  if ($("#entities").val() == "") {
    alert("No ha seleccionado empleados");
    return
  }

  if (validateDataForEachSelected()) {
    $("form#formAddEmployees").submit();
  }

});

function validateDataForEachSelected() {
  var validate = true
  $("input[name='checkBe']").each ( function(index) {
    if ($(this).prop("checked")) {
      var netPayment = "#netPayment"+$(this).val();
      if ($(netPayment).val() == "") {
        alert("Falta por registrar el total a pagar de algunos empleados");
        validate = false
        return
      }
      if (isNaN($(netPayment).val())) {
        alert("Algunos totales ingresados no son válidos");
        validate = false
        return
      }
    }
  });
  return validate
}
