<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'menu.label', default: 'Menu')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>

    <div class="row">
      <!-- BEGIN PAGE TITLE -->
      <div class="page-title">
        <h1>
          <i class="fa fa-info-circle fa-3x"></i>
          <g:message code="menu.show"/>
        </h1>
      </div>
      <!-- END OF PAGE TITLE -->
    </div>

    <g:if test="${flash.message}">
    <div class="alert alert-info" role="alert">
        <div class="message" role="status">${flash.message}</div>
    </div>
    </g:if>

    <div class="row">
      <div class="col-md-6">
        <div class="portlet portlet-default">
          <div class="portlet-heading">
            <div class="portlet-title">
              <h4>Datos de la operación de menú</h4>
            </div>
            <div class="clearfix"></div>
          </div>
          <div id="defaultPortlet" class="panel-collapse collapse in">
            <div class="portlet-body">
              <ul class="property-list menu">
                <g:if test="${menu.parentMenu}">
                  <g:link action="show" id="${menu.parentMenu.id}">
                  <f:display bean="menu" property="parentMenu" wrapper="show" />
                  </g:link>
                </g:if>
                <f:display bean="menu" property="name" wrapper="show" />
                <f:display bean="menu" property="internalUrl" wrapper="show" />
                <f:display bean="menu" property="parameters" wrapper="show" />
              </ul>
              <div class="property-value" aria-labelledby="menu-label">
                <g:form action="delete" method="DELETE" id="${menu.id}">
                  <g:link class="edit btn btn-primary" action="edit" resource="${this.menu}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                  <g:link class="edit btn btn-default" action="index" resource="${this.menu}"><g:message code="default.button.index.label" default="Todos los menues" /></g:link>
                  <input class="delete btn btn-danger" type="submit" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                </g:form>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="col-md-6">
        <div class="portlet portlet-default">
          <div class="portlet-heading">
            <div class="portlet-title">
              <h4>Submenues</h4>
            </div>
            <div class="clearfix"></div>
          </div>
          <div id="defaultPortlet" class="panel-collapse collapse in">
            <div class="portlet-body">
              <g:if test="${menu.menus}">
                <ul>
                  <g:each in="${menu.menus}" var="m">
                  <li>
                    <g:link action="show" id="${m.id}">
                    ${m}
                    </g:link>
                  </li>
                  </g:each>
                </ul>
              </g:if>
              <g:else>
                <b>No hay submenues</b>
              </g:else>
              <br></br>
              <div class="property-value" aria-labelledby="menu-label">
                <g:link class="edit btn btn-primary" action="create" resource="${this.menu}" params="['parentMenu.id': menu.id]">
                  <g:message code="default.button.submenu.label" default="New submenu" />
                </g:link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="row">
      <div class="col-md-12">
        <div class="portlet portlet-default">
          <div class="portlet-heading">
            <div class="portlet-title">
              <h4>Menus no incluidos</h4>
            </div>
            <div class="clearfix"></div>
          </div>
          <div id="defaultPortlet" class="panel-collapse collapse in">
            <div class="portlet-body">
              <g:if test="${menusNotIncluded}">
                <ul>
                  <g:each in="${menusNotIncluded}" var="m">
                  <li>
                    <g:link action="show" id="${m.id}">
                    ${m}
                    </g:link>
                  </li>
                  </g:each>
                </ul>
              </g:if>
              <g:else>
                <b>No hay submenues</b>
              </g:else>
              <br></br>
            </div>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
