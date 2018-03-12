<!DOCTYPE html>
<%! import com.modulus.uno.paysheet.PaysheetStatus %>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'paysheet.label', default: 'Paysheet')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-credit-card-alt fa-3x"></i>
        Nómina
        <small>${paysheet.paysheetContract.client}</small>
      </h1>
    </div>

    <div class="content scaffold-edit" role="main">
      <div class="portlet portlet-default">
        <div class="portlet-heading">
          <div class="row">
            <div class="col-md-6">
            </div>
            <div class="col-md-6 text-right">
              <g:link class="btn btn-default" action="list">Lista</g:link>
            </div>
          </div>
        </div>
        <div class="portlet-body">
          <g:render template="paysheetData"/>
        </div>
        <div class="portlet-footer">
					<sec:ifAnyGranted roles="ROLE_AUTHORIZER_PAYSHEET">
          <g:if test="${!paysheet.dispersionFiles && paysheet.status != PaysheetStatus.CANCELED && paysheet.status != PaysheetStatus.REJECTED}">
            <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#modalConfirm">Cancelar</button>
            <div class="modal fade" id="modalConfirm" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
              <div class="modal-dialog" role="document">
                <div class="modal-content">
                  <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">Confirme la acción</h4>
                  </div>
                  <div class="modal-body">
                    ¿Está seguro en que quiere Cancelar la nómina?
                  </div>
                  <div class="modal-footer">
                    <g:link class="edit btn btn-danger" action="cancel" resource="${this.paysheet}">Sí</g:link>
                    <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
                  </div>
                </div>
              </div>
            </div>
          </g:if>
          </sec:ifAnyGranted>
        </div>
      </div>

      <div class="row">
        <div class="col-md-12">
          <g:render template="listEmployees"/>
        </div>
      </div>

			<g:if test="${dispersionSummary}">
      <div class="row">
        <div class="col-md-12">
 					<g:render template="dispersion"/>
        </div>
      </div>
			</g:if>

			<g:if test="${paysheet.dispersionFiles}">
			<div class="row">
				<div class="col-md-12">
					<g:render template="dispersionFiles"/>
				</div>
			</div>
      <div class="row">
        <div class="col-md-12">
          <g:render template="resultDispersionFiles"/>
        </div>
      </div>
			</g:if>
			
      <div class="row">
        <div class="col-md-12 text-right">
					<sec:ifAnyGranted roles="ROLE_OPERATOR_PAYSHEET">
          <g:if test="${paysheet.status == PaysheetStatus.CREATED}">
            <g:link class="btn btn-primary" action="sendToAuthorize" id="${paysheet.id}">Solicitar Autorización</g:link>
          </g:if>
					</sec:ifAnyGranted>
					<sec:ifAnyGranted roles="ROLE_AUTHORIZER_PAYSHEET">
          <g:if test="${paysheet.status == PaysheetStatus.TO_AUTHORIZE}">
            <g:link class="btn btn-primary" action="authorize" id="${paysheet.id}">Autorizar</g:link>

            <a data-toggle="collapse" role="button" href="#inputRejectReason" class="btn btn-danger" aria-expanded="false" aria-controls="inputRejectReason">Rechazar</a>
            <br/><br/>
            <div class="row">
            <div class="col-md-12">
            <div class="collapse" id="inputRejectReason">
              <div class="well">
                <g:form action="reject" id="${paysheet.id}">
                  <div class="form-group">
                    <g:textArea name="rejectReason" placeholder="Motivo del rechazo" rows="3" cols="60" maxLength="255" class="form-control"/>
                    <br/>
                    <button type="submit" class="btn btn-danger">Rechazar</button>
                  </div>
                </g:form>
              </div>
            </div>
            </div>
            </div>
          </g:if>
					</sec:ifAnyGranted>
        </div>
      </div>

    </div>
    <asset:javascript src="paysheet/show.js"/>
  </body>
</html>

