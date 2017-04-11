<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'company.label', default: 'Company')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
     <h1><g:message code="company.list.label" args="[entityName]" /></h1>
      <ol class="breadcrumb">
        <li><i class="fa fa-caret-square-o-up"></i> Compañia</li>
        <li class="active">Seccion de compañias</li>
      </ol>
    </div>
    <div class="page-body">

      <div id="list-company" class="" role="main">
        <g:if test="${flash.message}">
          <div class="message alert alert-info" align="center" role="status">${flash.message}</div>
        </g:if>
        <div class="portlet portlet-blue">
        <div class="portlet-heading">
          <div class="portlet-title">
            <h2>Selecciona una de las empresas</h2>
          </div>
        </div>
        <div class="portlet-body">
          <g:form class="form-group" id="company-selection" url="[action:'setCompanyInSession',controller:'company']" >
                  <g:select class="form-control" name="company" from="${companies}" optionKey="id" noSelection="${['':'Seleccione una Empresa']}" required="required"/>
                  <br>
                <div class="text-right">
                    <button type="submit" class="btn btn-default">IR</button>
                </div>
              </g:form>
        </div>
        </div><!--Close .portlet-->
      </div><!--Close #list-company-->
      
      

    </div><!--Close page body-->

  </body>
</html>
