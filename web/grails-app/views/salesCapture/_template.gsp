<script  id="partialPaymentTemplate" type="text/x-handlebars-template">
  <div id="rowPartialPayment">
      <div class="row" >
        <h1>Importe minimo de adeudado (opcional)</h1>
      </div>
      <div class="row" >
           <div class="col-md-2">
                  <input type="text" class="form-control" placeholder="0"></input>
            </div>
      </div>
      </div>
</script>
<script id="article-info-row" type="text/x-handlebars-template">
  <tbody>
                  <tr>
                    <td><input type="text" name="name" class="form-control"placeholder="Nombre del artículo" style="width:100%;" ></input></td>
                    <td><input type="text" class="form-control" placeholder="Nº" ></input></td>
                    <td><input type="text" class="form-control" placeholder="0.00" ></input></td>
                    <td><input type="text" class="form-control" value="IVA: 16%" ></input></td>
                    <td><input type="text" class="form-control" placeholder="$ 0.00" ></input></td>
  <td><div class="col-xs-3" style="width:50%; padding:0"><a href="#">
                          <span class="glyphicon glyphicon-remove"></span>
                    </a></div></td>

                  </tr>
                  <tr>
                    <td><input type="text" class="form-control"placeholder="Ingrese una descripción del artículo" ></input></td>
                  </tr>
                </tbody>
</script>
<script id="article-info-row-hours" type="text/x-handlebars-template">
  <tbody>
                  <tr>
                    <td><input type="text" class="form-control"placeholder="Nombre del artículo" style="width:100%;" ></input></td>
                    <td><input type="text" class="form-control" placeholder="Horas" ></input></td>
                    <td><input type="text" class="form-control" placeholder="0.00" ></input></td>
                    <td><input type="text" class="form-control" value="IVA: 16%" ></input></td>
                    <td><input type="text" class="form-control" placeholder="$ 0.00" ></input></td>
  <td><div class="col-xs-3" style="width:50%; padding:0"><a href="#">
                          <span class="glyphicon glyphicon-remove"></span>
                    </a></div></td>

                  </tr>
                  <tr>
                    <td><input type="text" class="form-control"placeholder="Ingrese una descripción del artículo" ></input></td>
                  </tr>
                </tbody>
</script>
<script id="tableHours" type="text/x-handlebars-template">
 <table class="table" id="articles-table">
                <thead>
                  <tr>
                    <th style="width:50%;" >Descripción</th>
                    <th style="width:9%;" >Horas</th>
                    <th style="width:9%;">Tarifa</th>
                    <th style="width:9%;">Impuesto</th>
                    <th style="width:9%;">Importe</th>
                    <th style="width:5%;"></th>
                  </tr>
                </thead>

                <tbody>
                  <tr>
                    <td><input type="text" name="name" class="form-control"placeholder="Nombre del artículo" ></input></td>
                    <td><input type="text" class="form-control" placeholder="Nº" ></input></td>
                    <td><input type="text" class="form-control" placeholder="0.00" ></input></td>
                    <td><input type="text" class="form-control" value="IVA: 16%" ></input></td>
                    <td><input type="text" class="form-control" placeholder="$ 0.00" ></input></td>
                    <td><div class="col-xs-3" style="width:50%; padding:0"><a href="#">
                          <span class="glyphicon glyphicon-remove"></span>
                    </a></div></td>
                  </tr>
                  <tr>
                    <td><input type="text" class="form-control"placeholder="Ingrese una descripción del artículo" ></input></td>
                  </tr>
                </tbody>
                <tfoot>
                  <tr class="table-active">
                    <td class="bg-info" colspan="6" >
                      <div class="addMoreItemBox"><a href="#" class="addMoreItem" id="addMoreItem" tabindee="-1"><span class="addMoreItem glyphicon glyphicon-plus" ></span> Agregar otra línea de producto</a></div>
                    </td>
                  </tr>
                </tfoot>
              </table>

