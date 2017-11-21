<div class="row">
  <div class="col-md-11">
    <div class="form-group">
      <label>
        <g:message code="Clientes" />
      </label>
      <g:select name="id" class="form-control" from="${quotationContractList}" optionValue="client" optionKey="id">
      </g:select>
    </div>
  </div>
  <div class="row">
    <div class="col-md-6 text-right">
      <g:submitButton name="seleccionar" class="btn btn-primary" value="${message(code: 'default.button.seleccionar.label', default: 'Seleccionar')}"
      />
    </div>
  </div>
  </div>

