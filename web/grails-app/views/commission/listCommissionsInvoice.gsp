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
        Facturas de Comisiones
        <small>${company.bussinessName}</small>
      </h1>
    </div>

    <div class="row">
      <div class="col-lg-6 col-lg-offset-3">
        <div class="portlet portlet-blue">
          <div class="portlet-heading">
          </div>
          <div id="bluePortlet" class="panel-collapse collapse in">
            <div class="portlet-body">

              <div class="table-responsive">
                <table class="table">
                  <thead>
                  <tr>
                    <th class="text-center">Fecha</th>
                    <th class="text-center">Total</th>
                    <th class="text-center">Estatus</th>
                  </tr>
                  </thead>
                  <tbody>
                  <g:each in="${invoices.listInvoices}" var="invoice">
                  <tr>
                    <td><g:formatDate format="dd-MM-yyyy" date="${invoice.dateCreated}"/> </td>
                    <td class="text-right">${modulusuno.formatPrice(number:invoice.commissions*.amount.sum())}</td>
                    <td><g:message code="commissions.invoice.status.${invoice.status}"/></td>
                  </tr>
                  </g:each>
                  </tbody>
                  <tfoot>
                    <tr>
                      <td><strong>Total:</strong></td>
                      <td class="text-right"><strong>${modulusuno.formatPrice(number:commissionsBalance*.balance.sum())}</strong></td>
                    </tr>
                    <tr>
                      <td></td>
                      <td class="text-right">
                        <g:link class="btn btn-success" action="createCommissionsInvoice" id="${company.id}" params="[corporateId:corporate.id]">Facturar</g:link>
                      </td>
                    </tr>
                  </tfoot>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-md-2 col-md-offset-10">
        <g:link class="btn btn-primary" controller="corporate" action="commissions" id="${corporate.id}">Regresar</g:link>
      </div>
    </div>

  </body>
</html>
