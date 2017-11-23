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
        <div class="portlet-heading">
          <div class="portlet-title">
            <div class="portlet-title">
            </div>
            <div class="clearfix"></div>
          </div>

          <div id="horizontalFormExample" class="panel-collapse collapse in">
            <div class="portlet-body">
              <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
              </g:if>
            </div>
          </div>

          <div class="row">
            <div class="col-md-11">
                <dl>
            		<dt>Cliente </dt>
        			<dd>${quotationContract.client}</dd>
        			<dt>Fecha de apertura </dt>
        			<dd><g:formatDate format="yyyy-MM-dd" date="${quotationContract.initDate}" class="form-control"/></dd>
        			<dt>Comisiòn</dt>
        			<dd>${quotationContract.commission}</dd>
        		</dl>
          </div>
          <div class="row">
            <div class="col-md-6">
              <br>
              <g:link class="btn btn-default" controller="quotationContract" action="index">Regresar</g:link>
            </div>
            <div class="col-md-6 text-right">
              <br>
              <g:link class="btn btn-default" controller="quotationContract" action="edit" id="${quotationContract.id}">Editar</g:link>
            </div>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>





