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
						<tbody>
						<g:each in="${dispersionSummary}" var="summaryBank">
              <g:hiddenField name="bank" value="${summaryBank.bank.id}"/>
              <g:hiddenField name="type" value="${summaryBank.type}"/>
							<tr>
								<td colspan="4"><strong>${summaryBank.type=="SameBank" ? summaryBank.bank.name : "INTERBANCARIO-BBVA"}</strong></td>
              </tr>
              <tr>
								<td style="width:10%"></td>
								<td style="width:20%"><strong>SA</strong></td>
								<td style="width:20%" class="text-right">${modulusuno.formatPrice(number:summaryBank.totalSA)}</td>
								<td style="width:50%" class="text-center">
                  <g:select class="form-control" name="saBankAccount" from="${summaryBank.saPayers}" optionKey="bankAccountId" optionValue="description" required=""/>
                </td>
							</tr>
              <tr>
								<td style="width:10%"></td>
								<td style="width:20%"><strong>IAS</strong></td>
								<td style="width:20%" class="text-right">${modulusuno.formatPrice(number:summaryBank.totalIAS)}</td>
								<td style="width:50%" class="text-center">
                  <g:select class="form-control" name="iasBankAccount" from="${summaryBank.iasPayers}" optionKey="bankAccountId" optionValue="description" required=""/> 
                </td>
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
			</div>
			<div class="col-md-3">
				<div class="form-group">
					<label>Fecha de Aplicación</label>
					<input class="form-control" type="text" id="dpApplyDate" name="applyDate" required="required">
				</div>
			</div>
			<div class="col-md-3">
				<div class="form-group">
					<label>Secuencia</label>
					<input class="form-control" type="number" name="sequence" max="9999" required="required">
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
