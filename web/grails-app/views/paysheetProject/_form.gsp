<%! import com.modulus.uno.FederalEntity %>
<input type="hidden" name="contractId" value="${paysheetProject?.paysheetContract?.id}"/>
<div class="row">
  <div class="col-md-3">
    <div class="form-group">
      <label><g:message code="paysheetProject.label.name"/></label>
      <g:if test="${paysheetProject?.id}">
        <g:hiddenField name="name" value="${paysheetProject?.name}"/>
        <input class="form-control" type="text" name="currentName" value="${paysheetProject?.name}" disabled="" maxLength="50"/>
      </g:if><g:else>
        <input class="form-control" type="text" name="name" value="${paysheetProject?.name}" required="" maxLength="50"/>
      </g:else>
    </div>
  </div>
  <div class="col-md-6">
    <div class="form-group">
      <label><g:message code="paysheetProject.label.description"/></label><br/>
      <input class="form-control" type="text" name="description" value="${paysheetProject?.description}" required="" maxLength="200"/>
    </div>
  </div>
  <div class="col-md-3">
    <div class="form-group">
      <label><g:message code="paysheetProject.label.federalEntity"/></label>
      <g:select class="form-control" name="federalEntity" from="${FederalEntity.values()}" value="${paysheetProject?.federalEntity}" required=""/>
    </div>
  </div>
</div>

<div class="row">
  <div class="col-md-4">
    <div class="form-group">
      <label><g:message code="paysheetProject.label.integrationFactor"/></label>
      <input class="form-control" type="number" min="0.000000" step="0.000001" name="integrationFactor" value="${paysheetProject?.integrationFactor}" required=""/>
    </div>
  </div>
  <div class="col-md-4">
    <div class="form-group">
      <label><g:message code="paysheetProject.label.occupationalRiskRate"/></label>
      <input class="form-control" type="number" min="0.000000" max="100.000000" step="0.000001" name="occupationalRiskRate" value="${paysheetProject?.occupationalRiskRate}" required=""/>
    </div>
  </div>
  <div class="col-md-4">
    <div class="form-group">
      <label><g:message code="paysheetProject.label.commission"/></label>
      <input class="form-control" type="number" min="0.000000" max="100.00" step="0.000001" name="commission" value="${paysheetProject?.commission}" required=""/>
    </div>
  </div>
</div>

