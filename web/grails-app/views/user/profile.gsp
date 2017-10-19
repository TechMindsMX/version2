<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-user fa-3x"></i>
        Perfil de usuario
        <small><g:message code="user.view.show.label" args="[entityName]" /></small>
      </h1>
    </div>
    <div class="row">
      <div class="col-md-12">
        <div class="portlet portlet-blue">
          <div class="portlet-heading">
            <div class="portlet-title">
            </div>
          </div>
          <div id="bluePortlet" class="panel-collapse collapse in">
            <div class="portlet-body">
              <div id="create-address" class="content scaffold-create" role="main">
                <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
                </g:if>

                <dl class="dl-horizontal">
                  <dt>Nombre:</dt>
                  <dd>${user.profile.fullName}</dd>
                  <dt>Usuario:</dt>
                  <dd>${user.username}</dd>
                  <dt>Email:</dt>
                  <dd>${user.profile.email}</dd>
                </dl>
                <br/>
                <div class="row">
                  <div class="col-md-12 text-right">
                    <g:link class="home btn small btn-primary" action="edit" id="${user.id}">Editar</g:link>
                    <g:link controller="dashboard"  class="home btn small btn-primary">Regresar</g:link>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div></div>
    </div>

  </body>
</html>
