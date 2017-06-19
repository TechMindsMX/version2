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
          <div>
            <span><h4><font color="red">* Totales del mes actual</font></h4></span>
          </div>
          <div class="table-responsive">
            <table class="table">
              <thead>
              <tr>
                <th>Nombre</th>
                <th>RFC</th>
                <th>Por facturar*</th>
                <th></th>
                <th></th>
              </tr>
              </thead>
              <tbody>
              <g:each in="${companies}" var="company" >
              <tr>
                <td>${company.toString()}</td>
                <td>${company.rfc}</td>
                <td class="text-right">
                  <g:link controller="commission" action="listPendingCommissions" id="${company.id}" params="[corporateId:corporate.id]">
                    ${modulusuno.formatPrice(number:(totalPendingCommissions.find {it.company == company}).total)}
                  </g:link>
                </td>
                <td>
                  <g:if test="${company.commissions}">
                  <g:link controller="commission" class="btn btn-success" params="[companyId: company.id]" >Editar</g:link>
                  </g:if>
                  <g:else>
                  <g:link controller="commission" class="btn btn-warning" params="[companyId: company.id]">Agregar</g:link>
                  </g:else>
                </td>
                <td>
                  <g:link class="btn btn-info" controller="commissionsInvoice" action="conciliate" id="${company.id}" params="[corporateId:corporate.id]">Conciliar cobros</g:link>
                </td>
              </tr>
              </g:each>
              </tbody>
              <tfoot>
                <tr>
                  <td></td>
                  <td><strong>Totales:</strong></td>
                  <td class="text-right"><strong>${modulusuno.formatPrice(number:totalPendingCommissions*.total.sum())}</strong></td>
                  <td></td>
                  <td></td>
                </tr>
              </tfoot>
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
        <div class="col-md-2 col-md-offset-10">
          <g:link class="btn btn-primary" controller="dashboard" action="index">Regresar</g:link>
        </div>
      </div>
    </sec:ifAnyGranted>
  </body>
</html>
