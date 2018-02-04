function calculatePriceWithDiscount() {
  return $("#price").val() - $("#price").val()*($("#discount").val()/100)
}

function calculateAmountAndNetPrice(){
  $("#amount").val("0")
  if (isNaN($("#quantity").val()) || isNaN($("#price").val()) || isNaN($("#discount").val()) || isNaN($("#ivaRetention").val()) || isNaN($("#iva").val())){
    $("#amount").val("No válido")
    $("#netprice").val("No válido")
    return
  }

  $("#netprice").val((calculatePriceWithDiscount()*(1 + $("#iva").val()/100.00 + $("#discount").val()/100.00)).toFixed(2))
  $("#amount").val(($("#quantity").val()*$("#netprice").val()).toFixed(2))
}

$("#price").change( function() {
    calculateAmountAndNetPrice()
  }
)

$("#discount").change( function() {
    calculateAmountAndNetPrice()
  }
)

$("#iva").change( function() {
    calculateAmountAndNetPrice()
  }
)

$("#ivaRetention").change( function() {
   calculateAmountAndNetPrice()
 }
)

$("#quantity").change( function() {
   calculateAmountAndNetPrice()
  }
)
