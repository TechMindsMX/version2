$("#add").click ( function() {
  if ($("#entities").val() == "") {
    alert("No ha seleccionado empleados");
    return
  }

  $("#addEmployees").submit();
});

$("#addUs").click ( function() {
  if ($("#entities").val() == "") {
    alert("No ha seleccionado usuarios");
    return
  }

  $("#addUsers").submit();
});

