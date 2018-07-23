<!DOCTYPE html>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'depositOrder.label', default: 'DepositOrder')}" />
  <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
  <g:if test="${isMoneyBackOrder}">
    <g:set var="messageOrder" value="Reembolso"/>
    <g:set var="messageBusinessEntityOrder" value="Empleado"/>
  </g:if><g:else>
    <g:set var="messageOrder" value="Compra"/>
    <g:set var="messageBusinessEntityOrder" value="Proveedor"/>
  </g:else>

  <div class="page-title">
    <h1>
      <i class="fa fa-shopping-basket fa-3x"></i>
      Operaciones / Orden de ${messageOrder}<small>Listado de Órdenes de ${messageOrder}</small>
    </h1>
  </div>
<div id="edit-address" class="content scaffold-edit" role="main">
  <div class="portlet portlet-blue">
    <div id="horizontalFormExample" class="panel-collapse collapse in">
      <div class="portlet-body">
        
        <modulusuno:showFilters controller="purchaseOrder" action="search" filters="['providerName']" labels="['PROVEEDOR']" filterValues="${filterValues}" viewAll="list"/>
        <div class="row">
          <div class="col-md-12 text-right">
            <g:link class="btn btn-primary" action="listMissingDocs">Pendientes de Factura</g:link>
          </div>
        </div>

        <g:if test="${flash.message}">
          <div class="alert alert-danger" role="alert">${flash.message}</div>
        </g:if>
        <g:if test="${messageSuccess}">
          <div class="well well-sm alert-success">${messageSuccess}</div>
        </g:if>
      <div class="table-responsive">
      <table class="table">
        <tr>
          <th>No. de Orden</th>
          <th>${messageBusinessEntityOrder}</th>
          <th>Estatus</th>
          <th>Fecha de Pago</th>
          <th>Total</th>
          <th>Por pagar</th>
          <g:if test="${!isMoneyBackOrder}">
            <th>Anticipada</th>
          </g:if>
          <th>Factura</th>
        </tr>
          <g:if test="${purchaseOrder.isEmpty()}">
            <div class="alert alert-danger" role="alert">
              <g:message code="purchaseOrder.list.empty"/>
            </div>
          </g:if>
        <g:each in="${purchaseOrder}" var="purch">
          <tr class="${message(code: 'purchaseOrder.style.background.'+purch.status)}">
            <td class="text-center"><g:link action="show" id="${purch.id}">${purch.id}</g:link></td>
            <td>${purch.providerName}</td>
            <td><g:message code="purchaseOrder.status.${purch.status}" default="${purch.status}"/></td>
            <td><modulusuno:dateFormat date="${purch.fechaPago}"/></td>
            <td class="text-right">${modulusuno.formatPrice(number: purch.total)}</td>
            <td class="text-right">${modulusuno.formatPrice(number: purch.total - purch.totalPayments)}</td>
            <g:if test="${!purch.isMoneyBackOrder}">
              <td class="text-center"><g:if test="${purch.isAnticipated}">SÍ</g:if><g:else>NO</g:else></td>
            </g:if>
            <td class="text-center">
              <g:if test="${purch.isAnticipated}">
                <g:if test="${!purch.documents}">
                  <span class="label label-danger" title="Sin documentos de facturación">
                    <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                  </span>
                </g:if>
                <g:if test="${purch.documents?.size()==1}">
                  <span class="label label-warning" title="Falta agregar un documento de facturación">
                    <span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span>
                  </span>
                </g:if>
                <g:if test="${purch.documents?.size()>=2}">
                  <span class="label label-success" title="Documentos de facturación completos">
                    <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
                  </span>
                </g:if>
              </g:if>
            </td>
          </tr>
         </g:each>
       </table>
       <g:if test="${!filterValues}">
       <nav>
          <ul class="pagination">
            <g:paginate class="pagination" controller="purchaseOrder" action="list" total="${purchaseOrderCount}" />
          </ul>
        </nav>
        </g:if>
      </div>
    </div>
  </div>
</div>
</div>
</body>
</html>
