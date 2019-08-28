<!doctype html>
<html>
  <head>
    <title>Modulus UNO | Servicios Financieros</title>
    <meta name="layout" content="main">
  </head>
  <body>

    <sec:ifAnyGranted roles="ROLE_M1">
      <div class="page-title">
        <h1>
        <i class="fa fa-hourglass-end fa-3x"></i>
          Resultados de Traspaso Final
        </h1>
      </div>


        <div class="portlet portlet-default">
           <div class="portlet-body">
              <g:form action="listFinalTransactionResult">
                <div class="row">
                  <div class="col-md-12 col-lg-12">
                    <div class="form-group">
                      <label>Fecha:</label>
                      <input type="text" id="datepicker" name="date" required="required" autocomplete="off">
                    </div>
                  </div>
                </div>
                <div class="row">
                  <div class="col-md-12 text-right">
                    <button class="btn btn-primary" type="submit">Consultar</button>
                  </div>
                </div>
              </g:form>
          </div>
        </div>

        <g:if test="${results}">
          <g:render template="listFinalTransactionResults"/>
        </g:if>

    </sec:ifAnyGranted>

    <asset:javascript src="managerApplication/finalTransaction.js"/>
  </body>
</html>
