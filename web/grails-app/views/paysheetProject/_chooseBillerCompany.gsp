<%! import com.modulus.uno.paysheet.PaymentSchema %>
<div class="portlet portlet-default">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4>Seleccione Empresa Facturadora</h4>
    </div>
    <div class="clearfix"></div>
  </div>

  <div class="portlet-body">
    <g:hiddenField name="paysheetProject" value="${paysheetProject.id}"/>
    <div class="row">
      <div class="col-md-6">
        <div class="form-group">
          <label>Empresa:</label>
          <g:select class="form-control" name="company" from="${billersList.sort{it.bussinessName}}" required="" noSelection="['':'- Elija la empresa']" optionKey="id"/>
        </div>
      </div>
      <div class="col-md-6">
        <div class="form-group">
          <label>Esquema de Pago:</label>
          <g:select class="form-control" name="schema" from="${PaymentSchema.values()}" required=""/>
        </div>
      </div>
    </div>
  </div>

  <div class="portlet-footer">
    <div class="row">
      <div class="col-md-12 text-right">
        <button class="btn btn-primary">Guardar</button>
      </div>
    </div>
  </div>

</div>

