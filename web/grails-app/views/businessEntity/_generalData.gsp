<%! import com.modulus.uno.BusinessEntityType %>
<%! import com.modulus.uno.BusinessEntityStatus %>
<div class="panel panel-default">
  <div class="panel-heading">
    <div class="row">
      <div class="col-md-12">
        <div class="col-md-6">
          <h4>Datos Generales</h4>
        </div>
        <div class="col-md-6">
          <fieldset class="buttons">
            <sec:ifAnyGranted roles="ROLE_AUTHORIZER_EJECUTOR">
              <g:if test="${businessEntity.status == BusinessEntityStatus.ACTIVE}">
                <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#modalConfirm">Dar de baja</button>
                <div class="modal fade" id="modalConfirm" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                  <div class="modal-dialog" role="document">
                    <div class="modal-content">
                      <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel">Confirme la acción</h4>
                      </div>
                      <div class="modal-body">
                        ¿Está seguro en dar de baja la relación comercial?
                      </div>
                      <div class="modal-footer">
                        <g:link class="edit btn btn-danger" action="inactive" resource="${this.businessEntity}">Sí</g:link>
                        <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
                      </div>
                    </div>
                  </div>
                </div>

              </g:if>
              <g:if test="${businessEntity.status == BusinessEntityStatus.TO_AUTHORIZE || businessEntity.status == BusinessEntityStatus.INACTIVE}">
                <g:link class="edit btn btn-primary" action="authorize" resource="${this.businessEntity}">Autorizar</g:link>
              </g:if>
            </sec:ifAnyGranted>
            <g:link class="btn btn-primary" action="edit" id="${businessEntity.id}">Editar</g:link>
          </fieldset>
        </div>
      </div>
    </div>
  </div>

  <div class="panel-body">
    <g:if test="${flash.message}">
    <div class="alert alert-success" role="alert">${flash.message} - ${relation}
    </div>
    </g:if>

    <dl class="dl">
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
      <dt>Correo Electrónico</dt>
      <dd>${businessEntity.email ?: 'No registrado'}</dd>
      <dt>Estatus</dt>
      <dd><g:message code="businessEntity.status.${businessEntity.status}"/></dd>
    </dl>

  </div>
  <div class="panel-footer">
    <div class="row">
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
              <sec:ifAnyGranted roles="ROLE_AUTHORIZER_EJECUTOR">
                <g:if test="${businessEntity.status == BusinessEntityStatus.ACTIVE}">
                  <g:link class="btn btn-default" action="generateSubAccountStp" id="${businessEntity.id}" >
                    <g:message code="businessEntity.label.createSubAccount" default="Generar cuenta"/>
                  </g:link>
                </g:if>
              </sec:ifAnyGranted>
            </g:else>
          </div>
        </div>
        </g:if>
    </div>
  </div>
</div>
