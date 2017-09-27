<input type="hidden" name="idBankAccount" value="${movimientosBancarios?.cuenta?.id}" />
<div class="row">
  <div class="col-md-12">
    <div class="form-group">
      <label>Concepto:</label>
      <input type"text" name="concept" class="form-control" required="required" value="${movimientosBancarios?.concept}"/>
    </div>
  </div>
</div>
<div class="row">
  <div class="col-md-4">
    <div class="form-group">
      <label>Referencia:</label>
      <input type"text" name="reference" class="form-control" value="${movimientosBancarios?.reference}" />
    </div>
  </div>
  <div class="col-md-4">
    <div class="form-group">
      <label>Monto:</label>
      <input type="text" name="amount" class="form-control"  required="required" value="${modulusuno.formatQuantity(number:movimientosBancarios?.amount)}"/>
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
