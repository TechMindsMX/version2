<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'paysheetProject.label', default: 'PaysheetProject')}" />
    <title><g:message code="default.update.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-hospital-o fa-3x"></i>
        Editar Proyecto de Nómina
        <small>${paysheetProject.paysheetContract.client}</small>
      </h1>
    </div>
    <div id="edit-address" class="content scaffold-edit" role="main">
      <div class="portlet portlet-blue">
        <div id="horizontalFormExample" class="panel-collapse collapse in">
          <div class="portlet-body">
            <g:if test="${flash.message}">
              <div class="message" role="status">${flash.message}</div>
            </g:if>

              <g:hasErrors bean="${paysheetProject}">
                <ul class="error alert alert-danger" role="alert">
                  <g:eachError bean="${paysheetProject}" var="error">
                  <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message code="paysheetProject.error.${error.field}" args="${[error.defaultMessage.replace('{0}','')]}"/></li>
                    </g:eachError>
                </ul>
              </g:hasErrors>

              <div class="row">
                <div class="col-md-12 text-right">
                  <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#modalConfirm">
                    <i class="fa fa-trash"></i> Borrar
                  </button>
                </div>

                  <div class="modal fade" id="modalConfirm" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                    <div class="modal-dialog" role="document">
                      <div class="modal-content">
                        <div class="modal-header">
                          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                          <h4 class="modal-title" id="myModalLabel">Confirme la acción</h4>
                        </div>
                        <div class="modal-body">
                          ¿Está seguro de eliminar el proyecto de nómina?
                        </div>
                        <div class="modal-footer">
                          <g:link class="btn btn-danger" action="delete" id="${paysheetProject.id}">Sí</g:link>
                          <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
                        </div>
                      </div>
                    </div>
                  </div>

              </div>

              <g:form action="update">
                <g:hiddenField name="id" value="${paysheetProject.id}"/>
                <g:hiddenField name="version" value="${paysheetProject.version}"/>
                <fieldset class="form">
                  <g:render template="form" bean="${form}"/>
                </fieldset>
                <br />
                <div class="row">
                  <div class="col-md-6">
                <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR">
                  <g:submitButton name="update" class="save btn btn-default" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                </sec:ifAnyGranted>
                  </div>
                  <div class="col-md-6 text-right">
                    <g:link class="btn btn-primary" controller="paysheetContract" action="show" id="${paysheetProject.paysheetContract.id}">Cancelar</g:link>
                  </div>
                </div>
              </g:form>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
