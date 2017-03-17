<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <title></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-usd fa-3x"></i>
        Conciliación de Pago
        <small>Elegir factura(s)</small>
      </h1>
    </div>

    <div class="row">
      <div class="col-lg-12">
        <div class="panel panel-primary">
          <div class="panel-body">
            <div class="container-fluid">

              <div class="row">
                <div class="col-md-4 text-center">
                  <strong>Fecha del Pago</strong><br>
                  <g:formatDate format="dd-MM-yyyy" date="${payment.dateCreated}"/>
                </div>
                <div class="col-md-4 text-center">
                  <strong>Total</strong>
                  <br/>${modulusuno.formatPrice(number: payment.amount)}
                </div>
                <g:if test="${toApply == 0}">
                <div class="col-md-4 text-center alert alert-success">
                </g:if>
                <g:else>
                <div class="col-md-4 text-center alert alert-warning">
                </g:else>
                  <strong>Por aplicar</strong><br/>
                  ${modulusuno.formatPrice(number: toApply)}
                </div>
              </div>
              <hr>
              <g:if test="${toApply > 0}">

              <g:if test="${flash.message}">
                <div class="alert alert-danger" role="alert">${flash.message}</div>
              </g:if>

              <div class="row">
                <g:form action="addSaleOrderToConciliate">
                <g:hiddenField name="paymentId" value="${payment.id}"/>
                <div class="col-md-12">
                  <label>Facturas disponibles:</label>
                  <g:select class="form-control" name="saleOrderId" from="${saleOrders}" noSelection="['':' Elegir factura...']" required="true" optionKey="id"/>
                </div>
              </div><br/>
              <div class="row">
                <div class="col-md-4"></div>
                <div class="col-md-3">
                  <label>Tipo de Cambio:</label>
                  <input class="form-control" type="number" min="0.01" step="0.01" name="changeType" readOnly="true" value="0.00"/>
                </div>
                <div class="col-md-3">
                  <label>Monto a aplicar (MXN):</label>
                  <input class="form-control" type="number" min="0.01" step="0.01" name="amountToApply" required="true"/>
                </div>
                <div class="col-md-2 text-right">
                  <br/>
                  <button class="btn btn-primary">Agregar</button>
                </div>
                </g:form>
              </div>
              <hr>
              </g:if>
              <g:if test="${conciliations}">
              <label>Facturas seleccionadas:</label>
              <div class="table-responsive">
                <table class="table">
                  <tr>
                    <th class="col-md-4">Factura</th>
                    <th>Total</th>
                    <th>Por pagar</th>
                    <th>Monto a aplicar (MXN)</th>
                    <th>Nuevo Saldo</th>
                    <th>Moneda</th>
                    <th>Tipo Cambio</th>
                    <th></th>
                  </tr>
                  <g:each in="${conciliations}" var="conciliation">
                  <tr>
                    <td>${conciliation.saleOrder.id} / ${conciliation.saleOrder.clientName}</td>
                    <td class="text-right">${modulusuno.formatPrice(number: conciliation.saleOrder.total)}</td>
                    <td class="text-right">${modulusuno.formatPrice(number: conciliation.saleOrder.amountToPay)}</td>
                    <td class="text-right">${modulusuno.formatPrice(number: conciliation.amount)}</td>
                    <td class="text-right">${modulusuno.formatPrice(number: conciliation.saleOrder.amountToPay - conciliation.amount)}</td>
                    <td>${conciliation.saleOrder.currency}</td>
                    <td>${conciliation.changeType ?: "NA"}</td>
                    <td class="text-center">
                      <button class="btn btn-danger">Quitar</button>
                    </td>
                  </tr>
                  </g:each>
                </table>
              </div>
              <hr>
              </g:if>
              <div class="row">
                <div class="col-md-6 text-center">
                  <button class="btn btn-danger">Cancelar</button>
                </div>
                <div class="col-md-6 text-center">
                  <g:if test="${conciliations && toApply == 0}">
                  <button class="btn btn-success">Aplicar</button>
                  </g:if>
                  <g:if test="${!conciliations}">
                    <div class="alert alert-warning" role="alert">No ha agregado facturas</div>
                  </g:if>
                  <g:if test="${conciliations && toApply > 0}">
                    <div class="alert alert-warning" role="alert">Aún dispone de monto por aplicar</div>
                  </g:if>
                </div>
              </div>

            </div>
          </div>
        </div>
      </div>
    </div>

  </body>
</html>
