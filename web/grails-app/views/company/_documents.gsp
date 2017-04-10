<div class="">
<div class="portlet portlet-blue">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4>Archivos para Facturación</h4>
    </div>
    <div class="clearfix"></div>
  </div>
  <div id="defaultPortlet" class="panel-collapse collapse in">
    <div class="portlet-body">
      <g:if test="${!documents.status}">
        <g:form class="form-horizontal" action="sendFilesToCreateInvoice" name="documentsToInvoice" method="POST" enctype="multipart/form-data" >
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
          <label>Serie de Facturas</label>
          <input type="text" class="form-control" name="serie" />
          <br />
          <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR">
            <input type="submit" class="btn btn-green btn-lg" value="Subir" />
          </sec:ifAnyGranted>
        </g:form>
      </g:if>
      <g:else>
      <ul>
        <li class="text-primary">
          <span class="label label-success">
            <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
          </span>
          Archivo .key
        </li>
        <li class="text-primary">
          <span class="label label-success">
            <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
          </span>
          Archivo .cer
        </li>
        <li class="text-primary">
          <span class="label label-success">
            <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
          </span>
          Logotipo
        </li>
        <li class="text-primary">
          <span class="label label-success">
            <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
          </span>
          Password del Certificado
         </li>
         <li class="text-primary">
           <span class="label label-success">
             <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
           </span>
           Número de Certificado
         </li>
         <li class="text-primary">
           Serie de Facturas: ${documents.currentSerie ?: "SIN SERIE"} &nbsp;
            <button type="button" class="btn btn-info btn-xs" data-toggle="modal" data-target="#changeSerieModal" data-whatever="${documents.currentSerie}">
              Cambiar Serie
            </button>
            <!-- modal change date -->
            <div class="modal fade" id="changeSerieModal" tabindex="-1" role="dialog" aria-labelledby="changeSerieModalLabel">
              <div class="modal-dialog" role="document">
                <div class="modal-content">
                  <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="changeSerieModalLabel">Cambiar Serie y Folio Inicial de Facturación</h4>
                  </div>
                  <g:form action="changeSerieForInvoices" id="${company.id}">
                  <div class="modal-body">
                      <div class="form-group">
                        <label for="serie" class="control-label">Serie:</label>
                        <input type="text" class="form-control" id="serie" name="serie">
                      </div>
                      <div class="form-group">
                        <label for="folio" class="control-label">Folio Inicial:</label>
                        <input type="number" name="folio" min="1" step="1" required="required">
                      </div>
                  </div>
                  <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                    <button type="submit" class="btn btn-primary">Cambiar</button>
                  </div>
                  </g:form>
                </div>
              </div>
            </div>
            <!-- modal change date end -->

         </li>

       </ul>
       <div class="text-right">
         <g:link class="btn btn-primary" action="changeStampDocuments" id="${company.id}">Cambiar</g:link>
       </div>
      </g:else>
    </div>
  </div>
  <div class="portlet-footer"></div>
  </div>
</div>
<asset:javascript src="company/invoicingDocuments.js"/>
<br />
<br />
