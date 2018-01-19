<%! import com.modulus.uno.quotation.QuotationRequestStatus %>
<div class="row">
  <div class="col-md-6">
    <dl>
      <dt>Cliente cotización</dt>
      <dd>${quotationRequest.quotationContract.client}</dd>
      <dt>Fecha de creación</dt>
      <dd><g:formatDate format="dd-MM-yyyy" date="${quotationRequest.dateCreated}" class="form-control"/></dd>
      <g:if test="${quotationRequest.status == QuotationRequestStatus.PROCESSED}">
        <dt>Facturadora</dt>
        <dd>${quotationRequest.biller}</dd>
        <dt>Producto facturado</dt>
        <dd>${quotationRequest.product?.name}</dd>
        <dt>Comisión</dt>
        <dd>${quotationRequest.commission}</dd>
      </g:if>
    </dl>
  </div>
  <div class="col-md-6">
    <dl>
      <dt>Descripción</dt>
      <dd>${quotationRequest.description}</dd>
      <dt>Subtotal</dt>
      <dd>${modulusuno.formatPrice(number:quotationRequest.subtotal)}</dd>
      <dt>Iva</dt>
      <dd>${modulusuno.formatPrice(number:quotationRequest.iva)}</dd>
      <dt>Total</dt>
      <dd>${modulusuno.formatPrice(number:quotationRequest.total)}</dd>
      <dt>Estatus</dt>
      <dd><g:message code="quotationRequest.status.${quotationRequest.status}"/></dd>
    </dl>
  </div>
</div>
