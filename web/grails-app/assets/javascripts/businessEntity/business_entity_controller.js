var BusinessEntityController = (function(){

  var regimenType = '#regimenBusiness',
      entityRadio = 'input[name="clientProviderType"]',
      regimenSelected = '#type option[value="MORAL"]'

  var start = function(option) {
    newProviderByInvoice();
    disabledEntities();
  };

  var newProviderByInvoice = function() {
    var type = $(regimenType).val();
    if (type != "") {
      $(entityRadio).filter('[value="PROVEEDOR"]').attr('checked', true);
    }
    if (type == "MORAL") {
      $(regimenSelected).prop('selected', true);
      $(regimenSelected).change();
    }
  };

  var disabledEntities = function() {
    var entity = $('input[name="clientProviderType"]:checked').val();
    var existing = $('#id').val();
    if (typeof existing === "undefined") {
      return
    }

      if (entity=='EMPLEADO') {
        $('input[name="clientProviderType"][value=CLIENTE]').attr("disabled",true);
        $('input[name="clientProviderType"][value=PROVEEDOR]').attr("disabled",true);
        $('input[name="clientProviderType"][value=CLIENTE_PROVEEDOR]').attr("disabled",true);
      } else {
        $('input[name="clientProviderType"][value=CLIENTE]').attr("disabled",false);
        $('input[name="clientProviderType"][value=PROVEEDOR]').attr("disabled",false);
        $('input[name="clientProviderType"][value=CLIENTE_PROVEEDOR]').attr("disabled",false);
        $('input[name="clientProviderType"][value=EMPLEADO]').attr("disabled",true);
      }
  };

  return {
    start:start
  };

}());
