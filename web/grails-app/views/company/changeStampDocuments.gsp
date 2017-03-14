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
                <label>Archivo .key</label>
                <input type="file" required="true" class="form-control" name="key" />
                <label>Archivo .cer</label>
                <input type="file" required="true" class="form-control" name="cer" />
                <label>Número de Certificado <small><a href="https://portalsat.plataforma.sat.gob.mx/RecuperacionDeCertificados/">Más Información</a></small></label>
                <input type="text" required="true" class="form-control" name="numCert" />
                <label>Logotipo <small>(Solo se acepta archivos *.png con dimensiones 254 × 101)</small></label>
                <input type="file" required="true" class="form-control" name="logo" accept="image/png" />
                <label>Password</label>
                <input type="password" required="true" class="form-control" name="password" />
                <br />
                <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR">
                  <input type="submit" class="btn btn-green btn-lg" value="Actualizar" />
                </sec:ifAnyGranted>
              </g:form>

            </div>
          </div>
        </div>
    </div>
  </body>
</html>
