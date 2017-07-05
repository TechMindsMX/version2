<div class="portlet portlet-default">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4>Proyectos de Nómina</h4>
    </div>
    <div class="clearfix"></div>
  </div>
  <div id="defaultPortlet" class="panel-collapse collapse in">
    <div class="portlet-body">
      <div class="row">
        <div class="col-md-12 text-right">
          <g:link class="btn btn-default" controller="paysheetProject" action="create" params="[companyId:company.id]">Nuevo</g:link>
        </div>
      </div>
      <g:if test="${company.paysheetProjects}">
      <div class="table-responsive">
        <table class="table">
          <g:each in="${company.paysheetProjects}" var="project">
          </g:each>
        </table>
      </div>
      </g:if>
    </div>
  </div>
</div>

