<%! import com.modulus.uno.CommissionType %>
<f:with bean="commission">
<div class="form-group">
  <label for="">${message(code:"commission.type.label")}</label>
  <g:select name="type" from="${CommissionType.values()}" value="${commission.type}" class="form-control" />
</div>
<div class="form-group">
  <label for="">${message(code:"commission.fee.label")}</label>
  <input type="text" name="fee" class="form-control" value="${formatNumber(number:commission?.fee,locale: 'es_MX', format:'##0.00')}" min="0.00" pattern="[0-9]+(\.[0-9]{2})?$" title="Ingrese una cantidad sin separador de miles y con dos decimales"/>
</div>
<div class="form-group">
  <label for="">${message(code:"commission.percentage.label")}</label>
  <input type="text" name="percentage" class="form-control" value="${formatNumber(number:commission?.percentage,locale: 'es_MX', format:'##0.00')}" min="0.00" pattern="[0-9]+(\.[0-9]{2})?$" title="Ingrese una cantidad sin separador de miles y con dos decimales" />
</div>
</f:with>
<input type="hidden" id="company" name="company" value="${company.id}" />
