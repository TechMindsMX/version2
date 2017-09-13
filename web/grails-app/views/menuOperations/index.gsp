<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <title>Menu Operations</title>
  </head>
  <body>

    <div class="row">
      <!-- BEGIN PAGE TITLE -->
      <div class="page-title">
        <h1>
          Menu operations
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
              <h4>Clases con menú</h4>
            </div>
            <div class="clearfix"></div>
          </div>
          <div id="defaultPortlet" class="panel-collapse collapse in">
            <div class="portlet-body">
              <ul>
              <g:each in="${roles}" var="r">
                <li>
                  <g:link controller="menuOperations" action="show" id="${r.id}">
                    <g:message code="role.authority.${r.authority.toLowerCase()}" default="${r.authority}" />
                  </g:link>
                </li>
              </g:each>
              </ul>
            </div>
          </div>
        </div>
      </div>

      <div class="col-md-6">
        <div class="portlet portlet-default">
          <div class="portlet-heading">
            <div class="portlet-title">
              <h4>Menu's disponibles</h4>
            </div>
            <div class="clearfix"></div>
          </div>
          <div id="defaultPortlet" class="panel-collapse collapse in">
            <div class="portlet-body">
              <ul>
              <g:each in="${menus}" var="m">
                <li>
                  <g:link controller="menu" action="show" id="${m.id}">
                    ${m}
                  </g:link>
                </li>
              </g:each>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
