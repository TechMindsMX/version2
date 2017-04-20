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
        Ordenes de Pago
        <small>Conciliacion por Cliente</small>
      </h1>
    </div>
    <div class="row">
    <div class="col-md-offset-2 col-md-8">
      <div class="portlet portlet-blue">
        <div class="portlet-heading">
          Buscar ordenes de pago.
        </div>
        <div class="portlet-body">
        
        <g:form action="conciliationSaleOrderPerClients" class="form-horizontal">
        <div class="form-group">
          <div class="col-sm-9">
            <input type="text" name="q" class="form-control" value="${q}" placeholder="RFC ó Nombre" maxlength="20"/>
          </div>
          <div class="col-sm-2">
            <button class="btn btn-primary">Buscar</button>
          </div>
        </div>
        </g:form>


          <g:if test="${!clients.isEmpty()}">
        <table class="table table-hover">
          <tr>
            <th>#</th>
            <th>RFC</th>
            <th>Razón social</th>
            <th></th>
          </tr>
          <g:each in="${clients.sort{it.rfc}}" var="client" status="index">
          <tr>
            <td>${index+1}</td>
            <td>${client.rfc}</td>
            <td>${client}</td>
            <td><g:link class="btn btn-info" action="searchSaleOrderExcecuteByClient" params="[rfc:client.rfc]" >Ver</g:link>
          </tr>
          </g:each>
        </table>
        </g:if>

        </div>
      </div>
      </div>
    </div>
  </body>
</html>
