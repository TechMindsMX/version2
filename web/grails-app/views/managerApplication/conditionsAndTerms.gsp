<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'saleOrder.label', default: 'SaleOrder')}" />
    <title>Términos y Condiciones</title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-file-text-o-3x"></i>
        Términos y Condiciones
      </h1>
    </div>

    <div class="row">
      <div class="col-lg-12">
        <textarea class="form-control" rows="15">${conditionsAndTerms}</textarea>
      </div>
    </div>
  </body>
</html>
