<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'businessEntity.label', default: 'BusinessEntity')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
      <i class="fa fa-cube fa-3x"></i>
      Registros / Relaciones Comerciales
      <small><g:message code="businessEntity.view.list.toAuthorize.label" /></small>
      </h1>
    </div>

    <div id="edit-address" class="content scaffold-edit" role="main">
      <div class="portlet portlet-blue">
        <div class="portlet-heading">
        </div>
        <div id="horizontalFormExample" class="panel-collapse collapse in">
          <div class="portlet-body">
              <g:if test="${flash.message}">
              <div class="message" role="status">${flash.message}</div>
              </g:if>
            <g:form action="authorizeEntities">
              <g:hiddenField id="entities" name="entities" value=""/>

            <div class="row">
              <div class="col-md-12 text-right">
                <button class="btn btn-primary">Autorizar</button>
              </div>
            </div>

            <div class="row">
              <div class="col-md-12">
                <div class="table-responsive">
                  <table class="table">
                    <tr>
                      <th><g:checkBox id="selectAll" name="selectAll" title="Seleccionar Todos"/></th>
                      <th>RFC</th>
                      <th>Nombre/Razón Social</th>
                      <th>Sitio web</th>
                      <th>Persona</th>
                      <th>Tipo de Relación</th>
                      <th>Estatus</th>
                    </tr>
                    <g:each in="${beToAuthorize.sort{it.id}}" var="be">
                      <tr>
                        <td><g:checkBox class="entity" id="checkBe" name="checkBe" value="${be.id}" checked="false"/></td>
                        <td>
                          <g:link controller="businessEntity" action="show" id="${be.id}">${be.rfc}</g:link></td>
                        <td>${be}</td>
                        <td>${be.website}</td>
                        <td>${be.type}</td>
                        <td><my:whatIsThisBusinessEntity be="${be}" /></td>
                        <td><g:message code="businessEntity.status.${be.status}"/></td>
                      </tr>
                    </g:each>
                  </table>
                </div>
              </div>
            </div>
            </g:form>

          </div>

          <div class="portlet-footer">
            <div class="row">
              <div class="col-md-12 text-right">
                <g:link class="btn btn-default" action="index">Regresar</g:link>
              </div>
            </div>
          </div>

        </div>
      </div>
    </div>
    <asset:javascript src="businessEntity/selectEntities.js"/>
  </body>
</html
