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
              <div class="col-md-1" ><span class="glyphicon glyphicon-question-sign"></span></div>
            </div>
            <div align="right">
              <div class="col-md-4">Fecha del  Formato</div>
              <div class="col-md-2"><input type="datetime-local" name="bdaytime" class= "form-control"></div>
              <div class="col-md-1" ><span class="glyphicon glyphicon-question-sign"></span></div>
            </div>
            <div align="right">
              <div class="col-md-4">Referencia</div>
              <div class="col-md-2"><input type="text" class="form-control"placeholder="Ej. Nº de pedido"></div>
              <div class="col-md-1"><span class="glyphicon glyphicon-question-sign"></span></div>
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
              <div class="col-md-1"><span class="glyphicon glyphicon-question-sign" data-toggle="tooltip" data-placement="top" title="Hooray!"></span></div>
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
                    <h4 class="modal-title">Información de la empresa</h4>
                  </div>
                  <div class="modal-body" align="left">
                    <div class="row">
                      <div class="col-xs-12">
                        <div class="row">
                          <div class="col-xs-6">
                            <div class="form-group">
                              <label for="businfo_business_name">Nombre de la empresa</label>
                              <input type="text" class="form-control" id="businfo_business_name" name="businfo_business_name" placeholder="Nombre de la empresa" value="">
                            </div>
                          </div>
                        </div>
                        <div class="row">
                          <div class="col-xs-6">
                            <div class="form-group">
                              <label for="businfo_first_name">Nombre</label>
                              <input type="text" class="form-control" id="businfo_first_name" name="businfo_first_name" placeholder="Nombre" value="Raymundo Brandon">
                            </div>
                          </div>
                          <div class="col-xs-6">
                            <div class="form-group">
                              <label for="businfo_last_name">Apellidos</label>
                              <input type="text" class="form-control" id="businfo_last_name" name="businfo_last_name" placeholder="Apellidos" value="Vergara">
                            </div>
                          </div>
                        </div>
                        <div class="form-group">
                          <label for="businfo_address_list">Dirección</label>
                          <select class="form-control" id="businfo_address_list" name="businfo_address_list">
                            <option value="1">Rio churubusco, Jardines de Morelos, Mexico, Estado de Mexico 55070, México</option>
                            <option value="0" selected="">No mostrar en el formato de pago</option>
                          </select>
                        </div>
                        <div class="row">
                          <div class="col-xs-6">
                            <div class="form-group">
                              <label for="businfo_phone_list">Teléfono</label>
                              <select class="form-control" id="businfo_phone_list" name="businfo_phone_list">
                                <option value="1">+52 5551820657</option>
                                <option value="0" selected="">No mostrar en el formato de pago</option>
                              </select>
                            </div>
                          </div>
                          <div class="col-xs-6">
                            <div class="form-group">
                              <label for="businfo_fax">Fax</label>
                              <input type="text" class="form-control" id="businfo_fax" name="businfo_fax" placeholder="Fax" value="">
                            </div>
                          </div>
                        </div>
                        <div class="row">
                          <div class="col-xs-6">
                            <div class="form-group"><label for="businfo_email_list">Correo electrónico</label>
                              <select class="form-control" id="businfo_email_list" name="businfo_email_list">
                                <option value="1" selected="">leovergara.dark@gmail.com</option>
                                <option value="0">No mostrar en el formato de pago</option>
                              </select>
                            </div>
                          </div>
                        </div>
                        <div class="row">
                          <div class="col-xs-9">
                            <div class="form-group">
                              <label for="businfo_website">Sitio web</label>
                              <input type="text" class="form-control" id="businfo_website" name="businfo_website" placeholder="Sitio web" value="">
                            </div>
                          </div>
                        </div>
                        <div class="row">
                          <div class="col-xs-6">
                            <div class="form-group">
                              <label for="businfo_tax_id">Número de identificación fiscal</label>
                              <input type="text" class="form-control" id="businfo_tax_id" name="businfo_tax_id" placeholder="Número de identificación fiscal" value="">
                            </div>
                          </div>
                          <div class="col-xs-6">
                            <div class="form-group">
                              <label for="businfo_custom_value">Información adicional</label>
                              <input maxlength="400" type="text" class="form-control" id="businfo_custom_value" name="businfo_custom_value" placeholder="Por ejemplo, su horario comercial" value="">
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="modal-footer">
                    <button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
                  </div>
                </div>
                <!-- Finish Modal contente -->
              </div>
            </div>
            <!--Finish Modal -->
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
            <tfoot>
              <tr class="table-active">
                <td class="bg-info" colspan="5" >
                  <div class="addMoreItemBox"><a href="#" id="addMoreItem" tabindee="-1"><span class="glyphicon glyphicon-plus" ></span> Agregar otra línea de producto</a></div>
                </td>
              </tr>
            </tfoot>
          </table>
          <hr>
          <div class="row total">
            <div class="col-xs-12 left-indent">
              <div align="right">
                <table class="table"style="width:50%">
                  <tbody>
                    <tr>
                      <th colspan="3"style="background:#E6E6E6" >Subtotal</th>
                      <td colspan="3" style="width:30%;"align="right"><input style="text-align:right;" type="text" class="form-control" placeholder="$0.00"></td>
                    </tr>
                    <tr>
                      <th >Descuento</th>
                      <td style="width:30%;"><input style="text-align:right;" type="text" class="form-control" placeholder="$0.00"></td>
                      <td style="width:30%;"><select class="form-control" style="text-align-last:right;"><option>$</option><option>%</option></select></td>
                      <td style="width:30%;"><input style="text-align:right;" type="text" class="form-control" placeholder="$0.00"></td>

                    </tr>
                    <tr>
                      <th>Envió</th>
                      <td colspan="2"style="width:30%;"><input style="text-align:right;" type="text" class="form-control" placeholder="$0.00"></td>
                      <td style="width:30%;"><input style="text-align:right;" type="text" class="form-control" placeholder="$0.00"></td>
                    </tr>
                    <tr>
                      <th colspan="3" style="background:#E6E6E6" >Total</th>
                      <td colspan="3" style="width:10%;" ><input style="text-align:right;" type="text" class="form-control" placeholder="$0.00"></td>
                    </tr>


                  </tbody>

                </table>
              </div>
              <div class="checkbox">
                <label><input type="checkbox" value="">    Permitir pago parcial     <span  data-toggle="tooltip" data-placement="top" title="El cliente podra ingresar un pago"class="glyphicon glyphicon-question-sign"></span> </label>
              </div>
              <hr>
              <div class="row">
                <div class="col-xs-6 left-indent">
                  <div class="form-group"><label for="terms">Nota para el destinatario</label><textarea placeholder="Como, por ejemplo, “Gracias por su preferencia”" rows="5" class="form-control" name="notes" id="notes"></textarea><p class="help-block text-right" id="notesChars">4000</p>
                  </div>
                </div>
                <div class="col-xs-6">
                  <div class="form-group"><label for="notes">Términos y condiciones</label><textarea placeholder="Incluir su política de devoluciones o de cancelación" rows="5" class="form-control" name="terms" id="terms"></textarea><p class="help-block text-right" id="termsChars">4000</p>
                  </div>
                </div>
              </div>
              <hr>
              <div align="right">
                <button type="button" class="btn btn-primary">Vista Previa</button>
                <button type="button" class="btn btn-success">Enviar</button>
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

