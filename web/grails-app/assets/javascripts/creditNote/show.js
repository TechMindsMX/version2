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
      if(data.quantity!==undefined){$('#quantity').val(data.quantity.toFixed(2));}
      if(data.price!==undefined){$('#price').val(data.price.toFixed(2));$('#originalPrice').val(data.price.toFixed(2));}
      if(data.discount!==undefined){$('#discount').val(data.discount.toFixed(2));}
      if(data.iva!==undefined){$('#iva').val(data.iva.toFixed(2));}
      if(data.ivaRetention!==undefined){$('#ivaRetention').val(data.ivaRetention.toFixed(2));}
      if(data.unitType!==undefined){$('#unitType').val(data.unitType);}
      calculateAmountAndNetPrice()
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
  $("#amount").val("0")
  if (isNaN($("#quantity").val()) || isNaN($("#price").val()) || isNaN($("#discount").val()) || isNaN($("#ivaRetention").val()) || isNaN($("#iva").val())){
    $("#amount").val("No válido")
    $("#netprice").val("No válido")
    return
  }

  if (priceIsGreaterThanOriginalPrice()) {
    $("#amount").val("No válido")
    $("#netprice").val("No válido")
    $("#price").focus()
    alert("El precio ingresado es mayor al precio original")
    return
  }

  $("#netprice").val((calculatePriceWithDiscount()*(1 + $("#iva").val()/100.00) - $("#ivaRetention").val()).toFixed(2))
  $("#amount").val(($("#quantity").val()*$("#netprice").val()).toFixed(2))
}

function priceIsGreaterThanOriginalPrice() {
  return ($("#price").val()*1) > ($("#originalPrice").val()*1)
}

$("#price").change( function() {
   calculateAmountAndNetPrice()
  }
)

$("#discount").change( function() {
    calculateAmountAndNetPrice()
  }
)

$("#ivaRetention").change( function() {
   calculateAmountAndNetPrice()
 }
)

$("#iva").change( function() {
   calculateAmountAndNetPrice()
  }
)

$("#quantity").change( function() {
   calculateAmountAndNetPrice()
  }
)

