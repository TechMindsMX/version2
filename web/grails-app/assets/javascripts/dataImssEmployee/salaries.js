processSchema();
calculateTotalSalary();

$("input[name='paysheetSchema']").change( function (){
  processSchema();
});

function processSchema() {
  var schema = $("input[name='paysheetSchema']:checked").val();
  switch(schema) {
    case 'SA_IAS': saAndIas(); break;
    case 'SA': onlySa(); break;
    case 'IAS_FIJO': case 'IAS_VARIABLE': onlyIas(); break;
  }
}

function saAndIas() {
  $("#saSalary").removeAttr("disabled");
  $("#iasSalary").removeAttr("disabled");
  $("#nss").removeAttr("disabled");
  $("#dpRegistration").removeAttr("disabled");
  $("#dpDischarge").removeAttr("disabled");
  $("#holidayBonusRate").removeAttr("disabled");
  $("#annualBonusDays").removeAttr("disabled");
  $("#jobRisk").removeAttr("disabled");
  $("#department").removeAttr("disabled");
  $("#job").removeAttr("disabled");
  $("#workDayType").removeAttr("disabled");
  calculateTotalSalary();
}

function onlySa() {
  $("#saSalary").removeAttr("disabled");
  $("#nss").removeAttr("disabled");
  $("#dpRegistration").removeAttr("disabled");
  $("#dpDischarge").removeAttr("disabled");
  $("#holidayBonusRate").removeAttr("disabled");
  $("#annualBonusDays").removeAttr("disabled");
  $("#iasSalary").attr("disabled", "true");
  $("#iasSalary").val("");
  $("#jobRisk").removeAttr("disabled");
  $("#department").removeAttr("disabled");
  $("#job").removeAttr("disabled");
  $("#workDayType").removeAttr("disabled");
  calculateTotalSalary();
}

function onlyIas() {
  $("#iasSalary").removeAttr("disabled");
  $("#saSalary").attr("disabled", "true");
  $("#saSalary").val("");
  $("#nss").attr("disabled", "true");
  $("#nss").val("");
  $("#dpRegistration").attr("disabled", "true");
  $("#dpRegistration").val("");
  $("#dpDischarge").attr("disabled", "true");
  $("#dpDischarge").val("");
  $("#holidayBonusRate").attr("disabled", "true");
  $("#holidayBonusRate").val("");
  $("#annualBonusDays").attr("disabled", "true");
  $("#annualBonusDays").val("");
  $("#jobRisk").attr("disabled", "true");
  $("#department").attr("disabled", "true");
  $("#department").val("");
  $("#job").attr("disabled", "true");
  $("#job").val("");
  $("#workDayType").attr("disabled", "true");
  calculateTotalSalary();
}

$("#saSalary").on('blur',function(){
  calculateTotalSalary();
});

$("#iasSalary").on('blur',function(){
  calculateTotalSalary();
});

function calculateTotalSalary() {
  $('#totalSalary').val('');
  var sa = $("#saSalary").val()*1;
  var ias = $("#iasSalary").val()*1;
  if (sa==="" || ias==="" || isNaN(sa) || isNaN(ias)) {
      return
  }
  var total = sa + ias;
  $('#totalCrudeSalary').val((total).toFixed(2));
}

function calculateIasSalary() {
  $('#iasSalary').val('');
  var sa = $("#saSalary").val();
  var total = $("#totalSalary").val();
  if (sa==="" || total==="" || isNaN(sa) || isNaN(total)){
      return
  }

  $('#iasSalary').val((total-sa).toFixed(2));
}

$( function() {
  $( "#dpRegistration" ).datepicker({
    dateFormat: 'dd/mm/yy',
    maxDate: 0
  });
} );

$( function() {
  $( "#dpDischarge" ).datepicker({
    dateFormat: 'dd/mm/yy',
    maxDate: 0
  });
} );

