<%! import com.modulus.uno.CompanyStatus %>
<%! import com.modulus.uno.CompanyTaxRegime %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'company.label', default: 'Company')}" />
    <asset:stylesheet src="company/show.css" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="row">
      <!-- BEGIN PAGE TITLE -->
      <div class="page-title">
        <h1>
          <i class="fa fa-info-circle fa-3x"></i>
          <g:message code="company.show"/>
        </h1>
      </div>
      <!-- END OF PAGE TITLE -->
    </div>

    <div class="row">
      <div class="alert alert-info" role="alert">
        <b><g:message code="company.status.message.${company.status}" /></b>
      </div>

      <div class="col-md-6">
        <div class="portlet portlet-default">
          <div class="portlet-heading">
            <div class="portlet-title">
              <h4>Datos de la Empresa</h4>
            </div>
            <div class="clearfix"></div>
          </div>
          <div id="defaultPortlet" class="panel-collapse collapse in">
            <div class="portlet-body">
              <form class="form-horizontal" role="form">
                <div class="form-group">
                  <label id="taxRegime-label" class="col-sm-12 control-label">
                    <g:message code="company.taxRegime"/>
                  </label>
                  <div class="col-sm-12"><g:message code="${company.taxRegime}"/></div>
                </div>
                <f:display bean="company" property="bussinessName" wrapper="describe" /></b>
              <div class="form-group">
                <label class="col-sm-12 control-label">Alias</label>
                <div class="col-sm-12">
                  ${company.aliasCompany}
                </div>
              </div>
              <f:display bean="company" property="webSite"  wrapper="describe" />
              <f:display bean="company" property="employeeNumbers" wrapper="describe" />
              <f:display bean="company" property="numberOfAuthorizations" wrapper="describe" />
              <div class="form-group">
                <label id="grossAnnualBilling-label" class="col-sm-12 control-label">Facturacion Anual Bruta</label>
                <div class="col-sm-12">
                  ${modulusuno.formatPrice(number:company.grossAnnualBilling)}
                </div>
              </div>
              <g:if test="${balance != null}">
              <div class="form-group">
                <label id="grossAnnualBilling-label" class="col-sm-12 control-label">Balance de la Compañia en MXN</label>
                <div class="col-sm-12">
                  ${modulusuno.formatPrice(number:balance)}
                </div>
              </div>
              </g:if>
              <g:if test="${usd != null}">
              <div class="form-group">
                <label id="grossAnnualBilling-label" class="col-sm-12 control-label">Balance de la Compañia en USD</label>
                <div class="col-sm-12">
                  ${modulusuno.formatPrice(number:usd)}
                </div>
              </div>
              </g:if>
              <g:if test="${company.accounts}">
              <div class="form-group">
                <label id="grossAnnualBilling-label" class="col-sm-12 control-label">Cuenta STP</label>
                <div class="col-sm-12">
                  ${company.accounts.first().stpClabe}
                </div>
              </div>
              </g:if>
              <f:display bean="company" property="rfc" wrapper="describe" />
              <div class="form-group">
                <label id="grossAnnualBilling-label" class="col-sm-12 control-label">Día de la primera quincena de pago</label>
                <div class="col-sm-12">
                  ${company.firstPaymentDay}
                </div>
              </div>
              <div class="form-group">
                <label id="grossAnnualBilling-label" class="col-sm-12 control-label">Día de la segunda quincena de pago</label>
                <div class="col-sm-12">
                  ${company.secondPaymentDay}
                </div>
              </div>
              </form>

              <div class="property-value">
                <g:if test="${!company.accounts}" >
                <g:link controller="company" action="crearCuentaSTP" id="${company.id}" class="btn btn-danger btn-block">Generar Cuenta STP</g:link>
                </g:if>
              </div>
            </div>
          </div>
          <div class="portlet-footer">
            <sec:ifAnyGranted roles="ROLE_CORPORATIVE">
            <div class="text-right">
              <g:link class="btn btn-default" action="edit" resource="${this.company}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
            </div>
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_M1">
            <div class="text-right">
              <g:link class="btn btn-default" controller="commission" action="index" params="[companyId:company.id]">Listar comisiones</g:link>
            </div>
            </sec:ifAnyGranted>
          </div>
        </div>

        <!-- Información de contacto -->
        <div class="portlet portlet-default">
          <div class="portlet-heading">
            <div class="portlet-title">
              <h4>Información de contacto</h4>
            </div>
            <div class="clearfix"></div>
          </div>

          <div id="defaultPortlet" class="panel-collapse collapse in">
            <div class="portlet-body">
              <div class="portlet-body">
                <g:if test="${!company.telephones}">
                <h4><span class="label label-warning">Debe agregar al menos un Telefono</span></h4>
                </g:if>
                <g:render template="contactInformation" />
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- Cuentas -->
      <div class="col-md-6">
        <div class="">
          <!-- BEGIN PORTLET -->
          <div class="portlet portlet-blue">
            <!-- BEGIN PORTLET-HEADING -->
            <div class="portlet-heading">
              <div class="portlet-title">
                <h4>Cuentas Bancarias</h4>
              </div>
              <div class="clearfix"></div>
            </div>
            <!-- END OF PORTLET-HEADING -->

            <!-- BEGIN PANEL-COLLAPSE -->
            <div id="defaultPortlet" class="panel-collapse collapse in">
              <!-- BEGIN PORTLET-BODY -->
              <div class="portlet-body">
                <g:if test="${!company.banksAccounts}">
                <h4><span class="label label-warning">Debe agregar al menos una cuenta</span></h4>
                </g:if>
                <g:render template="bankAccounts" />
              </div>
              <!-- END OF PORTLET BODY -->
            </div>
            <!-- END OF PANEL-COLLAPSE-->
            <div class="portlet-footer"></div>
          </div>
          <!-- END OF PORTLET -->
        </div>

        <!-- DOCUMENTS -->
        <g:if test="${company.status == CompanyStatus.ACCEPTED}">
        <g:render template="documents" />
        </g:if>

        <!-- ADDRESSES -->
        <div class="">
          <!-- BEGIN PORTLET -->
          <div class="portlet portlet-blue">
            <!-- BEGIN PORTLET-HEADING -->
            <div class="portlet-heading">
              <!-- BEGIN PORTLET-TITLE -->
              <div class="portlet-title">
                <h4>Direcciones</h4>
              </div>
              <!-- END OF PORTLET-TITLE -->
              <div class="clearfix"></div>
            </div>
            <!-- END OF PORTLET -->

            <!-- BEGIN PANEL COLLAPSE -->
            <div class="panel-collapse collapse in">
              <div class="portlet-body">
                <g:if test="${!company.addresses}">
                <h4><span class="label label-warning">Debe agregar al menos una dirección</span></h4>
                </g:if>
                <g:render template="addresses" />
              </div>
            </div>
            <!-- END OF PANEL COLLAPSE -->
            <div class="portlet-footer"></div>
          </div>
          <!--END OF PORTLET -->
        </div>

        <!-- Telefonos -->
        <div class="">
          <!-- BEGIN PORTLET -->
          <div class="portlet portlet-blue">
            <div class="portlet-heading">
              <div class="portlet-title">
                <h4>Teléfonos</h4>
              </div>
              <div class="clearfix"></div>
            </div>
            <div id="defaultPortlet" class="panel-collapse collapse in">
              <div class="portlet-body">
                <g:if test="${!company.telephones}">
                <h4><span class="label label-warning">Debe agregar al menos un Telefono</span></h4>
                </g:if>
                <g:render template="telephones" />
              </div>
            </div>
            <div class="portlet-footer"></div>
          </div>
          <!-- END OF PORTLET -->
        </div>
      </div>
    </div>
    </div>
  </body>
</html>
