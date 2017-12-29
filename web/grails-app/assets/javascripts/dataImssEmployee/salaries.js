$("#saSalary").on('blur',function(){
  calculateCrudeSalary();
});

$("#iasSalary").on('blur',function(){
  calculateCrudeSalary();
});

$("#crudeSalary").on('blur',function(){
  calculateIasSalary();
});

function calculateCrudeSalary() {
  $('#crudeSalary').val('');
  var sa = $("#saSalary").val()*1;
  var ias = $("#iasSalary").val()*1;
  if (sa==="" || ias==="" || isNaN(sa) || isNaN(ias)) {
      return
  }
  var crude = sa + ias;
  $('#crudeSalary').val((crude).toFixed(2));
}

function calculateIasSalary() {
  $('#iasSalary').val('');
  var sa = $("#saSalary").val();
  var crude = $("#crudeSalary").val();
  if (sa==="" || crude==="" || isNaN(sa) || isNaN(crude)){
      return
  }

  $('#iasSalary').val((crude-sa).toFixed(2));
}
