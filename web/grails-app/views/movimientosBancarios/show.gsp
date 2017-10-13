<%! import com.modulus.uno.MovimientoBancarioType %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'movimientosBancarios.label', default: 'MovimientosBancarios')}" />
        <title>Listado de Movimientos Bancarios</title>
    </head>
    <body>
      <div class="page-title">
        <h1>
          <i class="icon-retiro fa-3x"></i>
           ${bankAccount}
          <small>Saldo Total: <modulusuno:amountAccountToday id="${bankAccount.id}" /></small>
        </h1>
      </div>
      <div class="col-lg-12">
        <div class="portlet portlet-blue">
          <div id="defaultPortlet" class="panel-collapse collapse in">
            <div class="portlet-body">

        <div id="show-movimientosBancarios" class="content scaffold-show" role="main">
          <p><g:link controller="movimientosBancarios" action="create" class="btn btn-green" id="${bankAccount.id}"><i class="fa fa-plus" aria-hidden="true"></i> Nuevo movimiento</g:link></p>

            <g:if test="${flash.message}">
              <div class="message" role="status">${flash.message}</div>
            </g:if>

            <div class="table-responsive">
              <table class="table table-condensed">

              <tr>
                <th scope="col">Fecha</th>
                <th scope="col">Concepto</th>
                <th scope="col">Referencia</th>
                <th scope="col">Abono</th>
                <th scope="col">Cargo</th>
                <th scope="col"></th>
                <th scope="col">Conciliable</th>
              </tr>
            <g:each in="${movimientosBancarios}" var="movimiento">
              <tr>
                <td>
                  ${movimiento.dateEvent.format('dd/MMM/yyyy')}
                </td>
                <td>${movimiento.concept}</td>
                <td>${movimiento.reference}</td>
                <td>
                  <g:if test="${movimiento.type == MovimientoBancarioType.CREDITO}">
                    $ ${movimiento.amount}
                  </g:if>
                </td>
                <td>
                  <g:if test="${movimiento.type == MovimientoBancarioType.DEBITO}">
                    $ ${movimiento.amount}
                  </g:if>
                </td>
                <td>
                  <g:if test="${movimiento.type == MovimientoBancarioType.CREDITO}">
                  <span class="label label-success">
                    <span class="glyphicon glyphicon-chevron-up" aria-hidden="true"></span>
                  </span>
                  </g:if>
                  <g:else>
                  <span class="label label-danger">
                    <span class="glyphicon glyphicon-chevron-down" aria-hidden="true"></span>
                  </span>
                  </g:else>
                </td>
								<td class="text-center">
									<g:if test="${movimiento.reconcilable}">
	                  <span class="label label-primary">
      	              <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
  	                </span>
									</g:if>
									<g:else>
	                  <span class="label label-info">
      	              <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
  	                </span>
								
									</g:else>
								</td>
              </tr>
            </g:each>

              </table>
              <nav>
                <div class="pagination">
                  <g:paginate class="pagination" controller="movimientosBancarios" action="show" total="${movimientosCount}" id="${bankAccount.id}" />
                </div>
              </nav>

            </div>
        </div>
    </body>
</html>
