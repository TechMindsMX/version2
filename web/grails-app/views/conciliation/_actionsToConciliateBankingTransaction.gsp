<%! import com.modulus.uno.PaymentWay %>
<%! import com.modulus.uno.Bank %>
<div class="row">
  <div class="col-md-3 text-center">
    <g:link class="btn btn-info" controller="payment" action="conciliation">Regresar</g:link>
  </div>
  <div class="col-md-3 text-center">
    <g:if test="${conciliations}">
    <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#modalConfirm" title="Al cancelar se borrarán las facturas seleccionadas">
      Cancelar
    </button>
    <div class="modal fade" id="modalConfirm" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title" id="myModalLabel">Confirme la acción</h4>
          </div>
          <div class="modal-body">
            ¿Está seguro de cancelar la conciliación (se eliminarán los datos seleccionados)?
          </div>
          <div class="modal-footer">
            <g:link action="cancelConciliationBankingTransaction" id="${bankingTransaction.id}" class="btn btn-primary">
            Sí
            </g:link>
            <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
          </div>
        </div>
      </div>
    </div>
    </g:if>
  </div>
  <g:if test="${conciliations && toApply == 0}">
    <div class="col-md-6 text-right">
      <g:form action="applyConciliationsForBankingTransaction" id="${bankingTransaction.id}">
        <button id="btnApply" type="submit" class="btn btn-success">Aplicar</button>
        <input type="checkbox" id="chkPaymentComplement" name="chkPaymentComplement"/> <label>Complemento de Pago SAT</label>
      </g:form>
    </div>
  </g:if>
    <g:if test="${!conciliations}">
    <div class="col-md-6 text-center">
      <div class="alert alert-warning" role="alert">No ha agregado facturas</div>
    </div>
    </g:if>
    <g:if test="${conciliations && toApply > 0}">
    <div class="col-md-6 text-center">
      <div class="alert alert-warning" role="alert">Aún dispone de monto por aplicar</div>
    </div>
    </g:if>
</div>
<div class="row">
  <div class="col-md-12">
<div class="collapse" id="collapsePaymentComplement">
  <hr>
  <div class="well">
    <g:form action="applyConciliationsForBankingTransaction" id="${bankingTransaction.id}">
      <g:hiddenField name="chkPaymentComplement" value="on"/>
      <div class="form-group">
        <label>Forma de Pago del Depósito:</label>
        <g:select class="form-control" name="paymentWay" from="${PaymentWay.values()}" optionKey="key" optionValue="description" value="${PaymentWay.TRANSFERENCIA_ELECTRONICA}"/>
      </div>
      <div class="form-group">
        <label>Banco Origen:</label>
        <g:select class="form-control" name="sourceBank" from="${Bank.list()}" optionKey="id" optionValue="name" noSelection="['':'Elija el Banco Origen...']" required=""/>
      </div>
      <div class="form-group">
        <label>Cuenta Origen:</label>
        <g:textField class="form-control" name="sourceAccount" required="" pattern="[0-9]*" placeholder="Hasta 18 dígitos"/>
      </div>
      <div class="text-right">
        <button type="submit" class="btn btn-success">Aplicar</button>
      </div>
    </g:form>
  </div>
</div>
  </div>
</div>
