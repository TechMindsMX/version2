<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <title>Grupo de Relaciones Comerciales</title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-users fa-3x"></i>
        Grupo de Relaciones Comerciales
      </h1>
    </div>

    <div class="row">
      <div class="col-md-12">
        <div class="portlet portlet-default">
          <div class="portlet-heading">
            <div class="portlet-title">
              <h4>Grupo de Relaciones Comerciales</h4>
            </div>
            <div class="clearfix"></div>
          </div>
          <div id="defaultPortlet" class="panel-collapse collapse in">
            <div class="portlet-body">
              <dl class="dl-horizontal">
                <dt>Empresa:</dt>
                <dd>${businessEntitiesGroup.company}</dd>
                <dt>Descripción:</dt>
                <dd>${businessEntitiesGroup.description}</dd>
                <dt>Tipo:</dt>
                <dd>${businessEntitiesGroup.type}</dd>
              </dl>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class"row">
      <div class"col-md-12">
        <g:render template="listBusinessEntitiesInGroup"/>        
      </div>
    </div>

    <div class"row">
      <div class"col-md-12 text-right">
        
      </div>
    </div>

  </body>
</html>

