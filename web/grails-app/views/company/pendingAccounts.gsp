<!DOCTYPE html>
<%! import java.text.SimpleDateFormat %>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'accountStatement.label', default: 'AccountStatement')}" />
    <title><g:message code="manager.pendingAccounts.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
      <i class="fa fa-money fa-3x"></i>
      <g:message code="manager.pendingAccounts.label"/>
      </h1>
      <ol class="breadcrumb">
        <li><i class="fa fa-caret-square-o-up"></i>Modulus UNO</li>
        <li class="active"><g:message code="manager.pendingAccounts.label"/></li>
      </ol>
    </div>
    <div class="portlet portlet-blue">
      <div class="portlet-heading">Saldo Bancario</div>
      <div class="portlet-body">
        <div class="container-fluid">
        <g:if test="${mainAccount}">
          <div class="col-md-4">
            <b>${mainAccount.banco}</b>
          </div>
          <div class="col-md-4 text-center">
            <p><b>Cuenta: ${mainAccount.accountNumber}</b></p>
            <p><b>Clabe: ${mainAccount.clabe}</b></p>
          </div>
          <div class="col-md-4 text-right">
            <b><modulusuno:amountAccountToday id="${mainAccount.id}"/></b>
          </div>
        </g:if>
        <g:else>
          <div class="alert alert-warning text-center">
            No hay cuenta bancaria concentradora
          </div>
        </g:else>
        </div>
      </div>
    </div>

    <div class="portlet portlet-blue">
      <div class="portlet-body">
      <div class="row">
        <g:form action="pendingAccounts">
          <div class="col-md-4">
            <p>Desde el:</p>
            <input type="text" id="startDate" name="startDate" required="required" value="${new SimpleDateFormat('dd/MM/yyyy').format(pendingAccounts.startDate)}">
          </div>
          <div class="col-md-4">
            <p>Hasta el:</p>
            <input type="text" id="endDate" name="endDate" required="required" value="${new SimpleDateFormat('dd/MM/yyyy').format(pendingAccounts.endDate)}">
          </div>
          <div class="col-md-2 text-righ text-right">
            <g:actionSubmit class="btn btn-default" value="Consultar" action="pendingAccounts"/>
          </div>
        </g:form>
        <div class="col-md-2 text-righ text-right">
          <g:link class="btn btn-default" action="generateXlsForPendingAccounts" params="[startDate:pendingAccounts.startDate, endDate:pendingAccounts.endDate]">
            <i class="fa fa-file-excel-o"></i> XLS
          </g:link>
        </div>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-md-6">
        <div class="portlet portlet-blue">
      <div class="portlet-heading">Cuentas por Cobrar</div>
      <div class="portlet-body">
        <div class="text-right">
        <p><b>Total: ${modulusuno.formatPrice(number:pendingAccounts.totalCharges)}</b></p>
        <p><b>Vencido: ${modulusuno.formatPrice(number:pendingAccounts.totalExpiredCharges)}</b></p>
        </div>
        <div class="text-right">
          <button class="btn btn-primary" type="button" data-toggle="collapse" data-target="#detailCharges">Detalle</button>
        </div>
      </div>
      <div class="collapse" id="detailCharges">
        <div class="container-fluid">
          <div class="well">
            <div class="table-responsive">
              <table class="table table-condensed table-hover">
                <thead>
                  <tr>
                  <th class="text-center">Cliente</th>
                  <th class="text-center">Fecha Cobro</th>
                  <th class="text-center">Monto</th>
                  </tr>
                </thead>
                <tbody>
                  <g:each var="chargeExpired" in="${pendingAccounts.listExpiredCharges.sort{it.fechaCobro}}">
                    <tr class="warning">
                      <td>${chargeExpired.clientName}</td>
                      <td class="text-center">
                        <button type="button" class="btn btn-link" data-toggle="modal" data-target="#changeDateChargeModal" data-whatever="${chargeExpired.id}">
                          <g:formatDate format="dd-MM-yyyy" date="${chargeExpired.fechaCobro}"/>
                        </button>
                      </td>
                      <td class="text-right">${modulusuno.formatPrice(number:chargeExpired.total)}</td>
                    </tr>
                  </g:each>
                  <g:each var="charge" in="${pendingAccounts.listCharges.sort{it.fechaCobro}}">
                    <tr>
                      <td>${charge.clientName}</td>
                      <td class="text-center">
                        <button type="button" class="btn btn-link" data-toggle="modal" data-target="#changeDateChargeModal" data-whatever="${charge.id}">
                          <g:formatDate format="dd-MM-yyyy" date="${charge.fechaCobro}"/>
                        </button>
                      </td>
                      <td class="text-right">${modulusuno.formatPrice(number:charge.total)}</td>
                    </tr>
                  </g:each>
                </tbody>
              </table>
            </div><!--table responsive-->
            <!-- modal change date -->
            <div class="modal fade" id="changeDateChargeModal" tabindex="-1" role="dialog" aria-labelledby="changeDateChargeModalLabel">
              <div class="modal-dialog" role="document">
                <div class="modal-content">
                  <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="changeDateChargeModalLabel">Cambiar fecha de Cobro</h4>
                  </div>
                  <g:form action="updateDateCharge">
                  <input type="hidden" id="startDate" name="startDate" value="${new SimpleDateFormat('dd/MM/yyyy').format(pendingAccounts.startDate)}"/>
                  <input type="hidden" id="endDate" name="endDate" value="${new SimpleDateFormat('dd/MM/yyyy').format(pendingAccounts.endDate)}"/>
                  <div class="modal-body">
                      <div class="form-group">
                        <label for="chargeId" class="control-label">No. de Orden:</label>
                        <input type="text" class="form-control" id="chargeId" name="chargeId">
                      </div>
                      <div class="form-group">
                        <label for="message-text" class="control-label">Nueva Fecha:</label>
                        <input type="text" id="datepickerCharge" name="fechaCobro" required="required">
                      </div>
                  </div>
                  <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                    <button type="submit" class="btn btn-primary">Cambiar</button>
                  </div>
                  </g:form>
                </div>
              </div>
            </div>
            <!-- modal change date end -->
          </div>
        </div><!--container-->
      </div>
    </div>
      </div><!--end col-6-->
      <div class="col-md-6">
       
    <div class="portlet portlet-blue">
      <div class="portlet-heading">Cuentas por Pagar</div>
      <div class="portlet-body">
        <div class="text-right">
        <p><b>Total: ${modulusuno.formatPrice(number:pendingAccounts.totalPayments)}</b></p>
        <p><b>Vencido: ${modulusuno.formatPrice(number:pendingAccounts.totalExpiredPayments)}</b></p>
        </div>
        <div class="text-right">
          <button class="btn btn-primary" type="button" data-toggle="collapse" data-target="#detailPayments">Detalle</button>
        </div>
      </div>
      <div class="collapse" id="detailPayments">
        <div class="container-fluid">
          <div class="well">
            <div class="table-responsive">
              <table class="table table-condensed table-hover">
                <thead>
                  <tr>
                    <th class="text-center">Proveedor</th>
                    <th class="text-center">Fecha Pago</th>
                    <th class="text-center">Monto</th>
                  </tr>
                </thead>
                <tbody>
                  <g:each var="payExpired" in="${pendingAccounts.listExpiredPayments.sort{it.fechaPago}}">
                    <tr class="warning">
                      <td>${payExpired.providerName}</td>
                      <td class="text-center">
                        <button type="button" class="btn btn-link" data-toggle="modal" data-target="#changeDatePaymentModal" data-whatever="${payExpired.id}">
                          <g:formatDate format="dd-MM-yyyy" date="${payExpired.fechaPago}"/>
                        </button>
                      </td>
                      <td class="text-right">${modulusuno.formatPrice(number:payExpired.total)}</td>
                    </tr>
                  </g:each>
                  <g:each var="pay" in="${pendingAccounts.listPayments.sort{it.fechaPago}}">
                    <tr>
                      <td>${pay.providerName}</td>
                      <td class="text-center">
                        <button type="button" class="btn btn-link" data-toggle="modal" data-target="#changeDatePaymentModal" data-whatever="${pay.id}">
                          <g:formatDate format="dd-MM-yyyy" date="${pay.fechaPago}"/>
                        </button>
                      </td>
                      <td class="text-right">${modulusuno.formatPrice(number:pay.total)}</td>
                    </tr>
                  </g:each>
                </tbody>
              </table>
            </div><!--table responsive-->
            <!-- modal change date for payments -->
            <div class="modal fade" id="changeDatePaymentModal" tabindex="-1" role="dialog" aria-labelledby="changeDatePaymentModalLabel">
              <div class="modal-dialog" role="document">
                <div class="modal-content">
                  <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="changeDatePaymentModalLabel">Cambiar fecha de Pago</h4>
                  </div>
                  <g:form action="updateDatePayment">
                  <input type="hidden" id="startDate" name="startDate" value="${new SimpleDateFormat('dd/MM/yyyy').format(pendingAccounts.startDate)}"/>
                  <input type="hidden" id="endDate" name="endDate" value="${new SimpleDateFormat('dd/MM/yyyy').format(pendingAccounts.endDate)}"/>
                  <div class="modal-body">
                      <div class="form-group">
                        <label for="paymentId" class="control-label">No. de Orden:</label>
                        <input type="text" class="form-control" id="paymentId" name="paymentId">
                      </div>
                      <div class="form-group">
                        <label for="message-text" class="control-label">Nueva Fecha:</label>
                        <input type="text" id="datepickerPayment" name="fechaPago" required="required">
                      </div>
                  </div>
                  <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                    <button type="submit" class="btn btn-primary">Cambiar</button>
                  </div>
                  </g:form>
                </div>
              </div>
            </div>
            <!-- modal change date payments end -->
        
      </div><!--end col-6-->
    </div><!--endrow-->
   
          </div>
        </div><!--container-->
      </div>
    </div>
    <asset:javascript src="pendingAccounts/pendingAccounts.js"/>
</body>
</html>
