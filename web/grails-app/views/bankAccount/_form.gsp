<div class="form-group">
  <label class="col-sm-5">Clabe <span class="required-indicator">*</span></label>
  <div class="col-sm-4">
    <input class="form-control" type="number" id="clabe" name="clabe" max="999999999999999999" required=""/>
  </div>
</div>
<div class="form-group">
  <label class="col-sm-5">Plaza <span class="required-indicator">*</span></label>
  <div class="col-sm-4">
    <input class="form-control" type="number" id="branchNumber" name="branchNumber" max="999" required=""/>
  </div>
</div>
<div class="form-group">
  <label class="col-sm-5">Número de Cuenta</label>
  <div class="col-sm-4">
    <input class="form-control" type="number" id="accountNumber" name="accountNumber" readonly=""/>
  </div>
</div>
<input id="bank" name="bank" type="hidden"/>
<div class="form-group">
  <label class="col-sm-5">Banco <span class="required-indicator">*</span></label>
  <div class="col-sm-4">
    <g:select name="banco" from="${banks}" optionValue="name" optionKey="bankingCode" class="form-control" aria-controls="example-table" readonly="true" noSelection="['':'']"/>
  </div>
</div>

<g:if test="${params.companyBankAccount}" >
  <g:if test="${bankLib.checkAccountForSTPAvailable() == '0' }" >
    <div class="center">
      <input type="radio" name="concentradora" value="true" class="control-form"> Cuenta Concentradora<br>
    </div>
  </g:if>
  <input type="hidden" name="company" value="${session.company}" />
  <input type="hidden" name="companyBankAccount" value="${params.companyBankAccount}" />
</g:if>
<g:else>
  <input type="hidden" name="businessEntity" value="${params.businessEntity}" />
</g:else>

<div class="form-group">
  <label class="col-sm-5">Número de Tarjeta</label>
  <div class="col-sm-4">
    <input class="form-control" type="number" id="cardNumber" name="cardNumber" max="9999999999999999"/>
  </div>
</div>

