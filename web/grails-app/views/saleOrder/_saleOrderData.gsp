<%! import com.modulus.uno.status.SaleOrderStatus %>

<div class="portlet portlet-default">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4>Datos de orden de venta</h4>
    </div>
    <div class="clearfix"></div>
  </div>
  <div id="defaultPortlet" class="panel-collapse collapse in">
    <div class="portlet-body">
			<div class="row">
        <div class="col-md-6">
          <g:render template="summaryData"/>
          <g:if test="${saleOrder.status == SaleOrderStatus.CREADA}">
            <g:if test="${canceledSaleOrders}">
              <g:if test="${!saleOrder.uuidReplacement}"
                <div class="form-group">
                  <h4>
                    <input type="checkbox" id="replacementInvoice" name="replacementInvoice" value="" />&nbsp;&nbsp;<label>Marcar como factura de reposición</label>
                  </h4>
                </div>
              </g:if>
            </g:if>
          </g:if>
          <g:if test="${saleOrder.uuidReplacement}">
            <h4>
              <label><b>Factura de reposición</b></label>
            </h4>
          </g:if>
        </div>
        <div class="col-md-6">
        <g:if test="${saleOrder.status == SaleOrderStatus.EJECUTADA || saleOrder.status == SaleOrderStatus.PAGADA}">
          <h4>Pagos registrados</h4>
          <div class="table-responsive">
            <table class="table">
              <thead>
                <tr>
                  <th>Fecha</th>
                  <th>Monto</th>
                </tr>
              </thead>
              <tbody>
                <g:each in="${saleOrder.payments.sort{it.dateCreated}}" var="payment">
                  <tr>
                    <td><g:formatDate format="dd-MM-yyyy" date="${payment.dateCreated}"/></td>
                    <td>${modulusuno.formatPrice(number:payment.amount)}</td>
                  </tr>
                </g:each>
              </tbody>
            </table>
          </div>
        </g:if>
        </div>
      </div>
      <div class="row">
        <div class="col-md-12">
          <g:if test="${saleOrder.status == SaleOrderStatus.CANCELADA || saleOrder.status == SaleOrderStatus.RECHAZADA}">
            <div class="alert alert-danger" role="alert">
              <label class="control-label">Motivo ${message(code:'saleOrder.rejectReason.'+saleOrder.status)}:</label>
              <g:message code="rejectReason.${saleOrder.rejectReason}" default="${saleOrder.rejectReason}"/>
              <textarea class="form-control" rows="3" cols="60" readonly>${saleOrder.comments}</textarea>
            </div>
          </g:if>
        </div>
      </div>
      <!-- actions section -->
      <div class="row">
        <g:render template="saleOrderActions"/>
      </div>
      <!-- end actions section -->
    </div>
  </div>
</div>
