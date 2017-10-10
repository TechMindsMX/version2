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
        <small>${paysheet.company}</small>
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

      <div class="row">
        <div class="col-md-12 text-right">
          <g:if test="${paysheet.status == PaysheetStatus.CREATED}">
            <g:link class="btn btn-primary" action="sendToAuthorize" id="${paysheet.id}">Solicitar Autorización</g:link>
          </g:if>
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
        </div>
      </div>

    </div>
    <asset:javascript src="paysheet/show.js"/>
  </body>
</html>

