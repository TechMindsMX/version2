<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'unitType.label', default: 'UnitType')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
      <div class="page-title">
        <h1>
          <i class="fa fa-sitemap fa-3x"></i>
          Tipos de Unidad
          <small><g:message code="unitType.view.list.label" /></small>
        </h1>
      </div>
      <div class="portlet portlet-blue">
        <div class="portlet-heading">
          <div class="portlet-title">
          </div>
          <div class="clearfix"></div>
        </div>
        <div id="list-unitType" class="panel-collapse collapse in">
          <div class="portlet-body">
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <div class="table-responsive">
             <table class="table table-striped table-condensed">
               <tr>
                 <th><g:message code="unitType.unitKey.label" /></th>
                 <th><g:message code="unitType.name.label" /></th>
                 <th><g:message code="unitType.symbol.label" /></th>
               </tr>
               <g:each in="${unitTypeList.sort{it.name}}" var="unitType">
               <tr>
                 <td>
                  <g:link action="show" id="${unitType.id}">
                     ${unitType.unitKey}
                  </g:link>
                 </td>
                 <td>${unitType.name}</td>
                 <td>${unitType.symbol}</td>
               </tr>
               </g:each>
             </table>
            </div>
            <div class="pagination">
                <g:paginate total="${unitTypeCount ?: 0}" />
            </div>
          </div>
          <div class="portlet-footer">
            <g:link class="create btn btn-primary" action="create"><g:message code="unitType.view.create.label" args="[entityName]" /></g:link>
          </div>
        </div>
      </div>
    </body>
</html>
