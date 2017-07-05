<%! import com.modulus.uno.BusinessEntityType %>
<div class="panel panel-default">
  <div class="panel-heading">
    <div class="row">
      <div class="col-md-12">
        <div class="col-md-6">
          <h4>Datos Generales</h4>
        </div>
        <div class="col-md-6" align="right">
          <g:form resource="${this.businessEntity}" method="DELETE">
          <fieldset class="buttons">
            <g:link class="edit btn btn-default" action="edit" resource="${this.businessEntity}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
          </fieldset>
            </g:form>
        </div>
      </div>
    </div>
  </div>

  <div class="panel-body">
    <g:if test="${flash.message}">
    <div class="alert alert-success" role="alert">${flash.message} - ${relation}
    </div>
    </g:if>

    <dl class="dl-horizontal">
      <dt>RFC</dt>
      <dd>${businessEntity.rfc}</dd>
      <g:if test="${relation == 'EMPLEADO'}">
        <dt>CURP</dt>
        <dd>${businessEntity.getCurp()}</dd>
        <dt>No. de Empleado</dt>
        <dd>${businessEntity.getNumber()}</dd>
      </g:if>
      <dt>Sitio web</dt>
      <dd>${businessEntity.website ?: 'Sin información'}</dd>
      <dt>
        <g:if test="${businessEntity.type == BusinessEntityType.MORAL}">Razón Social</g:if>
        <g:if test="${businessEntity.type == BusinessEntityType.FISICA}">Nombre</g:if>
      </dt>
      <dd>
        <g:render template="businessName" model="[businessEntity: businessEntity, relation: relation]" />
      </dd>
      <dt>Estatus</dt>
      <dd><g:message code="businessEntity.status.${businessEntity.status}"/></dd>
    </dl>

  </div>
  <div class="panel-footer">
    <div class="row">
      <div class="col-md-12">
        <g:if test="${relation=='CLIENTE' || relation=='CLIENTE_PROVEEDOR'}">
        <div class="col-md-6">
          <div class="property-value">
            <span class="property-label"><g:message code="businessEntity.label.stpAccount" default="Cuenta para pago referenciado"/></span>

          </div>
        </div>
        <div class="col-md-6" align="right">
          <div class="property-value">
            <g:if test="${clientData?.clientLink?.stpClabe}">
            ${clientData?.clientLink?.stpClabe}
            </g:if>
            <g:else>
            <g:link class="btn btn-default" action="generateSubAccountStp" id="${businessEntity.id}" >
            <g:message code="businessEntity.label.createSubAccount" default="Generar cuenta"/>
            </g:link>
            </g:else>
          </div>
        </div>
        </g:if>
      </div>
    </div>
  </div>
</div>
