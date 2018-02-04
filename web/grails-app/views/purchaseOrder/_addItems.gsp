<%! import com.modulus.uno.PurchaseOrderStatus %>
<g:form controller="purchaseOrderItem" action="save">
  <g:if test="${purchaseOrder.status == PurchaseOrderStatus.CREADA}">
    <g:hiddenField name="purchaseOrder.id" value="${purchaseOrder.id}"/>
    <tr>
      <td>
        <div class="input-group">
          <input id="quantity" name="quantity" class="form-control" type="text" min="1" required="" pattern="[0-9]+(\.[0-9]{2})?" title="Ingrese una cantidad en formato correcto"/>
        </div>
      </td>
      <td>
        <div class="input-group">
          <g:textField id="product-name" name="name" class="form-control" required="" placeholder="Nombre del producto" style="width:300px" maxLength="300"/>
        </div>
      </td>
      <td>
        <div class="input-group">
          <div class="input-group-addon">$</div>
          <input type="text" id="price" name="price" required="" pattern="[0-9]+(\.[0-9]{2})?" title="Ingrese una cantidad en formato correcto" style="width:100px;height:35px"/>
        </div>
        <div class="input-group">
          <div class="input-group-addon">%</div>
          <input type="text" id="discount" name="discount" class="form-control" required="" pattern="[0-9]+(\.[0-9]{2})?" value="0"/>
          <div class="input-group-addon">Descuento:</div>
        </div>
        <div class="input-group">
          <div class="input-group-addon">%</div>
          <input type="text" id="iva" name="iva" class="form-control" required="" pattern="[0-9]+(\.[0-9]{2})?" value="16"/>
          <div class="input-group-addon">IVA</div>
        </div>
        <div class="input-group">
          <div class="input-group-addon">$</div>
          <input type="text" id="ivaRetention" name="ivaRetention" class="form-control" value="0" required="" pattern="[0-9]+(\.[0-9]{1,2})?"/>
          <div class="input-group-addon">Retención IVA</div>
        </div>
        <div class="input-group">
          <div class="input-group-addon">$</div>
          <input type="text" id="netprice" name="netprice" class="form-control" value="" readonly=""/>
        </div>
      </td>
      <td>
        <div class="input-group">
          <g:select id="unit"  name="unitType" from="${unitTypes.sort{it.name}}" optionKey="name" optionValue="name" class="form-control" noSelection="['':'Elija la unidad...']" required="" />
        </div>
      </td>
      <td>
        <div class="input-group">
          <input type="text" id="amount" name="amount" value="0" readonly="" class="form-control" />
        </div>
      </td>
      <td>
        <div class="input-group">
          <button type="submit" class="btn btn-primary">
            <i class="fa fa-plus"></i> Agregar
          </button>
        </div>
      </td>
    </tr>
  </g:if>
</g:form>
