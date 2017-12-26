<!DOCTYPE html>
<%! import com.modulus.uno.paysheet.PrePaysheetStatus %>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'prePaysheet.label', default: 'PrePaysheet')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-credit-card-alt fa-3x"></i>
        Pre-Nómina
        <small>${prePaysheet.paysheetContract.client}</small>
      </h1>
    </div>

    <div class="content scaffold-edit" role="main">
      <div class="portlet portlet-default">
        <div class="portlet-body">
          <g:render template="prePaysheetData"/>
        </div>
        <div class="portlet-footer">
          <div class="row">
            <div class="col-md-6">
              <g:if test="${prePaysheet.status == PrePaysheetStatus.CREATED}">

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
                      ¿Está seguro de eliminar la prenómina seleccionada?
                    </div>
                    <div class="modal-footer">
                      <g:link class="btn btn-danger" action="delete" id="${prePaysheet.id}">Sí</g:link>
                      <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
                    </div>
                  </div>
                </div>
              </div>

              </g:if>
            </div>
            <div class="col-md-6 text-right">
              <g:link class="btn btn-default" action="list">Ir al Listado</g:link>
            </div>
          </div>
        </div>
      </div>

			<g:if test="${importResults}">
			<div class="row">
				<div class="col-md-12">
					<g:render template="importResults"/>
				</div>
			</div>
			</g:if>

      <div class="row">
        <div class="col-md-12">
          <g:render template="listEmployees"/>
        </div>
      </div>

    </div>
  </body>
</html>


