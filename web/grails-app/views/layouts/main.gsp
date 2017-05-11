<%! import com.modulus.uno.CompanyStatus %>
<%! import com.modulus.uno.CompanyTaxRegime %>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Modulus UNO</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://fonts.googleapis.com/css?family=Montserrat:100,300,400,700,800|Lato:100,400,700" rel="stylesheet">
    <asset:stylesheet src="application.css"/>
    <asset:javascript src="application.js"/>
    <asset:javascript src="main-nav.js"/>
    <g:layoutHead/>
  </head>
  <body>
    <nav class="navbar-top" role="navigation">
      <div class="navbar-header">
      <div class="navbar-brand">
      <img src="${assetPath(src: 'modulusuno-bco.svg')}" alt="MODULUS UNO">
      <span>MODULUS UNO</span>
      </div>

        <button type="button" class="navbar-toggle pull-right" data-toggle="collapse" data-target=".sidebar-collapse">
          <i class="fa fa-bars"></i> Menu
        </button>

      </div>

      <div class="nav-top">
        <ul class="nav navbar-left">
          <li class="tooltip-sidebar-toggle">
            <a href="#" id="sidebar-toggle" data-toggle="tooltip" data-placement="right" title="Mostrar/ocultar menú">
              <i class="fa fa-bars"></i>
            </a>
          </li>
        </ul>
        <ul class="nav navbar-center">
          <li class="tooltip-sidebar-toggle" >
            <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_VISOR,ROLE_LEGAL_REPRESENTATIVE_EJECUTOR,ROLE_FICO_VISOR,ROLE_FICO_EJECUTOR,ROLE_AUTHORIZER_VISOR,ROLE_AUTHORIZER_EJECUTOR,ROLE_OPERATOR_VISOR,ROLE_OPERATOR_EJECUTOR">
              <g:if test="${session.company}">
                <g:form class="form-group" id="company-selection" url="[action:'setCompanyInSession',controller:'company']" >
                  <span>Selecciona tu Compañía ${companyInfo.selectedCompany()}
                  <button class="btn btn-primary" type="submit">CAMBIAR</button>
                  </span>
                </g:form>
              </g:if>
            </sec:ifAnyGranted>
          </li>
          <li>
            &nbsp;&nbsp;&nbsp;&nbsp;
          </li>
          <li align="right">
          </li>
        </ul>


        <ul class="nav navbar-right">

          <li class="dropdown">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
              <i class="fa fa-user"></i>  <i class="fa fa-caret-down"></i>
            </a>

            <ul class="dropdown-menu dropdown-user">
              <li>
                <g:link controller="user" action="profile" id="${sec.loggedInUserInfo(field: "id")}">
                  <i class="fa fa-user"></i> Mi Perfil
                </g:link>
              </li>
              <li class="divider"></li>
              <li>
                <g:link controller="logout" action="index" class="logout_open">
                  <i class="fa fa-sign-out"></i> Cerrar sesión
                </g:link>
              </li>

            </ul>
          </li>
        </ul>
        <ul class="nav navbar-brand navbar-right">
          <sec:ifLoggedIn>
          <font color="white"> Hola: ${modulusuno.userLoggin()}</font>
          </sec:ifLoggedIn>
        </ul>
      </div>
    </nav>

    <nav class="navbar-side" role="navigation">
      <div class="navbar-collapse sidebar-collapse collapse">
        <ul id="side" class="nav navbar-nav side-nav">
          <sec:ifAnyGranted roles="ROLE_M1">
            <li class="panel">
              <a href="javascript:;" data-parent="#side" data-toggle="collapse" class="accordion-toggle" data-target="#corporativos">
                Corporativos <i class="fa fa-caret-down"></i>
              </a>
              <ul class="collapse nav" id="corporativos">
                <li>
                  <g:link controller="dashboard" action="index" >Lista de Corporativos</g:link>
                  <g:link controller="corporate" action="create" >Crear Nuevo Corporativo</g:link>
                </li>
              </ul>
            </li>
          </sec:ifAnyGranted>

          <sec:ifAnyGranted roles="ROLE_CORPORATIVE">
            <g:if test="${session.corporate}">
              <li><g:link controller="corporate" action="addCompany" id="${session.corporate.id}">Crear Nueva Empresa</g:link></li>
              <li><g:link controller="corporate" action="addUser" id="${session.corporate.id}">Alta Usuario</g:link></li>
              <li><g:link controller="corporate" action="users" id="${session.corporate.id}">Lista de Usuarios</g:link></li>
              <li><g:link controller="corporate" action="companies" id="${session.corporate.id}">Todas las Empresas</g:link></li>
              <li><g:link controller="machine" action="index">Lista de Máquinas</g:link></li>
              <li><g:link controller="machine" action="register">Crear nueva máquina</g:link></li>
              <li><g:link controller="notificationForState" action="index">Notificaciones</g:link></li>
            </g:if>
          </sec:ifAnyGranted>

          <g:if test="${session.company && companyInfo.isAvailableForOperationInThisCompany()}">
            <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_VISOR,ROLE_LEGAL_REPRESENTATIVE_EJECUTOR">
              <g:render template="/layouts/representante_legal" />
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_FICO_VISOR,ROLE_FICO_EJECUTOR">
              <g:render template="/layouts/fico" />
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_OPERATOR_VISOR,ROLE_OPERATOR_EJECUTOR">
              <g:render template="/layouts/operador" />
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_AUTHORIZER_VISOR, ROLE_AUTHORIZER_EJECUTOR">
              <g:render template="/layouts/authorizer" />
            </sec:ifAnyGranted>

          </g:if>
          <li><g:link controller="logout" action="index"><i class="fa fa-sign-out"></i> Cerrar sesión</g:link></li>
        </ul>
      </div>
    </nav>
    <div id="page-wrapper">
      <div class="page-content page-content-ease-in">
        <div class="row">
          <div class="col-lg-12">
            <g:layoutBody/>
          </div>
        </div>
      </div>
    </div>

    <div class="footer" role="contentinfo"></div>
    <div id="spinner" class="spinner" style="display:none;">
      <g:message code="spinner.alt" default="Loading&hellip;"/>
    </div>

  </body>
</html>
