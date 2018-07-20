<%! import com.modulus.uno.businessEntity.BusinessEntitiesGroupType %>
<f:with bean="businessEntitiesGroup">
<div class="form-group">
  <label for="">${message(code:"businessEntitiesGroup.company.label")}</label>
  <g:select name="company" from="${companies}" value="${businessEntitiesGroup.company}" class="form-control" />
</div>
<div class="form-group">
  <label for="">${message(code:"businessEntitiesGroup.type.label")}</label>
  <g:select name="type" from="${BusinessEntitiesGroupType.values()}" value="${businessEntitiesGroup.type}" class="form-control" />
</div>
<div class="form-group">
  <label for="">${message(code:"businessEntitiesGroup.description.label")}</label>
  <input type="text" name="description" class="form-control" value="${businessEntitiesGroup.description}"/>
</div>
</f:with>
