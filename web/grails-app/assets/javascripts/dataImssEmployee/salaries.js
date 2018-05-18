$("#saSalary").on('blur',function(){
  calculateTotalSalary();
});

$("#iasSalary").on('blur',function(){
  calculateTotalSalary();
});

$("#totalSalary").on('blur',function(){
  calculateIasSalary();
});

function calculateTotalSalary() {
  $('#totalSalary').val('');
  var sa = $("#saSalary").val()*1;
  var ias = $("#iasSalary").val()*1;
  if (sa==="" || ias==="" || isNaN(sa) || isNaN(ias)) {
      return
  }
  var total = sa + ias;
  $('#totalSalary').val((total).toFixed(2));
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

