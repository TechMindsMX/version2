<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'businessEntitiesGroup.label', default: 'BusinessEntitiesGroup')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
      <i class="fa fa-users fa-3x"></i>
      Lista de Grupos de Relaciones Comerciales
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
              <div class="col-md-12">
                <div class="table-responsive">
                  <table class="table">
                    <tr>
                      <th class="text-center">ID</th>
                      <th class="text-center">Descripción</th>
                      <th class="text-center">Empresa</th>
                      <th class="text-center">Tipo</th>
                    </tr>
                    <g:each in="${groupList}" var="group">
                      <tr>
                        <td class="text-center">
                          <g:link action="show" id="${group.id}">${group.id}</g:link></td>
                        <td>${group.description}</td>
                        <td>${group.company}</td>
                        <td class="text-center">${group.type}</td>
                      </tr>
                    </g:each>
                  </table>
                  <nav>
                    <div class="pagination">
                      <g:paginate class="pagination" action="list" total="${groupCount ?: 0}" />
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
