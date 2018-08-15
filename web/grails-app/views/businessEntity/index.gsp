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
          <div class="portlet-title"></div>
          <div class="clearfix"></div>
        </div>
        <div id="horizontalFormExample" class="panel-collapse collapse in">
          <div class="portlet-body">
              <g:if test="${flash.message}">
              <div class="message" role="status">${flash.message}</div>
              </g:if>

            <div class="row">
              <g:if test="${businessEntityList}">
              <div class="col-md-7">
                <g:form controller="businessEntity" action="search" class="form-horizontal">
                  <div class="form-group">
                    <div class="col-sm-6">
                      <input type="text" name="rfc" class="form-control" placeholder="RFC ó Nombre" maxlength="20"/>
                    </div>
                    <div class="col-sm-2">
                      <button class="btn btn-default">Buscar</button>
                    </div>
                  </div>
                </g:form>
              </div>

              <div class="col-md-2">
                <button class="btn btn-primary" type="button" data-toggle="collapse" data-target="#export" aria-expanded="false" aria-controls="collapseExample">Exportar</button>
              </div>
              
              <div class="col-md-2 text-right">
                <sec:ifAnyGranted roles="ROLE_AUTHORIZER_EJECUTOR">
                  <g:if test="${businessEntityToAuthorize}">
                    <g:link class="btn btn-primary" action="showToAuthorizeEntities">Autorizar Registros</g:link>
                  </g:if>
                </sec:ifAnyGranted>
              </div>
              </g:if>
            </div>

            <div class="row">
                <div class="collapse" id="export">
                  <div class="well">
                    <div class="container-fluid">
                      <g:form controller="businessEntity" action="downloadListForBusinessEntities" class="form-horizontal">
                        <div class="col-md-8">
                          <div class="form-group">
                            <g:render template="businessEntityTypes"/>
                          </div>
                        </div>
                        <div class="col-md-1">
                          <button class="btn btn-default">XLS</button>
                        </div>
                      </g:form>
                      <div class="col-md-3">
                        <g:link class="btn btn-primary" action="downloadListForBusinessEntities">Todos</g:link>
                      </div>
                    </div>
                  </div>
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
                <g:each in="${businessEntityList?.sort{it.id}}" var="be">
                  <tr>
                    <td>
                      <g:link controller="businessEntity" action="show" id="${be.id}">${be.rfc}</g:link></td>
                    <td>${be}</td>
                    <td>${be.website}</td>
                    <td>${be.type}</td>
                    <td><my:whatIsThisBusinessEntity be="${be}" company="${company}" /></td>
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
</html>
