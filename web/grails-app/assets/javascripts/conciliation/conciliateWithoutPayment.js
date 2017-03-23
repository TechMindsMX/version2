$("#changeType").change( function() {
  if ($("#currency").val() == 'USD' && !(isNaN($("#changeType").val()))) {
    var amountToPay = ($("#amountToPay").val() * 1).toFixed(2)
    var amountMaxMXN = (amountToPay * $("#changeType").val()).toFixed(2)
    $("#amountToApply").attr('max', amountMaxMXN)
  }
})
