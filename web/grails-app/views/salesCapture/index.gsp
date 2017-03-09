<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
  </head>
  <body>
    <div class="page-title">
      <div class="panel panel-primary" style="width:70%; margin:auto;">
        <div class="panel-heading">
          <h2 class="panel-title">Crear formato de pago</h2>
        </div>
        <div class="panel-body">
          Contenido del panel
          <div align="right">
            <button type="button" class="btn btn-primary">Vista Previa</button>
            <button type="button" class="btn btn-success">Enviar</button>
          </div>
          <hr>
          <div class="row">
            <div align= "center">
              <div class= "col-md-4" style="margin-left:3em">
                <img src="https://image.freepik.com/iconos-gratis/plataforma-macos_318-33076.jpg" style="margin-bottom: 100px;" class="img-responsive" alt="Cinque Terre" width="100" height="400">
              </div>
            </div>
            <div align="right">
              <div class="col-md-4">Nº de Formato</div>
              <div class="col-md-2"><input type="text" class="form-control"></div>
              <div class="col-md-1" ><span class="badge">?</span></div>
            </div>
            <div align="right">
              <div class="col-md-4">Fecha del  Formato</div>
              <div class="col-md-2"><input type="datetime-local" name="bdaytime" class= "form-control"></div>
              <div class="col-md-1" ><span class="badge">?</span></div>
            </div>
            <div align="right">
              <div class="col-md-4">Referencia</div>
              <div class="col-md-2"><input type="text" class="form-control"placeholder="Ej. Nº de pedido"></div>
              <div class="col-md-1"><span class="badge">?</span></div>
            </div>
            <div align="right">
              <div class="col-md-4">Vencimiento</div>
              <div class="col-md-2"><select class="form-control">
                  <option>1</option>
                  <option>2</option>
                  <option>3</option>
                  <option>4</option>
                  <option>5</option>
              </select></div>
              <div class="col-md-1"><span data-toggle="tooltip" data-placement="top" title="Hooray!" class="badge">?</span></div>
            </div>
          </div>
          <div align="center">
            <a href="#" data-toggle="modal" data-target="#modalBussines"><span class="label label-primary">Información de la empresa</span></a>
            <!-- Modal -->
            <div class="modal fade" id="modalBussines" role="dialog">
              <div class="modal-dialog">

                <!-- Modal content-->
                <div class="modal-content">
                  <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Empresa Makingdevs</h4>
                  </div>
                  <div class="modal-body" align="left">Makingdevs
                    <p>Texto de la empresa</p>
                  </div>
                  <div class="modal-footer">
                    <button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
                  </div>
                </div>

              </div>
            </div>

          </div>
          <hr>
          <div class="row">
            <div align="left">
              <div class="col-md-2" style="width:100px" >Enviar a: </div>
              <div class="col-md-8"><input type="email" placeholder="email@dominio.com"name="email"required></div>
              <br/>
              <br/>
              <div class="col-md-2" style="width:100px">Cc:  </div>
              <div class="col-md-8"><input type="email" name="email"></div>
            </div>
          </div>
          <hr>
          <table class="table">
            <thead>
              <tr>
                <th style="width:50%;" >Descripción</th>
                <th style="width:10%;" >Cantidad</th>
                <th style="width:10%;">Precio</th>
                <th style="width:10%;">Impuesto</th>
                <th style="width:10%;">Importe</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td><input type="text" class="form-control"placeholder="Nombre del artículo" style="width:100%;" ></input></td>
                <td><input type="text" class="form-control" placeholder="Nº" ></input></td>
                <td><input type="text" class="form-control" placeholder="0.00" ></input></td>
                <td><input type="text" class="form-control" value="IVA: 16%" ></input></td>
                <td><input type="text" class="form-control" placeholder="$ 0.00" ></input></td>
              </tr>
              <tr>
                <td><input type="text" class="form-control"placeholder="Ingrese una descripción del artículo" ></input></td>
              </tr>
            </tbody>
          </table>
          <hr>
          <div class="row total">
            <div class="col-xs-12 left-indent">
              <div align="right">
                <table class="table"style="width:50%">
                  <tbody>
                    <tr>
                    <th colspan="3">Subtotal</th>
                  </tr>
                  </tbody>

                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <script>
    $(document).ready(function(){
      $('[data-toggle="tooltip"]').tooltip();
    });
  </script>
</body>
</html>

