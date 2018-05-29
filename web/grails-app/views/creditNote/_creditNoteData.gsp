<%! import com.modulus.uno.status.CreditNoteStatus %>

<div class="portlet portlet-default">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4>Datos de la Nota de Crédito</h4>
    </div>
    <div class="clearfix"></div>
  </div>
  <div id="defaultPortlet" class="panel-collapse collapse in">
    <div class="portlet-body">
			<div class="row">
			  <div class="col-md-6">
          <dl class="dl-horizontal">
            <dt>No.</dt>
            <dd>${creditNote.id}</dd>
            <dt>Forma de pago:</dt>
            <dd>${creditNote.paymentWay}</dd>
            <dt>Método de Pago:</dt>
            <dd>${creditNote.paymentMethod}</dd>
            <dt>Serie:</dt>
            <dd>${creditNote.invoiceSerie}</dd>
            <dt>Folio:</dt>
            <dd>${creditNote.invoiceFolio}</dd>
            <dt>Estatus:</dt>
            <dd>${creditNote.status}</dd>
          </dl>
        </div>
			  <div class="col-md-6">
          <dl class="dl-horizontal">
            <dt>Subtotal</dt>
            <dd>${modulusuno.formatPrice(number:creditNote.subtotal)}</dd>
            <dt>Descuento</dt>
            <dd>${modulusuno.formatPrice(number:creditNote.totalDiscount)}</dd>
            <dt>IVA</dt>
            <dd>${modulusuno.formatPrice(number:creditNote.totalIVA)}</dd>
            <dt>Retención IVA</dt>
            <dd>${modulusuno.formatPrice(number:creditNote.totalIvaRetention)}</dd>
            <dt>Total</dt>
            <dd>${modulusuno.formatPrice(number:creditNote.total)}</dd>
          </dl>
        </div>
      </div>
      <div class="row">
        <div class="col-md-12">
          <g:if test="${creditNote.status == CreditNoteStatus.CANCELED || creditNote.status == CreditNoteStatus.REJECTED}">
            <div class="alert alert-danger" role="alert">
              <label class="control-label">Motivo ${message(code:'creditNote.rejectReason.'+creditNote.status)}:</label>
              <g:message code="rejectReason.${creditNote.rejectReason}" default="${creditNote.rejectReason}"/>
              <textarea class="form-control" rows="3" cols="60" readonly>${creditNote.comments}</textarea>
            </div>
          </g:if>
        </div>
      </div>

    </div>
  </div>
</div>
 
