<div class="portlet portlet-default">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4>Facturadoras</h4>
    </div>
    <div class="clearfix"></div>
  </div>

  <div class="portlet-body">
    <div class="row">
      <div class="col-md-12">
        <g:link class="btn btn-primary" action="chooseBillers" id="${paysheetProject.id}">Agregar Facturadora</g:link>
      </div>
    </div>

    <div class="row">
      <div class="col-md-12">
        <div class="table-responsive">
          <table class="table table-striped table-condensed">
            <thead>
              <tr>
                <th class="text-center">Empresa</th>
                <th class="text-center">Esquema</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              <g:each in="${paysheetProject.billers.sort{it.company.bussinessName}}" var="biller">
                <tr>
                  <td>${biller.company}</td>
                  <td class="text-center">${biller.paymentSchema}</td>
                  <td class="text-center"><g:link class="btn btn-danger" action="deleteBiller" id="${biller.id}">Quitar</g:link>
                </tr>
              </g:each>
            </tbody>
          </table>
        </div>
      </div>
    </div>

  </div>
</div>

