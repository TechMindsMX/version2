<input type="hidden" name="companyId" value="${company.id}"/>
<div class="row">
  <div class="col-md-12">
    <div class="form-group">
      <label><g:message code="prePaysheet.label.paysheetProject"/></label>
      <g:select class="form-control" name="paysheetProject" from="${company.paysheetProjects.sort{it.name}}" optionKey="name" optionValue="name" value="${prePaysheet?.paysheetProject}"/>
    </div>
  </div>
</div>
<div class="row">
  <div class="col-md-12">
    <div class="form-group">
      <label><g:message code="prePaysheet.label.paymentPeriod"/></label><br/>
      <input class="form-control" type="text" name="paymentPeriod" value="${prePaysheet?.paymentPeriod}" required="" maxLength="200"/>
    </div>
  </div>
</div>

<div class="row">
  <div class="col-md-12">
    <div class="form-group">
      <label><g:message code="prePaysheet.label.accountExecutive"/></label>
      <input class="form-control" type="text" name="accountExecutive" value="${prePaysheet?.accountExecutive}" required="" maxLength="200"/>
    </div>
  </div>
</div>

