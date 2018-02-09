<!DOCTYPE html>
<%! import java.text.SimpleDateFormat %>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'accountStatement.label', default: 'AccountStatement')}" />
    <title><g:message code="manager.pastDuePortfolio.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
      <i class="fa fa-money fa-3x"></i>
      <g:message code="manager.pastDuePortfolio.label"/>
      </h1>
      <ol class="breadcrumb">
        <li><i class="fa fa-caret-square-o-up"></i>Modulus UNO</li>
        <li class="active"><g:message code="manager.pastDuePortfolio.label"/></li>
      </ol>
      <div class="portlet portlet-blue">
        <div class="portlet-heading">Cartera Vencida</div>
        <div class="portlet-body">
          <div class="container-fluid">
          <div class="row">
            <div class="col-md-3 text-center">
              <g:link class="btn btn-info" action="pastDuePortfolioForDays" params="[days:30]">30 días</g:link>
            </div>
            <div class="col-md-3 text-center">
              <g:link class="btn btn-warning" action="pastDuePortfolioForDays" params="[days:60]">60 días</g:link>
            </div>
            <div class="col-md-3 text-center">
              <g:link class="btn btn-warning" action="pastDuePortfolioForDays" params="[days:90]">90 días</g:link>
            </div>
            <div class="col-md-3 text-center">
              <g:link class="btn btn-danger" action="pastDuePortfolioForDays" params="[days:120]">120 días y más</g:link>
            </div>
            </div>
          </div>
        </div>
      </div> <!-- cabecera -->

      <g:if test="${days}">
      <div class="portlet portlet-blue">
        <div class="portlet-body">
          <div class="container-fluid">
            <div class="row">
              <div class="alert alert-${alert}">
                Cartera vencida de ${days} días ${days==120? "y más" : ""}
              </div>
            </div>

            <div class="row">
            <g:if test="${detail}">
              <div class="text-right">
                <g:link class="btn btn-default" action="pdfForPastDuePortfolio" params="[days:days]">PDF</g:link>
              </div>
              <div class="table-responsive">
                <table class="table table-condensed table-striped">
                  <thead>
                    <tr>
                      <th class="text-center">No. de Orden</th>
                      <th class="text-center">Cliente</th>
                      <th class="text-center">Fecha de Cobro</th>
                      <th class="text-center">Fecha de Vencimiento</th>
                      <th class="text-center">Estatus</th>
                      <th class="text-center">Total</th>
                    </tr>
                  </thead>
                  <tbody>
                    <g:each in="${detail.sort {it.fechaCobro}}" var="sale">
                      <tr>
                        <td class="text-center">${sale.id}</td>
                        <td>${sale.clientName}</td>
                        <td><g:formatDate format="dd-MM-yyyy" date="${sale.fechaCobro}"/></td>
                        <td><g:formatDate format="dd-MM-yyyy" date="${sale.originalDate ?: sale.fechaCobro}"/></td>
                        <td>${sale.status}</td>
                        <td class="text-right">${modulusuno.formatPrice(number: sale.total)}</td>
                      </tr>
                    </g:each>
                  </tbody>
                  <tfoot>
                    <tr>
                      <td></td>
                      <td></td>
                      <td></td>
                      <td></td>
                      <td><strong>TOTAL</strong></td>
                      <td class="text-right"><strong>${modulusuno.formatPrice(number: detail*.total.sum())}</strong></td>
                    </tr>
                  </tfoot>
                </table>
              </div>
            </g:if>
            <g:else>
              <div class="alert alert-warning">No se encontraron facturas vencidas </div>
            </g:else>
            </div>

          </div>
        </div>
      </div>
      </g:if>

    </div>
  </body>
</html>
