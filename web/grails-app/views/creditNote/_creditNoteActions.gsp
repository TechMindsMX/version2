<%! import com.modulus.uno.status.CreditNoteStatus %>

<g:if test="${creditNote.status == CreditNoteStatus.CREATED}">
  <g:link class="btn btn-primary" action="requestAuthorization" id="${creditNote.id}">Solicitar Autorización</g:link>
  
  <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#modalConfirm">
    <i class="fa fa-trash"></i> Borrar
  </button>

  <div class="modal fade" id="modalConfirm" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
          <h4 class="modal-title" id="myModalLabel">Confirme la acción</h4>
        </div>
        <div class="modal-body">
          ¿Está seguro de eliminar la nota de crédito?
        </div>
        <div class="modal-footer">
          <g:link class="btn btn-primary" action="deleteCreditNote" id="${saleOrder.id}">Sí</g:link>
          <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
        </div>
      </div>
    </div>
  </div>

</g:if>
