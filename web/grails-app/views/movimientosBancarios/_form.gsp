<input type="hidden" name="idBankAccount" value="${movimientosBancarios?.cuenta?.id}" />
<div class="row">
  <div class="col-md-12">
    <div class="form-group">
      <label>Concepto:</label>
      <input type"text" name="concept" class="form-control" required="required" pattern=".{5,200}" placeholder="De 5 a 200 caracteres" title="Ingrese al menos 5 caracteres y hasta 200" value="${movimientosBancarios?.concept}"/>
    </div>
  </div>
</div>
<div class="row">
  <div class="col-md-4">
    <div class="form-group">
      <label>Referencia:</label>
      <input type"text" name="reference" class="form-control" required="" pattern=".{5,200}" placeholder="De 5 a 200 caracteres" title="Ingrese al menos 5 caracteres y hasta 200" value="${movimientosBancarios?.reference}"/>
    </div>
  </div>
  <div class="col-md-4">
    <div class="form-group">
      <label>Monto:</label>
      <input type="text" name="amount" class="form-control" required="required" pattern="[0-9]+(\.[0-9]{1,2})?" title="Ingrese una cantidad en formato correcto (número sin decimales o con 2 decimales exactamente)" placeholder="Cantidad con 1 ó 2 decimales o sin decimales" value="${modulusuno.quantityWithoutComma(number:movimientosBancarios?.amount)}"/>
    </div>
  </div>
  <div class="col-md-4">
    <div class="form-group">
      <label>Fecha:</label>
      <input class="form-control" type="text" id="datepicker" name="dateEvent" required="required" value="${modulusuno.dateFormat(date:movimientosBancarios?.dateEvent)}">
    </div>
  </div>
</div>
<div class="row">
  <div class="col-md-6">
    <div class="form-group">
      <label>Tipo:</label>
      <g:select id="type" name="type" from="${com.modulus.uno.MovimientoBancarioType.values()}" class="form-control" value="${movimientosBancarios?.type}"/>
    </div>
  </div>
  <div class="col-md-6">
    <div class="form-group">
      <label>Es conciliable:</label>
      <g:checkBox class="form-control" name="reconcilable" value="${false}" />
    </div>
  </div>
</div>
