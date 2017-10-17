<div class="portlet portlet-default">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4>Archivos de Dispersión</h4>
    </div>
    <div class="clearfix"></div>
  </div>

  <div class="portlet-body">

		<div class="row">
			<div class="col-md-12">
				<ul>
				<g:each in="${paysheet.dispersionFiles}" var="dispersionFile">
					<li>
						<a href="${baseUrlDocuments}/${dispersionFile.title}.${dispersionFile.mimeType}"><i class="glyphicon glyphicon-download-alt"></i> ${dispersionFile.type}</a>
					</li>
				</g:each>
				</ul>
			</div>
		</div>

	</div>
</div>
