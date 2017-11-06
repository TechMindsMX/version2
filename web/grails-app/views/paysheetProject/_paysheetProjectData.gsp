<div class="portlet portlet-default">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4>Datos del Proyecto de Nómina</h4>
    </div>
    <div class="clearfix"></div>
  </div>

  <div class="portlet-body">

      <div class="row">
        <div class="col-md-6">
          <label><strong><g:message code="paysheetProject.label.name"/></strong></label><br/>
          <label>${paysheetProject.name}</label>
        </div>
        <div class="col-md-6">
          <label><strong><g:message code="paysheetProject.label.description"/></strong></label><br/>
          <label>${paysheetProject.description}</label>
        </div>       
      </div>
      <div class="row">
        <div class="col-md-4">
          <label><strong><g:message code="paysheetProject.label.integrationFactor"/></strong></label><br/>
          <label><g:formatNumber format="#,##0.000000" number="${paysheetProject.integrationFactor}" locale="es_MX"/></label>
        </div>
        <div class="col-md-4">
          <label><strong><g:message code="paysheetProject.label.occupationalRiskRate"/></strong></label><br/>
          <label><g:formatNumber format="#,##0.000000" number="${paysheetProject.occupationalRiskRate}" locale="es_MX"/></label>
        </div>
        <div class="col-md-4">
          <label><strong><g:message code="paysheetProject.label.commission"/></strong></label><br/>
          <label><g:formatNumber format="#,##0.000000" number="${paysheetProject.commission}" locale="es_MX"/></label>
        </div>
      </div>

  </div>

  <div class="portlet-footer">
    <div class="row">
      <div class="col-md-12 text-right">
        <g:link class="btn btn-primary" action="edit" id="${paysheetProject.id}">Editar</g:link>
      </div>
    </div>
  </div>

</div>
 
