<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'commission.label', default: 'Commission')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-info-circle fa-3x"></i>
        Comisiones Fijas de la Empresa
        <small>${company.bussinessName}</small>
      </h1>
    </div>

    <div class="row">
      <div class="col-lg-12">
        <div class="portlet portlet-blue">
          <div class="portlet-heading">
          </div>
          <div id="bluePortlet" class="panel-collapse collapse in">
            <div class="portlet-body">

              <div class="table-responsive">
                <table class="table">
                  <tr>
                    <th>Fecha</th>
                    <th>Monto</th>
                    <th>Estatus</th>
                  </tr>
                  <g:each in="${fixedCommissions.listFixedCommissions}" var="commission">
                  <tr>
                    <td><g:formatDate format="dd-MM-yyyy" date="${commission.dateCreated}"/></td>
                    <td>${modulusuno.formatPrice(number:commission.amount)}</td>
                    <td>${commission.status}</td>
                  </tr>
                  </g:each>
                </table>
              </div>
              <div class="pagination">
                <g:paginate total="${fixedCommissions.countFixedCommissions ?: 0}" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

  </body>
</html>
