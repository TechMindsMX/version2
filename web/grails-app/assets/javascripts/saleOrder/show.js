var options = {
  url: function(phrase) {
    var companyId = $("input[name='saleOrder.id']").val();
    return "../listProducts?pname=" + phrase + "&format=json";
  },

  getValue: "name",

  list: {
    sort: {
      enabled: true
    },
    onSelectItemEvent: function() {
      $('#sku').val('');
      $('#price').val('');
      $('#unit').val('');
      /* Obtenemos el valor del campo */
      var valor = $('#product-name').getItemData($('#product-name').getSelectedItemIndex()).name;
      /* Si la longitud del valor es mayor a 2 caracteres.. */
      if(valor.length>=3){
        /* Hacemos la consulta ajax */
        var consulta = $.ajax({
          type:'POST',
          url:'../getProduct',
          data:{nombre:valor, companyid:$("input[name='saleOrder.id']").val()},
          datatype:'JSON'
        });

        /* En caso de que se haya retornado bien.. */
        consulta.done(function(data){
          if(data.error!==undefined){
            return false;
          } else {
            if(data.sku!==undefined){$('#sku').val(data.sku);}
            if(data.price!==undefined){$('#price').val(calculatePriceWithCurrency(data.price, data.currency));}//data.price.toFixed(2)
            if(data.iva!==undefined){$('#iva').val(data.iva.toFixed(2));}
            if(data.unit!==undefined){$('#unit').val(data.unit).prop('selected',true);}
            if(data.satKey!==undefined){$('#satKey').val(data.satKey);}
            calculateAmountAndNetPrice()
            return true;
          }
        });

        /* Si la consulta ha fallado.. */
        consulta.fail(function(){
          return false;
        });
      } else {
        return false;
      }
    }
  }
};

var optionsBySku = {
  url: function(phrase) {
    var companyId = $("input[name='saleOrder.id']").val();
    return "../listProducts?psku=" + phrase + "&format=json";
  },

  getValue: "sku",

  list: {
    sort: {
      enabled: true
    },
    onSelectItemEvent: function() {
      $('#product-name').val('');
      $('#price').val('');
      $('#unit').val('');
      /* Obtenemos el valor del campo */
      var valor = $('#sku').val();
      /* Si la longitud del valor es mayor a 1 caracteres.. */
      if(valor.length>=1){
        /* Hacemos la consulta ajax */
        var consulta = $.ajax({
          type:'POST',
          url:'../getProductBySku',
          data:{sku:valor, companyid:$("input[name='saleOrder.id']").val()},
          datatype:'JSON'
        });

        /* En caso de que se haya retornado bien.. */
        consulta.done(function(data){
          if(data.error!==undefined){
            return false;
          } else {
            if(data.productName!==undefined){$('#product-name').val(data.productName);}
            if(data.price!==undefined){$('#price').val(calculatePriceWithCurrency(data.price, data.currency));}
            if(data.iva!==undefined){$('#iva').val(data.iva.toFixed(2));}
            if(data.unit!==undefined){$('#unit').val(data.unit).prop('selected',true);}
            if(data.satKey!==undefined){$('#satKey').val(data.satKey);}
            calculateAmountAndNetPrice()
              return true;
          }
        });
        /* Si la consulta ha fallado.. */
        consulta.fail(function(){
          return false;
        });
      } else {
        return false;
      }
    }
  }
};

$("#product-name").easyAutocomplete(options);
$("#sku").easyAutocomplete(optionsBySku);
$("#satKey").easyAutocomplete(optionsBySku);

//actualizar SKU, PRECIO Y UNIDAD
/* Ponemos evento blur a la escucha sobre id nombre en id cliente. */
$('#products').on('blur',function(){
  $('#sku').val('');
  $('#price').val('');
  $('#unit').val('');
  /* Obtenemos el valor del campo */
  var valor = this.value;
  /* Si la longitud del valor es mayor a 2 caracteres.. */
  if(valor.length>=3){
    /* Hacemos la consulta ajax */
    var consulta = $.ajax({
      type:'POST',
      url:'../getProduct',
      data:{nombre:valor},
      dataType:'JSON'
    });

    /* En caso de que se haya retornado bien.. */
    consulta.done(function(data){
      if(data.error!==undefined){
        return false;
      } else {
        if(data.sku!==undefined){$('#sku').val(data.sku);}
        if(data.price!==undefined){$('#price').val(calculatePriceWithCurrency(data.price, data.currency));}
        if(data.iva!==undefined){$('#iva').val(data.iva.toFixed(2));}
        if(data.unit!==undefined){$('#unit').val(data.unit).prop('selected',true);}
        calculateAmountAndNetPrice()
        return true;
      }
    });

    /* Si la consulta ha fallado.. */
    consulta.fail(function(){
      return false;
    });
  } else {
    return false;
  }
});

function calculatePriceWithCurrency(prodPrice, prodCurrency) {
  if (($("#saleCurrency").val()=='MXN' && prodCurrency=='PESOS')
     || ($("#saleCurrency").val()=='USD' && prodCurrency=='USD')) {
    return prodPrice
  } else {
    return 0
  }
}

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

  $("#netprice").val((calculatePriceWithDiscount()*(1 + $("#iva").val()/100.00) - $("#ivaRetention").val()).toFixed(2))
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

$("#btnPreview").click( function() {
    $("#executeSale").attr("action","/saleOrder/previewInvoicePdf/");
    $("#executeSale").submit();
  }
)

$("#btnExecute").click( function() {
    $("#executeSale").attr("action","/saleOrder/executeSaleOrder")
    $("#executeSale").submit()
  }
)

$('#product-name').keypress(function (e) {
    var regex = new RegExp("^[a-zA-Z0-9 ñÑ\s\+\-.#$%*();:]$")
    var str = String.fromCharCode(!e.charCode ? e.which : e.charCode)
    if (regex.test(str)) {
      return true
    }

    e.preventDefault()
    return false
  }
)

