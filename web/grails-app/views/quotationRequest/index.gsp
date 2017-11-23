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
        <small>${quotationContract?.client}</small>
      </h1>
    </div>

    <div id="edit-address" class="content scaffold-edit" role="main">
      <div class="portlet portlet-blue">
        <div class="portlet-body">
          <g:if test="${quotationContract.client}">
            <div class="row">
              <div class="col-md-12">
                <h4>${quotationContract?.client}</h4>
                <g:link class="btn btn-primary" action="index">Regresar</g:link>
                </div>
              </div>
            </g:if>
          <div class="portlet-title"></div>
          <div class="clearfix"></div>
        </div>
        <div id="horizontalFormExample" class="panel-collapse collapse in">
          <div class="portlet-body">
            <g:if test="${flash.message}">
              <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:if test="${!quotationContract.client}">
            <g:form action="chooseClient">
            <div class="row">
              <div class="col-md-12">
                <div class="form-group">
                  <label><g:message code="Clientes"/></label>
                  <g:select name="id" class="form-control"
                  from="${quotationContractList}"
                  optionValue="client"
                  optionKey="id">
                  </g:select>
                </div>
                <br>
              </div>
              <div class="row">
                <div class="col-md-12 text-right">
                  <g:submitButton name="seleccionar" class="btn btn-primary" value="${message(code: 'default.button.seleccionar.label', default: 'Seleccionar')}" />
                </div>
              </div>
            </div>
            </g:form>
            
            </g:if>
            <g:else>
            <div class="row">
              <div class="col-md-12">
                <div class="table-responsive">
                  <table class="table table-striped table-condensed">
                    <tr>
                      <th class="text-center">Fecha</th>
                      <th class="text-center">Descripción</th>
                      <th class="text-center">Comisión</th>
                      <th class="text-center">Monto</th>
                      <th class="text-center">Estatus</th>
                    </tr>
                    <g:each in="${quotationRequestList}" var="request">
                      <tr>
                        <td 
                        ><g:link action="show" id="${request.id}"><g:formatDate format="dd-MM-yyyy" date="${request.dateCreated}" class="form-control"/></g:link></td>
                        <td class="text-center">${request.description}</td>
                        <td class="text-right">${request.commission}</td>
                        <td class="text-right">${modulusuno.formatPrice(number:request.amount)}</td>
                        <td class="text-center"><g:message code="quotationRequest.status.${request.status}"/></td>
                      </tr>
                    </g:each>
                  </table>
                 </div>
              </div>
            </div>
          </div>
          </g:else>
          <nav>
            <div class="pagination">
              <g:paginate class="pagination" controller="quotationRequest" action="index" total="${quotationRequestCount ?: 0}" />
            </div>
          </nav>
        </div>
      </div>
    </div>
  </body>
</html>
