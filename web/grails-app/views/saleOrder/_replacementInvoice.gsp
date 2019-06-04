<%! import com.modulus.uno.status.SaleOrderStatus%>
<div class="portlet portlet- default">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4>Sustitución de factura</h4>
    </div>
    <div class="clearfix"></div>
  </div>
  <div class="defaultPortlet" class="panel-collapse collapse in">
    <div class="portlet-body">
      <div class="row">
        <div class="table-responsive">
          <div class="container-fluid">
            <table class="table table-condensed table-striped">
              <thead>
                <tr>
                  <th class="col-xs-2">Nombre del Cliente</th>
                  <th class="col-xs-1">RFC</th>
                  <th class="col-xs-1">No. Factura</th>
                  <th class="col-xs-3">Folio</th>
                  <th class="col-xs-2">UUID</th>
                  <th class="col-xs-1">Importe</th>
                  <th class="col-xs-1"></th>
                </tr>
              </thead>
              <tbody>
                <g:each in="${canceledSaleOrders}" var="sale">
                  <tr>
                    <td>${sale.clientName}</td>
                    <td>${sale.rfc}</td>
                    <td>${sale.id}</td>
                    <td>${sale.folio}</td>
                    <td>${sale.uuid}</td>
                    <td>${modulusuno.formatPrice(number: sale.total)}</td>
                    <td><g:link class="btn btn-default" action="getReplacementInvoiceUUID" params="[uuid: sale.folio]" id="${saleOrder.id}">Seleccionar</g:link></td>
                  </tr>
                </g:each>  
              </tbody>
            </table>
          </div>    
        </div>  
      </div>
    </div>
  </div>
</div>