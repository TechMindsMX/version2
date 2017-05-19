<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'movimientosBancarios.label', default: 'MovimientosBancarios')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
      <div class="page-title">
        <h1>
          <i class="icon-retiro fa-3x"></i>
          Listado de Cuentas Bancarias
          <small>${new Date().format("dd/MMMM/yyy")}</small>
        </h1>
      </div>
      <div class="col-lg-12">
        <div class="portlet portlet-blue">
          <div id="defaultPortlet" class="panel-collapse collapse in">
            <div class="portlet-body">

        <div id="list-movimientosBancarios" class="content scaffold-list" role="main">
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <p>
            <font color="red" size="1">* Cuenta Concentradora</font>
            </p>
            <div class="table-responsive">
              <table class="table table-condensed">
                <thead>
                  <tr>
                    <th>Cuenta</th>
                    <th>Saldo</th>
                    <th></th>
                  </tr>
                </thead>
                <tbody>

            <g:each in="${bankAccountsOfCompany}" var="account">
              <tr>
                <td>
                  ${account}&nbsp;<g:if test="${account.concentradora}"><font color="red">*</font></g:if>
                </td>
                <td class="text-right">
                  <modulusuno:amountAccountToday id="${account.id}" />
                </td>
                <td>
                  <g:link action="show" id="${account.id}" class="btn btn-green" ><i class="fa fa-search" aria-hidden="true"></i></g:link>&nbsp;
                  <g:link controller="movimientosBancarios" action="create" class="btn btn-green" id="${account.id}"><i class="fa fa-plus" aria-hidden="true"></i></g:link>
                </td>
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

    </body>
</html>
