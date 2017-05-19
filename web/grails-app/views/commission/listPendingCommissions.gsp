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
        Comisiones Pendientes de Cobro
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
                    <th class="text-center">Tipo</th>
                    <th class="text-center">Monto</th>
                  </tr>
                  </thead>
                  <tbody>
                  <g:each in="${commissionsBalance}" var="commission">
                  <tr>
                    <td>${commission.typeCommission}</td>
                    <td class="text-right">${modulusuno.formatPrice(number:commission.balance)}</td>
                  </tr>
                  </g:each>
                  </tbody>
                  <tfoot>
                    <tr>
                      <td><strong>Subtotal:</strong></td>
                      <td class="text-right"><strong>${modulusuno.formatPrice(number:commissionsBalance*.balance.sum())}</strong></td>
                    </tr>
                    <tr>
                      <td><strong>IVA:</strong></td>
                      <td class="text-right"><strong>${modulusuno.formatPrice(number:commissionsBalance*.iva.sum())}</strong></td>
                    </tr>
                    <tr>
                      <td><strong>Total:</strong></td>
                      <td class="text-right"><strong>${modulusuno.formatPrice(number:commissionsBalance*.total.sum())}</strong></td>
                    </tr>

                    <tr>
                      <td></td>
                      <td class="text-right">
                        <g:if test="${commissionsBalance*.balance.sum()}">
                        <g:link class="btn btn-success" controller="commissionsInvoice" action="createCommissionsInvoice" id="${company.id}" params="[corporateId:corporate.id]">Facturar</g:link>
                        </g:if>
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
