<div class="portlet portlet-default">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4>Pagadoras</h4>
    </div>
    <div class="clearfix"></div>
  </div>

  <div class="portlet-body">
    <div class="row">
      <div class="col-md-12">
        <g:link class="btn btn-primary" action="choosePayers" id="${paysheetProject.id}">Agregar Pagadora</g:link>
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
              </tr>
            </thead>
            <tbody>
              <g:each in="${paysheetProject.payers}" var="payer">
                <tr>
                  <td>${payer.company}</td>
                  <td>${payer.schema}</td>
                </tr>
              </g:each>
            </tbody>
          </table>
        </div>
      </div>
    </div>

  </div>
</div>

