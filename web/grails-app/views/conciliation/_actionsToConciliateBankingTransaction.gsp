<%! import com.modulus.uno.status.ConciliationStatus %>
<div class="row">
  <div class="col-md-4 text-center">
    <g:link class="btn btn-info" controller="payment" action="conciliation">Regresar</g:link>
  </div>
  <div class="col-md-4 text-center">
    <g:if test="${conciliations}">
    <g:if test="${bankingTransaction.conciliationStatus == ConciliationStatus.TO_APPLY}">  
      <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#modalConfirm" title="Al cancelar se borrarán las facturas seleccionadas">
        Cancelar
      </button>
    </g:if>
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
  <div class="col-md-4 text-center">
    <g:if test="${conciliations && toApply == 0}">
      <g:link action="applyConciliationsForBankingTransaction" id="${bankingTransaction.id}" class="btn btn-success">Aplicar</g:link>
    </g:if>
    <g:if test="${!conciliations}">
    <div class="alert alert-warning" role="alert">No ha agregado facturas</div>
    </g:if>
    <g:if test="${conciliations && toApply > 0}">
    <div class="alert alert-warning" role="alert">Aún dispone de monto por aplicar</div>
    </g:if>
  </div>
</div>

