$("#perceptions").hide();
$("#deductions").hide();
$("#extraHours").hide();

$("#incidenceType").change( function() {
  $("#incidence").val("");
  $("#extraHours").hide();
  $("#perceptions").val("");
  $("#deductions").val("");
  $("#exemptAmount").attr("readOnly", false);
  if (this.value == '') {
    $("#perceptions").hide();
    $("#deductions").hide();
  } else if (this.value == "Deducción") {
    $("#perceptions").hide();
    $("#deductions").show();
    $("#perceptions").removeAttr("required");
    $("#deductions").attr("required", true);
    $("#exemptAmount").attr("readOnly", true);
  } else {
    $("#perceptions").show();
    $("#deductions").hide(); 
    $("#deductions").removeAttr("required");
    $("#perceptions").attr("required", true);
  }
});

function setRequiredToExtraHours() {
  $("#extraHoursDays").attr("required", true);
  $("#extraHoursType").attr("required", true);
  $("#extraHoursQuantity").attr("required", true);
}

function quiteRequiredToExtraHours() {
  $("#extraHoursDays").removeAttr("required");
  $("#extraHoursType").removeAttr("required");
  $("#extraHoursQuantity").removeAttr("required");
}

$("#perceptions").change( function() {
  $("#incidence").val(this.value);
  if (this.value == '019 - Horas extra') {
    $("#extraHours").show();
    setRequiredToExtraHours();
  } else {
    $("#extraHours").hide();
    quiteRequiredToExtraHours();
  }
});

$("#deductions").change( function() {
  $("#incidence").val(this.value);
});
