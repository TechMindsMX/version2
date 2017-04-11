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
        ${corporate.nameCorporate}
        <small>Administrar Comisiones</small>
      </h1>
    </div>

    <sec:ifAnyGranted roles="ROLE_M1">
      <div class="row">
        <div class="col-md-12 col-lg-12">
          <g:if test="${!companies.isEmpty()}">
          <div class="table-responsive">
            <table class="table">
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
          </div>
          </g:if>
          <g:else>
            <div class="content scaffold-list">
              <div class="alert alert-warning">Ninguna Empresa ha sido creada</div>
            </div>
          </g:else>
        </div>
      </div>
      <div class="row">
        <div class="col-md-3 col-md-offset-9">
          <g:link class="btn btn-primary" controller="dashboard" action="index">Regresar</g:link>
        </div>
      </div>
    </sec:ifAnyGranted>
  </body>
</html>
