<!DOCTYPE html>
<%! import com.modulus.uno.BusinessEntityType %>
<%! import com.modulus.uno.NameType %>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'businessEntity.label', default: 'BusinessEntity')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="container" style="width:95%">

      <div class="page-title">
        <h1>
          <i class="fa fa-user fa-3x"></i>
          <g:message code="businessEntity.view.show.label" />
        </h1>
      </div>

      <div id="edit-address" class="content scaffold-edit" role="main">



        <div id="horizontalFormExample" class="panel-collapse collapse in">

          <div clas="row">
            <div clas="col-md-12">
              <div class="col-md-6">

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
                    <ol class="property-list businessEntity">
                      <f:display bean="businessEntity" property="rfc" wrapper="show"/>
                      <g:if test="${!relation.equals('EMPLEADO')}">
                        <f:display bean="businessEntity" property="website" wrapper="show"/>
                      </g:if>

                      <g:if test="${businessEntity.type == BusinessEntityType.FISICA}">
                        <g:each var="name" in="${businessEntity.names}">
                          <g:if test="${name.type == NameType.NOMBRE}">
                            <li class="fieldcontain">
                              <span id="name-label" class="property-label">Nombre</span>
                              <div class="property-value" aria-labelledby="name-label">
                                ${name.value}
                              </div>
                            </li>
                          </g:if>
                        </g:each>
                        <g:each var="name" in="${businessEntity.names}">
                          <g:if test="${name.type == NameType.APELLIDO_PATERNO}">
                            <li class="fieldcontain">
                              <span id="lastName-label" class="property-label">Apellido Paterno</span>
                              <div class="property-value" aria-labelledby="lastName-label">
                                ${name.value}
                              </div>
                            </li>
                          </g:if>
                        </g:each>
                        <g:each var="name" in="${businessEntity.names}">
                          <g:if test="${name.type == NameType.APELLIDO_MATERNO}">
                            <li class="fieldcontain">
                              <span id="lastName-label" class="property-label">Apellido Materno</span>
                              <div class="property-value" aria-labelledby="lastName-label">
                                ${name.value}
                              </div>
                            </li>
                          </g:if>
                        </g:each>
                      </g:if>
                      <g:if test="${businessEntity.type == BusinessEntityType.MORAL}">
                        <g:each var="name" in="${businessEntity.names}">
                          <g:if test="${name.type == NameType.RAZON_SOCIAL}">
                            <li class="fieldcontain">
                              <span id="name-label" class="property-label">Razón Social</span>
                              <div class="property-value" aria-labelledby="name-label">
                                ${name.value}
                              </div>
                            </li>
                          </g:if>
                        </g:each>
                      </g:if>
                    </ol>

                  </div>
                  <div class="panel-footer">
                    <div class="row">
                      <div class="col-md-12">
                        <div class="col-md-6">
                          <g:if test="${relation=='CLIENTE' || relation=='CLIENTE_PROVEEDOR'}">
                            <div class="property-value">
                              <span class="property-label"><g:message code="businessEntity.label.stpAccount" default="Cuenta para pago referenciado"/></span>

                            </div>
                          </g:if>
                        </div>
                        <div class="col-md-6" align="right">
                          <div class="property-value">
                            <g:if test="${clientLink?.stpClabe}">
                              ${clientLink.stpClabe}
                            </g:if>
                            <g:else>
                              <g:link class="btn btn-default" action="generateSubAccountStp" id="${businessEntity.id}" >
                                <g:message code="businessEntity.label.createSubAccount" default="Generar cuenta"/>
                              </g:link>
                            </g:else>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="col-md-6">
                <div class="panel panel-default">
                  <div class="panel-heading">
                    <div class="row">
                      <div class="col-md-12">
                        <div class="col-md-6">
                          <span id="address-label" class="property-label"><h4>Dirección</h4></span>
                        </div>
                        <div class="col-md-6" align="right">
                          <g:if test="${!relation.equals('EMPLEADO')}">
                            <div class="property-value" aria-labelledby="company-label">
                              <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR">
                                <g:link action="create" controller="address" params="[businessEntity:businessEntity.id]" class="btn btn-default">Agregar Dirección</g:link>
                              </sec:ifAnyGranted>
                            </div>
                          </g:if>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="panel-body">
                    <g:if test="${businessEntity.addresses}" >

                      <div class="property-value" aria-labelledby="telefono-label">
                        <ul>
                          <g:each var="address" in="${businessEntity.addresses.sort{it.id}}">
                            <g:link controller="address" action="edit" id="${address.id}" params="[relation:relation, businessEntityId:businessEntity.id]">
                              <li class="subList">${address.addressType}: ${address.street} #${address.streetNumber} - ${address.suite} CP ${address.zipCode}, ${address.colony}, ${address.city}, ${address.town}. ${address.federalEntity}, ${address.country}</li>
                            </g:link>
                          </g:each>
                        </ul>
                      </div>
                    </g:if>


                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="row" style="margin-right: 0px; margin-left: 0px;">
            <div class="col-md-6">

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
                            <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR">
                              <g:link class="btn btn-default" action="create" controller="bankAccount" params="[businessEntity:businessEntity.id, businessEntityInfo:businessEntity.toString(), relation:relation]"><g:message code="businessEntity.label.createBankAccount.${relation}" default="Agregar Cuenta"/></g:link>
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
                        <g:link controller="bankAccount" action="edit" params="[businessEntity:businessEntity.id, businessEntityInfo:businessEntity.toString(), relation:relation]" id="${account.id}">
                          ${account.banco} -
                          ${account.accountNumber}
                          <g:if test="${relation != 'CLIENTE'}">- CLABE: ${account.clabe}</g:if>
                        </g:link>
                      </li>
                    </g:each>
                  </ul>
                </div>

              </div>
            </div>
          </div>
          <div class="col-md-6">
            <g:if test="${relation.equals('CLIENTE_PROVEEDOR')} || ${relation.equals('CLIENTE')}">
                <div class="panel panel-default">
              <div class="panel-heading">
                <div class="row">
                  <div class="col-md-12">
                    <div class="col-md-6">
                      <span class="property-label"><h4>Pagos del cliente</h4></span>
                    </div>
                    <div class="col-md-6" align="right">

                    </div>
                  </div>
                </div>

              </div>

              <div class="panel-body">

               Pagos

            </div>
          </div>
            </g:if>

        </div>
      </div>
    </div>
    </div>
  </div>
</body>
</html>
