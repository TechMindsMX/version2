<div class="portlet portlet-default">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4>La empresa cliente es...</h4>
    </div>
    <div class="clearfix"></div>
  </div>
  <div id="defaultPortlet" class="panel-collapse collapse in">
    <div class="portlet-body">
      <g:if test="${flash.message}">
        <div class="alert alert-warning">
        ${flash.message}
        </div>
      </g:if>
      <g:form action="searchClientForSale" class="form-horizontal">
      <div class="form-group">
        <div class="col-sm-9">
          <input type="text" name="q" class="form-control" value="${q}" placeholder="RFC ó Nombre" maxlength="20"/>
          <input type="hidden" name="companyId" value="${company.id}" />
        </div>
        <div class="col-sm-2">
          <button class="btn btn-primary">Buscar</button>
        </div>
      </div>
      </g:form>
      <g:if test="${clients}">
      <p class="text-muted">
      Selecciona un cliente...
      </p>
      <div class="table-responsive">
        <table class="table">
          <thead>
            <tr>
              <th>#</th>
              <th>RFC</th>
              <th>Razón social</th>
            </tr>
          </thead>
          <tbody>
            <g:each in="${clients.sort{it.rfc}}" var="client" status="index">
            <tr>
              <td>${index+1}</td>
              <td>
                <g:link action="chooseClientForSale" id="${client.id}" params="[companyId:company.id,q:q]">
                ${client.rfc}
                </g:link>
              </td>
              <td>
                <g:link action="chooseClientForSale" id="${client.id}" params="[companyId:company.id,q:q]">
                ${client}
                </g:link>
              </td>
            </tr>
            </g:each>
          </tbody>
        </table>
      </div>
      </g:if>
    </div>
  </div>
  <!--div class="portlet-footer">

  </div-->
</div>
