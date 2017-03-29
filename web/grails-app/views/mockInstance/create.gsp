<!DOCTYPE html>
<html lang="en">
<head>
  <meta name="layout" content="main" />
  <meta charset="UTF-8">
  <title></title>
</head>
<body>
  <div class="page-title">
    <h1>Crear nueva instancia</h1>
  </div>

  <div class="row">
    <div class="col-lg-12">
      <div class="portlet portlet-default">
        <div class="portlet-heading">
          <div class="portlet-title">
            <h4>Registrar nueva</h4>  
          </div>
          <div class="clearfix"></div>
        </div>
      
        <div class="panel-collapse collapse in">
          <div class="portlet-body">
            <g:form action="save" class="form-horizontal">
              <div class="form-group">
                <div class="col-lg-1">
                  <label>Nombre</label>
                </div>
                <div class="col-lg-3">
                  <input type="text" class="form-control" name="name" autocomplete="off" />
                </div>
              </div>
              <div class="form-group">
                <div class="col-lg-12">
                  <button type="submit" class="btn btn-default">Guardar</button>
                </div>
              </div>
            </g:form> 
          </div>
        </div>
      </div>
    </div>
  </div>
</body>
</html>
