<!DOCTYPE html>
<html lang="en">
<head>
  <meta name="layout" content="main" />
  <meta charset="UTF-8">
  <title></title>
</head>
<body>
 <div class="page-title">
    <h1>Detalle Instancia</h1>
  </div>

  <div class="row">
    <div class="col-lg-12">
      <div class="portlet portlet-default">
        <div class="portlet-heading">
          <div class="portlet-title">
            <h4>Detalle de la Instancia</h4>  
          </div>
          <div class="clearfix"></div>
        </div>
      
        <div class="panel-collapse collapse in">
          <div class="portlet-body">
            <div class="row">
              <div class="col-lg-1">
                <label>Nombre</label>
              </div>
              <div class="col-lg-3">
                ${mockInstance.name}    
              </div>
            </div>
            <div class="row">
              <div class="col-lg-12">
                <g:each in="${actions}" var="action">
                  <g:link action="advance" params="[currentAction:"${action}"]" id="${mockInstance.id}" class="btn btn-primary">${action}</g:link>
                </g:each>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
 
</body>
</html>
