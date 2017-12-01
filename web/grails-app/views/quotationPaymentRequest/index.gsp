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
        Lista de Solicitudes de Pago
        <small>${quotationContract?.client}</small>
      </h1>
    </div>
    <div id="edit-address" class="content scaffold-edit" role="main">
      <div class="portlet portlet-blue">
        <div class="portlet-body">
          <div class="portlet-title">
            <g:if test="${quotationContract}">
            <div class="row">
              <div class="col-md-12">
                <g:link class="btn btn-primary" action="index">Regresar</g:link>
              </div>
            </div>
            </g:if>
          </div>
          <div class="clearfix"></div>
        </div>
        <div id="horizontalFormExample" class="panel-collapse collapse in">
          <div class="portlet-body">
            <g:if test="${flash.message}">
              <div class="message" role="status">${flash.message}</div>
            </g:if>
            <div class="row">
              <div class="col-md-12">
                <g:if test="${quotationContract}">
                  <div class="table-responsive">
                    <table class="table table-striped table-condensed">
                      <tr>
                        <th class="text-center">Fecha</th>
                        <th class="text-center">Monto</th>
                        <th class="text-center">Nota</th>
                        <th class="text-center">Metodo de pago</th>
                        <th class="text-center">Estado</th>
                      </tr>
                      <g:each in="${quotationPaymentRequestList}" var="paymentRequest">
                        <tr>
                          <td class="text-center"><g:link action="show" id="${paymentRequest.id}"><g:formatDate format="dd-MM-yyyy" date="${paymentRequest.dateCreated}" class="form-control"/></g:link></td>
                          <td class="text-right">${modulusuno.formatPrice(number:paymentRequest.amount)}</td>
                          <td class="text-center">${paymentRequest.note}</td>
                          <td class="text-center"><g:message code="${paymentRequest.paymentMethod}"/></td>
                          <td class="text-center"><g:message code="quotationPaymentRequest.status.${paymentRequest.status}"/></td>
                        </tr>
                      </g:each>
                    </table>
                  </div>
                </g:if>
                    
                <g:else>
                  <g:form action="selectPaymentRequest">
                    <div class="row">
                      <div class="col-md-12">
                        <div class="form-group">
                          <label><g:message code="Clientes"/></label>
                          <g:select name="quotation" class="form-control"
                                                     from="${quotationContractList}"
                                                     optionValue="client"
                                                     optionKey="id">
                          </g:select>
                          <br>
                        </div>
                      </div>
                      <div class="row">
                        <div class="col-md-12 text-right">
                          <g:submitButton name="seleccionar" class="btn btn-primary" value="${message(code: 'default.button.seleccionar.label', default: 'Seleccionar')}" />
                        </div>
                      </div>
                    </div>
                  </g:form>
                </g:else>
                            
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
  </body>
</html>
