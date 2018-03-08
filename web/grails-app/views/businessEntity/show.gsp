<!DOCTYPE html>
<%! import com.modulus.uno.BusinessEntityType %>
<%! import com.modulus.uno.NameType %>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'businessEntity.label', default: 'BusinessEntity')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>

      <div class="page-title">
        <h1>
          <i class="fa fa-user fa-3x"></i>
          <g:message code="businessEntity.view.show.label" />
          <small>${relation}</small>
        </h1>
      </div>

          <div clas="row">
            <div class="col-md-6">
              <g:render template="generalData"/>
            </div>
            <div class="col-md-6">
              <g:render template="bankAccounts"/>
            </div>
          </div>
          <div class="row"></div>
          <div class="row">
            <div class="col-md-6">
              <g:render template="summaryBalance"/>
            </div>
            <div class="col-md-6">
              <g:render template="addresses"/>
            </div>
          </div>

      <g:if test="${relation == 'EMPLEADO'}">
        <div class="row">
          <div class="col-md-12">
            <g:render template="dataImss"/>
          </div>
        </div>
      </g:if>
      <div class="row">
        <div class="col-md-2 col-md-offset-10">
          <g:link class="btn btn-primary" action="index">Regresar</g:link>
        </div>
      </div>

  </body>
</html>
