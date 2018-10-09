$("#saleOrderId").change(function() {
  getCurrencyForSaleOrder($("#saleOrderId option:selected").val());
});

function getCurrencyForSaleOrder(saleOrderId) {
  var consulta = $.ajax({
    type:'POST',
    url:'../../SaleOrder/getCurrencyOfSaleOrder',
    data:{saleOrderId: saleOrderId},
    datatype:'JSON'
  });

  consulta.done(function(data){
    if(data.error!==undefined){
      return false;
    } else {
      if(data.currency!==undefined){
        if (data.currency=='USD'){
          $("#changeType").attr('readOnly',false);
          $("#changeType").val('');
          $("#changeType").focus();
        } else {
          $("#changeType").attr('readOnly',true);
          $("#changeType").val('0.00');
        }

      }
      return true;
    }
  });

  consulta.fail(function(){
    return false;
  });
};

$("#chkPaymentComplement").change(function(){
  if (this.checked) {
    $("#btnApply").hide();
    $("#collapsePaymentComplement").collapse('show');
  } else {
    $("#btnApply").show();
    $("#collapsePaymentComplement").collapse('hide');
  }
});
