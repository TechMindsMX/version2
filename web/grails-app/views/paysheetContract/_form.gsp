<input type="hidden" name="companyId" value="${company.id}"/>
<div class="row">
  <div class="col-md-6">
    <div class="form-group">
      <label><g:message code="paysheetContract.label.client"/></label>
      <g:select class="form-control" name="clientId" from="${clients}" optionKey="id" value="${paysheetContract?.client?.id}" required=""/>
    </div>
  </div>
  <div class="col-md-6">
    <div class="form-group">
      <label><g:message code="paysheetContract.label.employerRegistration"/></label><br/>
      <input type="text" name="employerRegistration" value="${paysheetContract?.employerRegistration}" maxLength="11" required="required">
    </div>
  </div>
</div>
<div class="row">
  <div class="col-md-6">
    <div class="form-group">
      <label><g:message code="paysheetContract.label.initDate"/></label><br/>
      <input type="text" id="datepicker" name="initDate" value="${paysheetContract?.initDate?.format('dd/MM/yyyy')}" required="required" autocomplete="off">
    </div>
  </div>
  <div class="col-md-6">
    <div class="form-group">
      <label><g:message code="paysheetContract.label.executive"/></label><br/>
      <g:select class="form-control" name="executiveId" from="${users}" optionKey="id" value="${paysheetContract?.executive?.id}" optionValue="name" required=""/>
    </div>
  </div>
</div>
<div class="row">
  <div class="col-md-6">
    <div class="form-group">
      <label><g:message code="paysheetContract.label.serie"/></label><br/>
      <input type="text" name="serie" value="${paysheetContract?.serie}" maxLength="10">
    </div>
  </div>
  <div class="col-md-6">
    <div class="form-group">
      <label><g:message code="paysheetContract.label.folio"/></label><br/>
      <input type="number" name="folio" value="${paysheetContract ? paysheetContract.folio : 1}" min="1" step="1" required="required">
    </div>
  </div>
</div>

