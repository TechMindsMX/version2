<%! import com.modulus.uno.CommissionsInvoiceStatus %>
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
        Factura de Comisiones
        <small>${invoice.receiver.bussinessName}</small>
      </h1>
    </div>

    <div class="row">
      <div class="col-lg-9 col-lg-offset-1">
        <div class="portlet portlet-blue">
          <div class="portlet-heading">
          </div>
          <div id="bluePortlet" class="panel-collapse collapse in">
            <div class="portlet-body">

              <div class="row">
                <div class="col-md-4 text-center"><strong>Fecha</strong></div>
                <div class="col-md-4 text-center"><strong>Total</strong></div>
                <div class="col-md-4 text-center"><strong>Estatus</strong></div>
              </div>
              <div class="row">
                <div class="col-md-4 text-center"><g:formatDate format="dd-MM-yyyy" date="${invoice.dateCreated}"/></div>
                <div class="col-md-4 text-center">${modulusuno.formatPrice(number:invoice.commissions*.amount.sum())}</div>
                <div class="col-md-4 text-center"><g:message code="commissions.invoice.status.${invoice.status}"/></div>
              </div>
              </br>
              <div class="row">
                <div class="col-md-12"><strong>Detalle</strong></div>
              </div>
              <div class="row">
                <div class="col-md-12">
                  <div class="table-responsive">
                    <table class="table">
                      <thead>
                      <tr>
                        <th class="text-center">Tipo</th>
                        <th class="text-center">Monto</th>
                      </tr>
                      </thead>
                      <tbody>
                      <g:each in="${commissionsSummary.sort{it.type.toString()}}" var="totalType">
                      <tr>
                        <td class="text-center">${totalType.type}</td>
                        <td class="text-right">${modulusuno.formatPrice(number:totalType.total)}</td>
                      </tr>
                      </g:each>
                      </tbody>
                    </table>
                  </div>
                </div>
              </div>

            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-md-2 col-md-offset-10">
        <g:link class="btn btn-primary" action="listCommissionsInvoice" id="${invoice.receiver.id}" params="[corporateId:corporate.id]">Regresar</g:link>
      </div>
    </div>

  </body>
</html>
