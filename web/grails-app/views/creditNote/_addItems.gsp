<%! import com.modulus.uno.status.CreditNoteStatus %>
<div class="portlet">
  <div class="portlet-heading">
    <h4>Agregar Concepto</h4>
  </div>
  <div class="portlet-body">

    <g:if test="${errors}">
      <ul class="errors alert alert-danger alert-dismissable" role="alert">
        <g:each in="${errors}" var="error">
          <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
        </g:each>
      </ul>
    </g:if>

    <div class="table-responsive">
      <table class="table table-condensed">
        <thead>
          <tr>
            <th class="col-xs-4">Producto</th>
            <th class="col-xs-1">Cantidad</th>
            <th class="col-xs-3">Precio Unitario</th>
            <th class="col-xs-2">Medida</th>
            <th class="col-xs-2">Importe</th>
            <th>&nbsp;</th>
          </tr>
        </thead>
        <tbody>
          <g:if test="${creditNote.status == CreditNoteStatus.CREATED}">
            <g:hiddenField name="creditNoteId" value="${creditNote.id}"/>
            <tr>
              <td>
                <div class="input-group">
                  <g:select class="form-control" name="item" from="${creditNote.saleOrder.items.sort{it.id}}" var="item" optionKey="id" optionValue="name" noSelection="['':'Elija el producto']" required=""/>
                  <input type="hidden" id="name" name="name" value=""/>
                </div>
                <div class="input-group">
                  <div class="input-group-addon">SKU</div>
                  <input type="text" id="sku" name="sku" class="form-control" readOnly=""/>
                </div>
              </td>
              <td>
                <input class="form-control" id="quantity" name="quantity" type="number" min="0.01" step="0.01" />
                <input type="hidden" id="originalQuantity" name="originalQuantity" value=""/>
              </td>
              <td>
                <div class="input-group">
                  <div class="input-group-addon">$</div>
                  <input type="text" id="price" name="price" class="form-control" required="" pattern="[0-9]+(\.[0-9]{1,2})?" title="Ingrese una cantidad en formato correcto (número sin decimales o hasta con 2 decimales)"/>
                  <input type="hidden" id="originalPrice" name="originalPrice" value=""/>
                </div>
                <div class="input-group">
                  <div class="input-group-addon">%</div>
                  <input type="text" id="discount" name="discount" class="form-control" value="0" required="" pattern="[0-9]+(\.[0-9]{1,2})?"/>
                  <div class="input-group-addon">DESC</div>
                </div>
                <div class="input-group">
                  <div class="input-group-addon">%</div>
                  <input type="text" id="iva" name="iva" class="form-control" required="" pattern="[0-9]+(\.[0-9]{1,2})?"/>
                  <div class="input-group-addon">IVA</div>
                </div>
                <div class="input-group">
                  <div class="input-group-addon">$</div>
                  <input type="text" id="ivaRetention" name="ivaRetention" class="form-control" value="0" required="" pattern="[0-9]+(\.[0-9]{1,2})?"/>
                  <div class="input-group-addon">Retención IVA</div>
                </div>
                <div class="input-group">
                  <div class="input-group-addon">$</div>
                  <input type="text" id="netprice" name="netprice" class="form-control" value="" readonly="" required=""/>
                  <input type="hidden" id="originalNetprice" name="originalNetprice" class="form-control" value="" readonly=""/>
                </div>
              </td>
              <td>
                <div class="input-group">
                  <input type="text" id="unitType" name="unitType" class="form-control" value="" readonly=""/>
                </div>
              </td>
              <td>
                <input class="form-control" id="amount" name="amount" type="number" value="" readOnly="" required="" min="0.01"/>
                <input type="hidden" id="originalAmount" name="originalAmount" class="form-control" value="" readonly=""/>
              </td>
              <td class="text-center">
                <button class="btn btn-primary" type="submit">Agregar</button>
              </td>
            </tr>
          </g:if>
        </tbody>
      </table>
    </div>

  </div>
</div>

