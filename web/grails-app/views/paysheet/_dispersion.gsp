<div class="portlet portlet-default">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4>Dispersar Pagos</h4>
    </div>
    <div class="clearfix"></div>
  </div>

  <div class="portlet-body">

		<g:form action="generatePaymentDispersion" id="${paysheet.id}">
		<div class="row">
			<div class="col-md-12">
				<div class="table-responsive">
					<table class="table table-striped table-condensed">
						<thead>
							<tr>
								<th class="text-center" style="width:20%">Banco</th>
								<th class="text-center" style="width:40%">Cuenta</th>
								<th class="text-center">Total SA</th>
								<th class="text-center">Total IAS</th>
								<th class="text-center"></th>
							</tr>
						</thead>
						<tbody>
						<g:each in="${dispersionSummary}" var="summaryBank">
							<tr>
								<td>${summaryBank.bank.name}</td>
								<td>
									<g:select class="form-control" name="dispersionAccount" from="${summaryBank.accounts}" required=""/>
								</td>
								<td class="text-right">${modulusuno.formatPrice(number:summaryBank.totalSA)}</td>
								<td class="text-right">${modulusuno.formatPrice(number:summaryBank.totalIAS)}</td>
								<td class="text-center"></td>
							</tr>
						</g:each>
						</tbody>
					</table>
				</div>
			</div>
		</div>

		<div class="row">
			<div class="col-md-12">
				<h4>Datos de dispersión</h4>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<div class="form-group">
					<label>Mensaje del Pago</label>
					<input class="form-control" type="text" name="paymentMessage" maxlength="30" pattern="[A-Za-z0-9\s]{1,30}" title="Sólo se permiten letras (sin acentos y sin 'ñ'), números y espacios en blanco" required="" />
				</div>
				<div class="form-group">
					<label>Fecha de Aplicación</label>
					<input class="form-control" type="text" id="dpApplyDate" name="applyDate" required="required">
				</div>
			</div>
			<div class="col-md-6">
				<div class="form-group">
					<label>Secuencia</label>
					<input class="form-control" type="number" name="sequence" max="9999" required="required">
				</div>
				<div class="form-group">
					<label>Nombre de la Empresa Pagadora</label>
					<input class="form-control" type="text" name="nameCompany" maxlength="36" required="required">
				</div>
			</div>
		</div>

		<div class="row">
			<div class="col-md-12 text-right">
				<button class="btn btn-primary">Generar Archivos</button>
			</div>
		</div>

		</g:form>

	</div>
</div>
