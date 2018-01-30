<%! import com.modulus.uno.BusinessEntityStatus %>
<div class="panel panel-default">
  <div class="panel-heading">
    <div class="row">
      <div class="col-md-12">
        <div class="col-md-6">
          <span class="property-label"><h4><g:message code="businessEntity.label.bankAccounts.${relation}" default="Cuentas Bancarias"/></h4></span>
        </div>
        <div class="col-md-6" align="right">

          <div class="property-value">
            <fieldset class="buttons">
              <sec:ifAnyGranted roles="ROLE_OPERATOR_EJECUTOR">
                <g:if test="${businessEntity.status == BusinessEntityStatus.ACTIVE || businessEntity.status == BusinessEntityStatus.TO_AUTHORIZE}">
                  <g:link class="btn btn-default" action="create" controller="bankAccount" params="[businessEntity:businessEntity.id, businessEntityInfo:businessEntity.toString(), relation:relation, businessEntityBankAccount:true]"><g:message code="businessEntity.label.createBankAccount.${relation}" default="Agregar Cuenta"/></g:link>
                </g:if>
              </sec:ifAnyGranted>
            </fieldset>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div class="panel-body">
    <div class="property-value">
      <g:if test="${!businessEntity.banksAccounts}">
        <p><h4><label class="label label-warning"><g:message code="businessEntity.label.noBankAccounts.${relation}" default="No hay cuentas bancarias registradas"/></label></h4></p>
      </g:if>
          <ul>
            <g:each in="${businessEntity.banksAccounts.sort{it.banco.name}}" var="account">
            <li class="sublist">
              <g:if test="${businessEntity.status == BusinessEntityStatus.ACTIVE || businessEntity.status == BusinessEntityStatus.TO_AUTHORIZE}">
                <g:link controller="bankAccount" action="edit" params="[businessEntity:businessEntity.id, businessEntityInfo:businessEntity.toString(), relation:relation, businessEntityBankAccount:true]" id="${account.id}">
                  <g:if test="${relation != 'CLIENTE'}">${account}</g:if>
                  <g:else>${account.banco} - ${account.accountNumber}</g:else>
                </g:link>
              </g:if><g:else>
                <g:if test="${relation != 'CLIENTE'}">${account}</g:if>
                <g:else>${account.banco} - ${account.accountNumber}</g:else>
              </g:else>
            </li>
            </g:each>
          </ul>
    </div>
  </div>
</div>
