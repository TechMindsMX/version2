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
        <div class="panel panel-default">
          <div class="panel-body">
            <div class="table-responsive">
              <table class="table">
                <tbody>
                  <tr>
                    <td>
                  <g:each in="${privacyNotice}" var="line">
                      <p class="text-justify text-muted">${line}</p>
                  </g:each>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
