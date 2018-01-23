<%! import com.modulus.uno.quotation.QuotationRequestStatus %>
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
        Solitudes de Cotización
      </h1>
    </div>

    <div id="edit-address" class="content scaffold-edit" role="main">
      <div class="portlet portlet-default">
        <div class="portlet-heading">
          <div class="portlet-title">
            <div class="portlet-title"></div>
            <div class="clearfix"></div>
          </div>
        </div>

           <div class="portlet-body">
              <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
              </g:if>
            
            <g:render template="dataQuotationRequest"/>

  	        <sec:ifAnyGranted roles="ROLE_OPERATOR_QUOTATION">
              <g:if test="${quotationRequest.status == QuotationRequestStatus.CREATED}">
                <div class="row">
                  <div class="col-md-8 text-right">
                    <g:link class="btn btn-primary" controller="quotationRequest" action="sendQuotation" id="${quotationRequest.id}">Solicitar</g:link>
                  </div>
                  <div class="col-md-2 text-right">
                    <g:link class="btn btn-primary" controller="quotationRequest" action="edit" id="${quotationRequest.id}">Editar</g:link>
                  </div>
                  <div class="col-md-2">
                    <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#modalConfirm">
                      <i class="fa fa-trash"></i> Borrar
                    </button>
                    <div class="modal fade" id="modalConfirm" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                      <div class="modal-dialog" role="document">
                        <div class="modal-content">
                          <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="myModalLabel">Confirme la acción</h4>
                          </div>
                          <div class="modal-body">
                            ¿Está seguro de eliminar la solicitud de cotización?
                          </div>
                          <div class="modal-footer">
                            <g:link action="delete" id="${quotationRequest.id}" class="btn btn-primary">
                              Sí
                            </g:link>
                            <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

              </g:if>
            </sec:ifAnyGranted>
          
	          <sec:ifAnyGranted roles="ROLE_EXECUTOR_QUOTATION">
              <g:if test="${quotationRequest.status == QuotationRequestStatus.SEND}">
                <g:form action="processRequest">
                  <g:render template="setBillerForm"/>
                </g:form>
              </g:if>
            </sec:ifAnyGranted>

            <div class=" portlet-footer">
              <div class="row">
                <div class="col-md-6">
                  <g:link class="btn btn-default" controller="quotationRequest" action="index">Regresar</g:link>
                </div>
              </div>
            </div>

      </div>
    </div>
  </body>
</html>
