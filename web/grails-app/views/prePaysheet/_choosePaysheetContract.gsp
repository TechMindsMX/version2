<div class="row">
  <div class="col-md-12">
    <div class="form-group">
      <label>Seleccione el contrato de nómina:</label>
      <g:select class="form-control" name="paysheetContractId" from="${paysheetContracts}" optionKey="id" optionValue="client" required=""/>
    </div>
  </div>
</div>
<div class="row">
  <div class="col-md-12 text-right">
    <g:submitButton name="choose" class="btn btn-primary" value="Crear" />
    <g:link class="btn btn-default" controller="dashboard" action="index">Cancelar</g:link>
  </div>
</div>

