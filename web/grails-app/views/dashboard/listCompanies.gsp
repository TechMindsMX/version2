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
        <small>Administración total</small>
      </h1>
      <ol class="breadcrumb">
        <li><i class="fa fa-dashboard"></i>  <a href="index.html">Tablero principal</a>
        </li>
        <li class="active">Compañias existentes </li>
      </ol>
    </div>

    <sec:ifAnyGranted roles="ROLE_M1">
      <div class="row">
        <div class="col-md-12 col-lg-12">
          <g:if test="${!companies.isEmpty()}">
            <table class="table table-hover">
              <tr>
                <th>Nombre</th>
                <th>RFC</th>
                <th>Comisiones</th>
              </tr>
              <g:each in="${companies}" var="company" >
              <tr>
                <td>${company.toString()}</td>
                <td>${company.rfc}</td>
                <td>
                  <g:if test="${company.commissions}">
                  <g:link controller="commission" class="btn btn-success" params="[companyId: company.id]" >Editar</g:link>
                  </g:if>
                  <g:else>
                  <g:link controller="commission" class="btn btn-warning" params="[companyId: company.id]">Agregar</g:link>
                  </g:else>
                </td>
              </tr>
              </g:each>
            </table>
            <div class="pagination">
              <g:paginate controller="dashboard" action="listCompanies" total="${companiesCount}" />
            </div>
          </g:if>
          <g:else>
            <div class="content scaffold-list">
              <h1>Ninguna Empresa ha sido Creada</h1>
            </div>
          </g:else>
        </div>
      </div>
    </sec:ifAnyGranted>
  </body>
</html>
