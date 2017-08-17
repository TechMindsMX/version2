<%! import java.text.SimpleDateFormat %>
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
        </div>
      </div>
    </div>
  </div><!--container-->
