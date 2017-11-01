$("#saSalary").on('blur',function(){
  calculateNetSalary();
});

$("#iasSalary").on('blur',function(){
  calculateNetSalary();
});

$("#netSalary").on('blur',function(){
  calculateIasSalary();
});

function calculateNetSalary() {
  $('#netSalary').val('');
  var sa = $("#saSalary").val()*1;
  var ias = $("#iasSalary").val()*1;
  if (sa=="" || ias=="" || isNaN(sa) || isNaN(ias)) {
      return
  }
  var net = sa + ias;
  $('#netSalary').val((sa+ias).toFixed(2));
}

function calculateIasSalary() {
  $('#iasSalary').val('');
  var sa = $("#saSalary").val();
  var net = $("#netSalary").val();
  if (sa==="" || net==="" || isNaN(sa) || isNaN(net)){
      return
  }

  $('#iasSalary').val((net-sa).toFixed(2));
}
