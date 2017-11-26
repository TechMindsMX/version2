<!DOCTYPE html>
<html>
  <head>
  	<meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'quotationContract.label', default: 'QuotationContract')}" />
  	<title><g:message code="default.create.label" args="[entityName]" /></title>
  </head>

  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-list-alt fa-3x"></i>
        Cotización
        <small>${company}</small>
      </h1>
    </div>

    <div id="edit-address" class="content scaffold-edit" role="main">
      <div class="portlet portlet-blue">  
        <div class="portlet-body">
          <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
          </g:if>
          <div class="row">
            <div class="col-md-6">
              <dl>
            	  <dt>Cliente </dt>
        			  <dd>${quotationContract.client}</dd>
        			  <dt>Fecha de apertura </dt>
        			  <dd><g:formatDate format="dd-MM-yyyy" date="${quotationContract.initDate}" class="form-control"/></dd> 
                <dt>Comisión</dt>
        			  <dd>${quotationContract.commission}</dd>
        		  </dl>
            </div>
          </div>
        </div>
        <div class="portlet-footer">
          <div class="row">
            <div class="col-md-6">
              <g:link class="btn btn-default" controller="quotationContract" action="index">Regresar</g:link>
            </div>
            <div class="col-md-6 text-right">
              <g:link class="btn btn-default" controller="quotationContract" action="edit" id="${quotationContract.id}">Editar</g:link>
            </div>
          </div>
        </div>

      </div>
    </div>
        <div class="row">
          <g:form name="addingUsers" action="addUsers">
            <g:render template="users" />
            <input value="${quotationContract.id}" name="quotationId" type="hidden"/>
          </g:form>
        </div>
    <asset:javascript src="businessEntity/selectEntities.js"/>
  </body>
</html>