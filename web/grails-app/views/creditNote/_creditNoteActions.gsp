<%! import com.modulus.uno.status.CreditNoteStatus %>
<g:if test="${creditNote.status == CreditNoteStatus.APPLIED && isEnabledToStamp}">
  <div class="text-right">
    <a href="${modulusuno.creditNoteUrl(creditNote:creditNote, format:'xml')}" class="btn btn-success" download>XML</a>
    <a href="${modulusuno.creditNoteUrl(creditNote:creditNote, format:'pdf')}" class="btn btn-default" download>PDF</a>
  </div>
</g:if>

<sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR,ROLE_OPERATOR_EJECUTOR">
  <g:if test="${creditNote.status == CreditNoteStatus.CREATED}">
    <div class="text-right">
    <g:if test="${creditNote.items}">
      <g:link class="btn btn-primary" action="requestAuthorization" id="${creditNote.id}">Solicitar Autorización</g:link>
    </g:if>
    
    <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#modalConfirm">
      <i class="fa fa-trash"></i> Borrar
    </button>
    </div>

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
            <g:link class="btn btn-primary" action="deleteCreditNote" id="${creditNote.id}">Sí</g:link>
            <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
          </div>
        </div>
      </div>
    </div>
  </g:if>
</sec:ifAnyGranted>

<sec:ifAnyGranted roles="ROLE_AUTHORIZER_EJECUTOR">
  <g:if test="${creditNote.status == CreditNoteStatus.TO_AUTHORIZE}">
    <div class="text-right">
      <g:link class="btn btn-primary" action="authorize" id="${creditNote.id}">Autorizar</g:link>
    </div>
  </g:if>
</sec:ifAnyGranted>

<sec:ifAnyGranted roles="ROLE_FICO_EJECUTOR">
  <g:if test="${creditNote.status == CreditNoteStatus.AUTHORIZED && isEnabledToStamp}">
    <div class="text-right">
      <g:link class="btn btn-primary" action="apply" id="${creditNote.id}">Aplicar</g:link>
    </div>
  </g:if>
  <g:if test="${!isEnabledToStamp}">
    <div class="alert alert-warning">
      No está habilitado para timbrar facturas, debe registrar su certificado y su domicilio fiscal
    </div>
  </g:if>
</sec:ifAnyGranted>
