$("#perceptions").hide();
$("#deductions").hide();
$("#others").hide();
$("#extraHours").hide();

$("#incidenceType").change( function() {
  $("#incidence").val("");
  $("#extraHours").hide();
  $("#perceptions").val("");
  $("#deductions").val("");
  $("#others").val("");
  $("#exemptAmount").attr("readOnly", false);
  if (this.value == '') {
    $("#perceptions").hide();
    $("#deductions").hide();
    $("#others").hide();
  } else if (this.value == "Deducción") {
    $("#perceptions").hide();
    $("#others").hide();
    $("#deductions").show();
    $("#perceptions").removeAttr("required");
    $("#others").removeAttr("required");
    $("#deductions").attr("required", true);
    $("#exemptAmount").attr("readOnly", true);
  } else if (this.value == "Percepción") {
    $("#perceptions").show();
    $("#deductions").hide(); 
    $("#others").hide(); 
    $("#deductions").removeAttr("required");
    $("#others").removeAttr("required");
    $("#perceptions").attr("required", true);
  } else {
    $("#others").show();
    $("#deductions").hide(); 
    $("#perceptions").hide(); 
    $("#deductions").removeAttr("required");
    $("#perceptions").removeAttr("required");
    $("#others").attr("required", true); 
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
