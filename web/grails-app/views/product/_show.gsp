<div class="form-group">
  <label for=""><g:message code="product.satKey.label"/>:</label>
  ${product.satKey}
</div>
<div class="form-group">
  <label for=""><g:message code="product.sku"/>:</label>
  ${product.sku}
</div>
<div class="form-group">
  <label for=""><g:message code="product.name"/>:</label>
  ${product.name}
</div>
<div class="form-group">
  <label for=""><g:message code="product.price"/>:</label>
  ${modulusuno.formatPrice(number:product.price)}
</div>
<div class="form-group">
  <label for=""><g:message code="product.ieps"/>:</label>
  ${product.ieps}
</div>
<div class="form-group">
  <label for=""><g:message code="product.iva"/>:</label>
  ${product.iva}
</div>
<div class="form-group">
  <label for=""><g:message code="product.currencyType"/>:</label>
  ${product.currencyType}
</div>
<div class="form-group">
  <label for=""><g:message code="product.unitType"/>:</label>
  ${product.unitType?.name}
</div>

