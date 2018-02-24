$( function() {
  $( "#dpApplyDate" ).datepicker({
    dateFormat: 'dd/mm/yy',
    minDate: 0
  });
  verifyBankNameToDispersionFileResult();
});

function verifyBankNameToDispersionFileResult() {
  $("#resultFileTypeDiv").removeAttr('required');
  $("#resultFileTypeDiv").hide();
  if ($("#bank option:selected").text() == "BANAMEX") {
    $("#resultFileTypeDiv").prop('required',true);
    $("#resultFileTypeDiv").show();
  }
}

$( "#bank" ).change(function() {
  verifyBankNameToDispersionFileResult();
});

