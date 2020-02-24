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
                  <th class="col-xs-2">Fecha de factura original</th>
                  <th class="col-xs-1">Serie</th>
                  <th class="col-xs-2">Número de factura</th>
                  <th class="col-xs-1">Moneda</th>
                  <th class="col-xs-1">Monto</th>
                  <th class="col-xs-1">IVA</th>
                  <th class="col-xs-1">Total</th>
                  <th class="col-xs-1"></th>
                </tr>
              </thead>
              <tbody>
                <g:each in="${canceledSaleOrders}" var="sale">
                  <tr>
                    <td>${sale.clientName}</td>
                    <td>${sale.rfc}</td>
                    <td><g:formatDate format="dd-MM-yyyy" date="${sale.dateCreated}"/></td>
                    <td>${sale.invoiceSerie}</td>
                    <td>${sale.invoiceFolio}</td>
                    <td>${sale.currency}</td>
                    <td>${modulusuno.formatPrice(number: sale.subtotal)}</td>
                    <td>${modulusuno.formatPrice(number: sale.totalIVA)}</td>
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
