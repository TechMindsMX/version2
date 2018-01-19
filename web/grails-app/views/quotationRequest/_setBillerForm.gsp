<div class="row">
   <div class="col-md-12">
      <dt>Facturadora </dt>
      <g:select class="form-control" name="biller" from="${billers}" optionKey="id"/>
    </div>
    <div class="col-md-12">
      <br>
      <dt>Producto </dt>
      <g:select name="product" class="form-control" from="${products}" optionValue="name" optionKey="id"/>
    </div>
    <div class="col-md-3">
      <br>
      <dt><g:message code="Comisión"/></dt>
      <input class="form-control" type="number" id="commission" name="commission" value="${quotationRequest.commission}" required="required">
    </div>
  </div>
</div>
<div class="row">
  <div class="col-md-12 text-right">
    <button class="btn btn-primary" type="submit"  id="${quotationRequest.id}">Procesar</button>
  </div>
</div>
