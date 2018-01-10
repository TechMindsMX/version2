<div class="form-group col-sm-12">
<f:field bean="corporate" property="nameCorporate" label="${message(code:'corporate.nameCorporate')}*" class="input-group" value="${corporate.nameCorporate}" wrapper="create" />
</div>

<div class="form-group col-sm-8">
  <label for="corporateUrl">
    ${message(code:'corporate.corporateUrl')}*
  </label>
  <div class="input-group">
  <div class="input-group-addon">https://</div>
    <input class="form-control" id="corporateUrl" name="corporateUrl" placeholder="Corporativo" value="${corporate.corporateUrl}">
    <div class="input-group-addon">.modulusuno.com</div>
  </div>
</div>
<div class="form-group col-sm-4" style="margin-top:30px;">
  <input type="checkbox" name="hasQuotationContract" name="hasQuotationContract" value="true" />
  <label for="quotationService">Servicios de Cotizaciones</label>
</div>
