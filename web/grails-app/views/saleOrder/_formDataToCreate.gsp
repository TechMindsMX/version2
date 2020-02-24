<%! import com.modulus.uno.PaymentWay %>
<%! import com.modulus.uno.PaymentMethod %>
<%! import com.modulus.uno.InvoicePurpose %>
<%! import com.modulus.uno.AddressType %>

<div class="portlet portlet-default">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4>${client}</h4>
    </div>
    <div class="clearfix"></div>
  </div>
  <div id="defaultPortlet" class="panel-collapse collapse in">
    <div class="portlet-body">
      <p><strong>${client.rfc}</strong></p>
      <p>
      <g:if test="${client.addresses.find {it.addressType == AddressType.FISCAL}}">
      Direccion de facturación:
      <input type="hidden" name="addressId" value="${(client.addresses.find {it.addressType == AddressType.FISCAL}).id}"/>
      <p>${client.addresses.find {it.addressType == AddressType.FISCAL}}</p>
      <input type="hidden" name="companyId" value="${company.id}" />
      <input type="hidden" name="clientId" value="${client.id}" />
      </p>
      <div class="form-group">
        <input type="checkbox" id="currencyUsd" name="currencyUsd" value="USD" />&nbsp;&nbsp;<label>Factura en Dólares</label>
      </div>
      <div id="changeTypeSection" class="form-group" hidden>
        <label>Tipo de Cambio:</label>
        <input type="text" id="changeType" name="changeType" class="form-control" pattern="[0-9]+(\.[0-9]{1,2})?" title="Ingrese una cantidad en formato correcto (número sin decimales o con 2 decimales exactamente)"/>
      </div>
      <div class="">
        <label>Fecha de Cobro:</label>
        <input type="text" id="datepicker" name="fechaCobro" required="required" autocomplete="off" autocomplete="off">
      </div>
      <br />
      <div class="form-group">
        <label>Notas:</label>
        <textarea class="form-control" rows="4" cols="50" name="note" id="note" ></textarea>
      </div>
      <br/>
      <div class="form-group">
        <label>Forma de Pago:</label>
        <g:select class="form-control" name="paymentWay" from="${PaymentWay.values()}"/>
      </div>
      <div class="form-group">
        <label>Método de Pago:</label>
        <g:select class="form-control" name="paymentMethod" from="${PaymentMethod.values()}"/>
      </div>
      <div class="form-group">
        <label>Uso del CFDI:</label>
        <g:select class="form-control" name="invoicePurpose" from="${InvoicePurpose.values()}" value="${InvoicePurpose.G03}"/>
      </div>

      <p>
     <p class="text-right">
      <button type="submit" class="btn btn-primary">
        Confirmar cliente/cuenta y agregar productos/servicios
      </button>
      </p>
      </g:if>
      <g:else>
          <p>El cliente seleccionado no tiene dirección Fiscal registrada</p>
          <g:link controller="businessEntity" action="show" id="${client.id}" class="btn btn-green btn-block">
              Registrar Dirección
          </g:link>
      </g:else>
    </div>
  </div>
</div>
