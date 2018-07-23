<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'company.label', default: 'Company')}" />
    <asset:stylesheet src="company/show.css" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="row">
      <!-- BEGIN PAGE TITLE -->
      <div class="page-title">
        <h1>
          <i class="fa fa-info-circle fa-3x"></i>
          <g:message code="company.change.documents.tostamp"/>
          <small>${company}</small>
        </h1>
      </div>
      <!-- END OF PAGE TITLE -->
    </div>

    <div class="row">
        <div class="portlet portlet-default">
          <div class="portlet-heading">
            <div class="portlet-title">
              <h4>Especifique los nuevos archivos y datos</h4>
            </div>
            <div class="clearfix"></div>
          </div>
          <div id="defaultPortlet" class="panel-collapse collapse in">
            <div class="portlet-body">
              <g:form class="form-horizontal" action="updateDocumentsToStamp" name="documentsToInvoice" method="POST" enctype="multipart/form-data" >
                <g:render template="formDataToInvoice"/>
              </g:form>
            </div>
          </div>
        </div>
    </div>
  </body>
</html>
