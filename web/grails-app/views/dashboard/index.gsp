<!doctype html>
<html>
  <head>
    <title>Modulus UNO | Servicios Financieros</title>
    <meta name="layout" content="main">
  </head>
  <body>

    <div class="page-title">
      <h1>
      <i class="fa fa-tachometer fa-3x"></i>
        Tablero principal
        <p class="small">Administración total</p>
      </h1>
      <ol class="breadcrumb">
        <li><i class="fa fa-dashboard"></i>  <a href="index.html">Tablero principal</a>
        </li>
        <li class="active">Bienvenido, puedes empezar por aquí... </li>
      </ol>
    </div>

    <sec:ifAnyGranted roles="ROLE_M1">
      <div class="row">
        <div class="col-md-12 col-lg-12">
          <g:if test="${!corporates}">
            <div class="alert alert-info">
              <strong>Atención</strong> Vemos que aún no tienes corporaciones registradas en Modulus UNO, comienza...
            </div>
            <p>
              <g:link class="btn btn-default btn-lg" controller="corporate" action="create">
                Registra tu primera corporación
              </g:link>
            </p>
          </g:if>
          <g:else>
            <div class="content scaffold-list">
              <h1><g:message code="corporate.list.label" /></h1>
              <f:table collection="${corporates}" properties="['nameCorporate','corporateUrl']" />
            </div>
          </g:else>
        </div>
      </div>


    </sec:ifAnyGranted>

    <sec:ifAnyGranted roles="ROLE_CORPORATIVE">
    <div class="row">
      <div class="col-md-12">
        <!-- verificar las companies del corporate -->
        <g:if test="${companies.isEmpty()}">
        <div class="alert alert-info">
          <strong>Atención:</strong> Vemos que aún no tienes empresas registradas en Modulus UNO, comienza...
        </div>
        <p>
        <g:link class="btn btn-default btn-lg" controller="corporate" action="addCompany" id="${session.corporate.id}">
          Registra tu primera empresa
        </g:link>
        </g:if>
        </p>
      </div>
    </div>


    <g:if test="${!companies.isEmpty()}">
    <div align="center">
      <p class="text-primary">Estas son las empresas que tienes disponibles, selecciona una...</p>
    </div>

    <div id="list-company" class=" portlet-blue" role="main">
      <g:render template="/managerApplication/companyList" model="[companies:companies]"/>
    </div>
    </g:if>
    </sec:ifAnyGranted>
    <sec:ifAnyGranted  roles="ROLE_AUTHORIZER_VISOR, ROLE_AUTHORIZER_EJECUTOR">
      <div class="row">
        <div class="col-sm-12 text-center">
          <h2>Sección para Operaciones de Modulus UNO</h2>
          <img src="${assetPath(src:'modulus_uno.svg')}" alt="MODULUS UNO">
        </div>
      </div>
      
    </sec:ifAnyGranted>

  </body>
</html>
