$("#perceptions").hide();
$("#deductions").hide();

$("#incidenceType").change( function() {
  $("#incidence").val("");
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

$("#perceptions").change( function() {
  $("#incidence").val(this.value);
});

$("#deductions").change( function() {
  $("#incidence").val(this.value);
});
