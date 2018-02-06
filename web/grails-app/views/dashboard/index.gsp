<!doctype html>
<html>
  <head>
    <title>Modulus UNO | Servicios Financieros</title>
    <meta name="layout" content="main">
  </head>
  <body>

    <sec:ifAnyGranted roles="ROLE_M1">
      <div class="page-title">
        <h1>
        <i class="fa fa-building fa-3x"></i>
          Corporativos
          <small>Listado</small>
        </h1>
      </div>


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
              <div class="portlet portlet-blue">
                <div class="portlet-body">
                  <g:render template="corporatesList"/>
                </div>
              </div>
            </div>
          </g:else>
        </div>
      </div>


    </sec:ifAnyGranted>

    <sec:ifAnyGranted roles="ROLE_CORPORATIVE">
      <g:if test="${session.corporate}">
      <div class="page-title">
        <h1>
        <i class="fa fa-building fa-3x"></i>
          Empresas
          <small>Listado</small>
        </h1>
      </div>
      </g:if>


    <div class="row">
      <div class="col-md-12 text-center">
        <!-- verificar las companies del corporate -->
        <g:if test="${companies.isEmpty() && session.corporate}">
          <div class="alert alert-info">
            <strong>Atención:</strong> Vemos que aún no tienes empresas registradas en Modulus UNO, comienza...
          </div>
          <p>
          <g:link class="btn btn-default btn-lg" controller="corporate" action="addCompany" id="${session.corporate?.id}">
            Registra tu primera empresa
          </g:link>
          </p>
        </g:if>

        <g:if test="${!session.corporate}">
          <div class="alert alert-info">
            Tu cuenta se activó exitosamente
          </div>
          <p>
          <g:link class="btn btn-default btn-lg" controller="dashboard" action="index">
            Iniciar
          </g:link>
          </p>
        </g:if>
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
