<input type="hidden" name="clabe" value="002115016003269411"/>
<input type="hidden" name="branchNumber" value="115"/>
<input type="hidden" name="relation" value="${relation}"/>
<input type="hidden" name="businessEntity" value="${params.businessEntity}"/>
<input type="hidden" name="businessEntityBankAccount" value="${params.businessEntityBankAccount}"/>

<div class="form-group">
  <label class="col-sm-5 control-label">Últimos 4 dígitos del número de cuenta</label>
  <div class="col-sm-4">
    <g:field class="form-control" name="accountNumber" value="${bankAccount?.accountNumber?.replace('*','')}" maxLength="4" pattern="[0-9]{4}" title="Ingrese 4 dígitos exactamente" required=""/>
  </div>
</div>
<div class="form-group">
  <label class="col-sm-5 control-label">Banco</label>
  <div class="col-sm-4">
    <g:select name="bank" from="${banks}" optionValue="name" optionKey="bankingCode" class="form-control" aria-controls="example-table" required="" noSelection="['':'']" value="${bankAccount?.banco?.bankingCode}"/>
  </div>
</div>

