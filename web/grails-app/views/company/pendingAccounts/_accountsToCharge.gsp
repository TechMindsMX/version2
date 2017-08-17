<%! import java.text.SimpleDateFormat %>
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
