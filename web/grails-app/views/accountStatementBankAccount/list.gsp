<!DOCTYPE html>
<html>
<head>
  <meta name="layout" content="main" />
  <title>Estados de Cuenta Bancario</title>
</head>
<body>
  <div class="page-title">
    <h1>
      <i class="fa fa-file-text fa-3x"></i>
      ${bankAccount}<small>Listado de Estados de Cuenta</small>
    </h1>
  </div>

  <div class="row">
    <div class="col-md-12">
      <div class="portlet">

        <div class="portlet-body">
          <div class="table-responsive">
            <table class="table">
              <thead>
                <tr>
                  <th class="text-center">Mes-Año</th>
                  <th class="text-center">Documento</th>
                </tr>
              </thead>
              <tbody>
                <g:each in="${accountStatements}" var="accountStatement">
                  <tr>
                    <td><g:dateFormat format="MM-yyyy" date="${accountStatement.month}"/></td>
                    <td>${accountStatement.document}</td>
                  </tr>
                </g:each>
              </tbody>
            </table>
          </div>
        </div>

      </div>
    </div>
  </div>

  <div class="row">
    <div class="col-md-12 text-right">
      <g:link class="btn btn-default" controller="movimientosBancarios" action="index">Regresar</g:link>
    </div>
  </div>


</body>
</html>
