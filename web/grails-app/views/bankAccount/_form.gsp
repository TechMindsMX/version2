<f:display id="clabe" bean="bankAccount" property="clabe" label="${message(code:"bankAccount.clabe")}" wrapper="editField" />
<f:display id="branchNumber" bean="bankAccount" property="branchNumber" label="${message(code:"bankAccount.branchNumber")}" wrapper="editField" />
<f:display id="accountNumber" bean="bankAccount" property="accountNumber" label="${message(code:"bankAccount.accountNumber")}" wrapper="editReadOnly" />
<input id="bank" name="bank" type="hidden"/>
<div class="form-group">
  <label class="col-sm-5 control-label">Banco</label>
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
</g:if>
<g:if test="${params.companyBankAccount}">
<input type="hidden" name="company" value="${session.company}" />
<input type="hidden" name="companyBankAccount" value="${params.companyBankAccount}" />
</g:if>
<g:else>
<input type="hidden" name="businessEntity" value="${params.businessEntity}" />
</g:else>

<div class="form-group">
</div>

