$("#perceptions").hide();
$("#deductions").hide();
$("#extraHours").hide();

$("#incidenceType").change( function() {
  $("#incidence").val("");
  $("#extraHours").hide();
  $("#perceptions").val("");
  $("#deductions").val("");
  if (this.value == '') {
    $("#perceptions").hide();
    $("#deductions").hide();
  } else if (this.value == "Deducción") {
    $("#perceptions").hide();
    $("#deductions").show();
    $("#perceptions").removeAttr("required");
    $("#deductions").attr("required", true);
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
  $("#extraHoursAmount").attr("required", true);
}

function quiteRequiredToExtraHours() {
  $("#extraHoursDays").removeAttr("required");
  $("#extraHoursType").removeAttr("required");
  $("#extraHoursQuantity").removeAttr("required");
  $("#extraHoursAmount").removeAttr("required"); 
}

function hideAmounts() {
  $("#exemptAmount").hide();
  $("#taxedAmount").hide();
  $("#exemptAmount").removeAttr("required");
  $("#taxedAmount").removeAttr("required");
}

function showAmounts() {
  $("#exemptAmount").show();
  $("#taxedAmount").show();
  $("#exemptAmount").attr("required", true);
  $("#taxedAmount").attr("required", true);
}

$("#perceptions").change( function() {
  $("#incidence").val(this.value);
  if (this.value == '019 - Horas extra') {
    $("#extraHours").show();
    hideAmounts();
    setRequiredToExtraHours();
  } else {
    $("#extraHours").hide();
    showAmounts();
    quiteRequiredToExtraHours();
  }
});

$("#deductions").change( function() {
  $("#incidence").val(this.value);
});
