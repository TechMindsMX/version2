<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'menuOperation.label', default: 'MenuOperation')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>

      <div class="page-title">
        <h1>
        <i class="fa fa-building fa-3x"></i>
          Operaciones del menú
          <small>Lista de opciones disponibles</small>
        </h1>
      </div>

      <g:link class="create btn btn-default" action="create">Nueva operación de menú</g:link>

      <div id="list-menuOperation" class="content scaffold-list" role="main">
        <h1><g:message code="default.list.label" args="[entityName]" /></h1>
        <g:if test="${flash.message}">
          <div class="message" role="status">${flash.message}</div>
        </g:if>
        <f:table collection="${menuOperationList}" properties="['name','internalUrl']" />

        <div class="pagination">
          <g:paginate total="${menuOperationCount ?: 0}" />
        </div>
      </div>
    </body>
</html>