<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'quotationContract.label', default: 'QuotationContract')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
  </head>
  
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-list-alt fa-3x"></i>
        Cotización
        <small>${company}</small>
      </h1>
    </div>
    <div id="edit-address" class="content scaffold-edit" role="main">
      <div class="portlet portlet-blue">
        <div class="portlet-heading">
          <div class="portlet-title"></div>
          <div class="clearfix"></div>
        </div>
        <div id="horizontalFormExample" class="panel-collapse collapse in">
          <div class="portlet-body">
            <g:if test="${flash.message}">
              <div class="message" role="status">${flash.message}</div>
            </g:if>
            <div class="row">
              <div class="col-md-12">
                <div class="table-responsive">
                  <table class="table table-striped table-condensed">
                    <tr>
                      <th>Cliente</th>
                      <th>Fecha de Inicio</th>
                      <th>Comisiòn</th>
                    </tr>
                    <g:each in="${quotationContractList.sort{it.client.toString()}.each(){it}}" var="be">
                      <tr>
                        <td><g:link action="show" id="${be.id}">${be.client}</g:link></td>
                        <td><g:formatDate format="dd-MM-yyyy" date="${be.initDate}"/> </td>
                        <td>${be.commission}</td>
                      </tr>
                    </g:each>
                  </table>
                  <nav>
                    <div class="pagination">
                      <g:paginate class="pagination" controller="businessEntity" action="index" total="${businessEntityCount ?: 0}" />
                    </div>
                  </nav>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
