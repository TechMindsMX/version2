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
      <g:if test="${documents}">
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
<br />
<br />
