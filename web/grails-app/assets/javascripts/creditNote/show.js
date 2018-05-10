$('#name').on('change',function(){
  $('#sku').val('');
  $('#quantity').val('');
  $('#price').val('');
  $('#originalPrice').val('');
  $('#discount').val('');
  $('#iva').val('');
  $('#ivaRetention').val('');
  $('#unitType').val('');
  var valor = this.value;
  var consulta = $.ajax({
    type:'GET',
    url:'/saleOrderItem/getSaleOrderItem',
    data:{itemId:valor},
    dataType:'JSON'
  });

  consulta.done(function(data){
    if(data.error!==undefined){
      return false;
    } else {
      if(data.sku!==undefined){$('#sku').val(data.sku);}
      if(data.quantity!==undefined){$('#quantity').val(data.quantity.toFixed(2));$('#originalQuantity').val(data.quantity.toFixed(2));}
      if(data.price!==undefined){$('#price').val(data.price.toFixed(2));$('#originalPrice').val(data.price.toFixed(2));}
      if(data.discount!==undefined){$('#discount').val(data.discount.toFixed(2));}
      if(data.iva!==undefined){$('#iva').val(data.iva.toFixed(2));}
      if(data.ivaRetention!==undefined){$('#ivaRetention').val(data.ivaRetention.toFixed(2));}
      if(data.unitType!==undefined){$('#unitType').val(data.unitType);}
      calculateAmountAndNetPrice()
      setOriginalNetPriceAndOriginalAmount()
      return true;
    }
  });

  consulta.fail(function(){
    return false;
  });
});

function calculatePriceWithDiscount() {
  return $("#price").val() - $("#price").val()*($("#discount").val()/100)
}

function calculateAmountAndNetPrice(){
  $("#amount").val("0.00")
  $("#netprice").val("0.00")
  if (isNaN($("#quantity").val()) || isNaN($("#price").val()) || isNaN($("#discount").val()) || isNaN($("#ivaRetention").val()) || isNaN($("#iva").val())){
    return
  }

  if (priceIsGreaterThanOriginalPrice()) {
    $("#price").focus()
    alert("El precio ingresado es mayor al precio original")
    return
  }

  if (quantityIsGreaterThanOriginalQuantity()) {
    $("#quantity").focus()
    alert("La cantidad ingresada es mayor a la cantidad original")
    return
  }

  var netPrice = (calculatePriceWithDiscount()*(1 + $("#iva").val()/100.00) - $("#ivaRetention").val()).toFixed(2)
  var amount = ($("#quantity").val()*netPrice).toFixed(2)

  $("#netprice").val(netPrice)
  $("#amount").val(amount)
}

function priceIsGreaterThanOriginalPrice() {
  return ($("#price").val()*1) > ($("#originalPrice").val()*1)
}

function quantityIsGreaterThanOriginalQuantity() {
  return ($("#quantity").val()*1) > ($("#originalQuantity").val()*1)
}

function setOriginalNetPriceAndOriginalAmount() {
  $("#originalNetprice").val($("#netprice").val())
  $("#originalAmount").val($("#amount").val())
}

$("#price").change( function() {
    calculateAmountAndNetPrice()
    verifyAmount()
  }
)

$("#discount").change( function() {
    calculateAmountAndNetPrice()
    verifyAmount()
  }
)

$("#ivaRetention").change( function() {
    calculateAmountAndNetPrice()
    verifyAmount()
 }
)

$("#iva").change( function() {
    calculateAmountAndNetPrice()
    verifyAmount()
  }
)

$("#quantity").change( function() {
    calculateAmountAndNetPrice()
    verifyAmount()
  }
)

function verifyAmount() {
  if (($("#amount").val()*1) > ($("#originalAmount").val()*1)) {
    $("#amount").val("0.00")
    alert("El importe del concepto no puede ser mayor al importe original ("+$("#originalAmount").val()+")")
  }
}

$("form").on("submit", function() {
  if ($("#amount").val() == "0.00") {
    alert("El importe del concepto no puede ser cero, verifique los datos")
    return false
  }
  return true
} )
