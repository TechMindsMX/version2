<div class="portlet portlet-default">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4>Archivos de Resultados de Dispersión</h4>
    </div>
    <div class="clearfix"></div>
  </div>

  <div class="portlet-body">
    
    <sec:ifAnyGranted roles="ROLE_FICO_EJECUTOR">
    <div class="row">
      <div class="col-md-12">
        <g:form action="uploadResultDispersionFile" method="POST" enctype="multipart/form-data" id="${paysheet.id}">
          <div class="form-group">
            <label>Banco:</label>
            <g:select class="form-control" name="bank" from="${paysheetBanks}" optionKey="id" required="required"/>
          </div>
          <div class="form-group">
            <label>Archivo de resultados de dispersión:</label>
            <input type="file" name="file" class="form-control" required="required" accept="text/plain"/>
          </div>
          <div class="row">
            <div class="col-md-12 text-right">
              <input class="btn btn-primary" type="submit" value="Subir"/>
            </div>
          </div>
        </g:form>
      </div>
    </div>
    </sec:ifAnyGranted>

    <br/><br/>

		<div class="row">
			<div class="col-md-12">
        <g:if test="${!paysheet.dispersionResultFiles}">
          <div class="alert alert-warning" role="alert">No se han cargado archivos de resultados de dispersión todavía</div>
        </g:if><g:else>
				<ul>
				<g:each in="${paysheet.dispersionResultFiles.sort{it.id}}" var="resultFile">
					<li>
						<a href="${baseUrlDocuments}/${resultFile.file.title}.${resultFile.file.mimeType}"><i class="glyphicon glyphicon-download-alt"></i>${resultFile.bank} - ${resultFile.file.type}</a>
					</li>
				</g:each>
				</ul>
        </g:else>
			</div>
		</div>

	</div>
</div>
