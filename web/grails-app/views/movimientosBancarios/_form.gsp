<div class="row">
  <div class="col-md-12">
    <div class="form-group">
      <label>Concepto:</label>
      <input type"text" name="concept" class="form-control" required="required"/>
    </div>
  </div>
</div>
<div class="row">
  <div class="col-md-4">
    <div class="form-group">
      <label>Referencia:</label>
      <input type"text" name="reference" class="form-control" />
    </div>
  </div>
  <div class="col-md-4">
    <div class="form-group">
      <label>Monto:</label>
      <input type="text" name="amount" class="form-control"  required="required"/>
    </div>
  </div>
  <div class="col-md-4">
    <div class="form-group">
      <label>Fecha:</label>
      <input class="form-control" type="text" id="datepicker" name="dateEvent" required="required">
    </div>
  </div>
</div>
<div class="row">
  <div class="col-md-6">
    <div class="form-group">
      <label>Tipo:</label>
      <g:select id="type" name="type" from="${com.modulus.uno.MovimientoBancarioType.values()}" class="form-control" />
    </div>
  </div>
  <div class="col-md-6">
    <div class="form-group" id="reconcilable" style="display:none">
      <label>Es conciliable:</label>
      <g:checkBox class="form-control" name="reconcilable" value="${false}" />
    </div>
  </div>
</div>
