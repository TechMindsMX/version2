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
      <div class="col-lg-8 col-lg-offset-2">
        <div class="portlet portlet-blue">
          <div class="portlet-heading">
            <div class="table-responsive">
              <g:form action="listPendingCommissions" id="${company.id}" params="[corporateId:corporate.id]">
              <table class="table">
                <thead>
                  <tr>
                    <th>Desde:</th>
                    <th>Hasta:</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td class="text-center">
                      <g:datePicker id="startDate" name="startDate" value="${period.init}" precision="day" years="${2016..new Date()[Calendar.YEAR]}" required=""/>
                    </td>
                    <td class="text-center">
                      <g:datePicker id="endDate" name="endDate" value="${period.end}" precision="day" years="${2016..new Date()[Calendar.YEAR]}" required=""/>
                    </td>
                  </tr>
                </tbody>
                <tfoot>
                  <tr>
                    <td></td>
                    <td class="text-right">
                      <button class="btn btn-default">Consultar</button>
                    </td>
                  </tr>
                </tfoot>
              </table>
              </g:form>
            </div>
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
                        <g:link class="btn btn-success" controller="saleOrder" action="createCommissionsInvoice" id="${company.id}" params="[corporateId:corporate.id, startDate:period.init.format('dd-MM-yyyy'), endDate:period.end.format('dd-MM-yyyy')]">Facturar</g:link>
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
