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
      Registros / Lista de relaciones comerciales
      <small><g:message code="businessEntity.view.list.label" /></small>
      </h1>
    </div>

    <div id="edit-address" class="content scaffold-edit" role="main">
      <div class="portlet portlet-blue">
        <div class="portlet-heading">
          <div class="portlet-title">
            <br />
            <br />
          </div>
          <div class="clearfix"></div>
        </div>
        <div id="horizontalFormExample" class="panel-collapse collapse in">
          <div class="portlet-body">
              <g:if test="${flash.message}">
              <div class="message" role="status">${flash.message}</div>
              </g:if>

            <div class="row">
              <div class="col-md-10">
                <g:form controller="businessEntity" action="search" class="form-horizontal">
                  <div class="form-group">
                    <div class="col-sm-6">
                      <input type="text" name="rfc" class="form-control" placeholder="RFC ó Nombre" maxlength="20"/>
                    </div>
                    <div class="col-sm-2">
                      <button class="btn btn-default">Buscar</button>
                    </div>
                    <div class="col-md-2 text-right">
                      <g:link class="btn btn-primary">Exportar a XLS</g:link>
                    </div>
                  </div>
                </g:form>
              </div>
              <div class="col-md-2 text-right">
                <g:if test="${businessEntityToAuthorize}">
                <g:link class="btn btn-primary" action="showToAuthorizeEntities">Autorizar Registros</g:link>
                </g:if>
              </div>
            </div>

            <div class="row">
              <div class="col-md-12">
            <div class="table-responsive">
              <table class="table">
                <tr>
                  <th>RFC</th>
                  <th>Nombre/Razón Social</th>
                  <th>Sitio web</th>
                  <th>Persona</th>
                  <th>Tipo de Relación</th>
                  <th>Estatus</th>
                </tr>
                <g:each in="${businessEntityList.sort{it.id}}" var="be">
                  <tr>
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
</html