</script>
<script id="tableCount" type="text/x-handlebars-template">
 <table class="table" id="articles-table">
                <thead>
                  <tr>
                    <th style="width:50%;" >Descripción</th>
                    <th style="width:9%;" >Cantidad</th>
                    <th style="width:9%;">Precio</th>
                    <th style="width:9%;">Impuesto</th>
                    <th style="width:9%;">Importe</th>
                    <th style="width:5%;"></th>
                  </tr>
                </thead>

                <tbody>
                  <tr>
                    <td><input type="text" name="name" class="form-control"placeholder="Nombre del artículo" ></input></td>
                    <td><input type="text" class="form-control" placeholder="Nº" ></input></td>
                    <td><input type="text" class="form-control" placeholder="0.00" ></input></td>
                    <td><input type="text" class="form-control" value="IVA: 16%" ></input></td>
                    <td><input type="text" class="form-control" placeholder="$ 0.00" ></input></td>
                    <td><div class="col-xs-3" style="width:50%; padding:0"><a href="#">
                          <span class="glyphicon glyphicon-remove"></span>
                    </a></div></td>
                  </tr>
                  <tr>
                    <td><input type="text" class="form-control"placeholder="Ingrese una descripción del artículo" ></input></td>
                  </tr>
                </tbody>
                <tfoot>
                  <tr class="table-active">
                    <td class="bg-info" colspan="6" >
                      <div class="addMoreItemBox"><a href="#" class="addMoreItem" id="addMoreItem" tabindee="-1"><span class="addMoreItem glyphicon glyphicon-plus" ></span> Agregar otra línea de producto</a></div>
                    </td>
                  </tr>
                </tfoot>
              </table>
</script>
<script id="tableOnlyImport" type="text/x-handlebars-template">
   <table class="table" id="articles-table">
                <thead>
                  <tr>
                    <th style="width:70%;" >Descripción</th>
                    <th style="width:9%;">Impuesto</th>
                    <th style="width:9%;">Importe</th>
                    <th style="width:5%;"></th>
                  </tr>
                </thead>

                <tbody>
                  <tr>
                    <td><input type="text" name="name" class="form-control"placeholder="Nombre del artículo" ></input></td>
                    <td><input type="text" class="form-control" value="IVA: 16%" ></input></td>
                    <td><input type="text" class="form-control" placeholder="$ 0.00" ></input></td>
                    <td><div class="col-xs-3" style="width:50%; padding:0"><a href="#">
                          <span class="glyphicon glyphicon-remove"></span>
                    </a></div></td>
                  </tr>
                  <tr>
                    <td><input type="text" class="form-control"placeholder="Ingrese una descripción del artículo" ></input></td>
                  </tr>
                </tbody>
                <tfoot>
                  <tr class="table-active">
                    <td class="bg-info" colspan="6" >
                      <div class="addMoreItemBox"><a href="#" class="addMoreItem" id="addMoreItem" tabindee="-1"><span class="addMoreItem glyphicon glyphicon-plus" ></span> Agregar otra línea de producto</a></div>
                    </td>
                  </tr>
                </tfoot>
              </table>
</script>
<script id="tbodyOnlyAmount" type="text/x-handlebars-template">
    <tbody>
                  <tr>
                    <td><input type="text" class="form-control"placeholder="Nombre del artículo" style="width:100%;" ></input></td>
                    <td><input type="text" class="form-control" value="IVA: 16%" ></input></td>
                    <td><input type="text" class="form-control" placeholder="$ 0.00" ></input></td>
  <td><div class="col-xs-3" style="width:50%; padding:0"><a href="#">
                          <span class="glyphicon glyphicon-remove"></span>
                    </a></div></td>

                  </tr>
                  <tr>
                    <td><input type="text" class="form-control"placeholder="Ingrese una descripción del artículo" ></input></td>
                  </tr>
                </tbody>
</script>