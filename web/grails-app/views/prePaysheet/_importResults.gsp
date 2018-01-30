<div class="portlet portlet-default">
	<div class="row">
		<div class="col-md-2 col-md-offset-10 text-right">
			<g:link class="btn btn-primary" action="show" id="${prePaysheet.id}">Cerrar</g:link>
		</div>
	</div>

  <div class="portlet-heading">
    <div class="portlet-title">
      <h4>Resultados de la importación</h4>
    </div>
    <div class="clearfix"></div>
  </div>

  <div class="portlet-body">
		<div class="table-responsive">
			<table class="table table-condensed table-striped">
				<thead>
					<tr>
						<th>RFC</th>
						<th>CURP</th>
						<th>NO. EMPL.</th>
						<th>NOMBRE</th>
						<th>CLABE</th>
						<th>NETO A PAGAR</th>
						<th>RESULTADO</th>
					</tr>
				</thead>
				<tbody>
				<g:each in="${importResults.dataEmployees}" var="employee" status="index">
					<tr>
						<td>${employee.RFC}</td>
						<td>${employee.CURP}</td>
						<td>${employee.NO_EMPL}</td>
						<td>${employee.NOMBRE}</td>
						<td>${employee.CLABE}</td>
						<td>${modulusuno.formatPrice(number:employee.NETO_A_PAGAR)}</td>
						<td>${importResults.results[index]}</td>
					</tr>
				</g:each>
				</tbody>
			</table>
		</div>

  </div>
