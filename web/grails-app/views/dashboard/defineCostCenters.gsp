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
        Definición de Centros de Costos
        <small>Compañías Registradas</small>
      </h1>
    </div>

    <sec:ifAnyGranted roles="ROLE_M1">
      <div class="row">
        <div class="col-md-12 col-lg-12">
          <div class="table-responsive">
          <table class="table table-condensed table-hover">
            <thead>
            <tr>
              <th>Nombre</th>
              <th>RFC</th>
              <th>Clabe STP</th>
              <th>Alias CC</th>
            </tr>
            </thead>
            <tbody>
            <g:each in="${companies.sort{it.bussinessName}}" var="company">
            <tr>
              <td>${company}</td>
              <td>${company.rfc}</td>
              <td>${company.accounts ? company.accounts[0].stpClabe : "Sin cuenta STP"}</td>
              <td>
                <g:if test="${company.accounts}">
                <button type="button" class="btn btn-link" data-toggle="modal" data-target="#editAliasStp" data-whatever="${company.id}|${company}|${company.accounts[0].aliasStp}">
                  ${company.accounts[0].aliasStp ?: "No registrado"}
                </button>
                </g:if>
                <g:else> Sin cuenta STP </g:else>
              </td>
            </tr>
            </g:each>
            </tbody>
          </table>
          </div>

          <!-- modal edit alias stp -->
          <div class="modal fade" id="editAliasStp" tabindex="-1" role="dialog" aria-labelledby="editAliasStpLabel">
            <div class="modal-dialog" role="document">
              <div class="modal-content">
                <div class="modal-header">
                  <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                  <h4 class="modal-title" id="editAliasStpLabel">Editar Alias de Centro de Costos</h4>
                </div>
                <g:form action="saveAliasStp">
                <input type="hidden" id="company" name="company"/>
                <div class="modal-body">
                  <div class="form-group">
                    <label for="companyName" class="control-label">Empresa:</label>
                    <input type="text" class="form-control" id="companyName" name="companyName" readonly="">
                  </div>
                  <div class="form-group">
                    <label for="aliasStp" class="control-label">Alias STP:</label>
                    <input type="text" class="form-control" id="aliasStp" name="aliasStp" required="required">
                  </div>
                </div>
                <div class="modal-footer">
                  <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                  <button type="submit" class="btn btn-primary">Guardar</button>
                </div>
                </g:form>
              </div>
            </div>
          </div>
          <!-- modal edit alias stp -->

          <div class="pagination">
            <g:paginate controller="dashboard" action="defineCostCenters" total="${companiesCount}" />
          </div>
        </div>
      </div>
    </sec:ifAnyGranted>
    <asset:javascript src="dashboard/defineCostCenters.js"/>
  </body>
</html>
