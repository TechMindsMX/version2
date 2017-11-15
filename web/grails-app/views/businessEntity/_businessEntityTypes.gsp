<%! import com.modulus.uno.LeadType %>
<div class="form-group">
  <label><g:message code="businessEntity.clientProviderType" /><span class="required-indicator">*</span></label>
  <g:radioGroup name="clientProviderType"
      labels="['Cliente', 'Proveedor', 'Cliente/Proveedor', 'Emp/Colaborador']"
      values="[LeadType.CLIENTE, LeadType.PROVEEDOR, LeadType.CLIENTE_PROVEEDOR, LeadType.EMPLEADO]"
      value="${clientProviderType}" >
    <label>&nbsp; &nbsp; ${it.radio} ${it.label}</label>
  </g:radioGroup>
</div>

