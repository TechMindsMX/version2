<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'saleOrder.label', default: 'SaleOrder')}" />
    <title>Aviso de Privacidad</title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-file-text-o-3x"></i>
        Aviso de Privacidad
      </h1>
    </div>

    <div class="row">
      <div class="col-lg-12">
        <textarea class="form-control" rows="15">${privacyNotice}</textarea>
      </div>
    </div>
  </body>
</html>
