<%! import com.modulus.uno.PurchaseOrderStatus %>
<%! import com.modulus.uno.UnitType %>
<div class="table-responsive">
  <g:form controller="purchaseOrderItem" action="save">
  <table class="table">
    <thead>
      <tr>
        <th>Cantidad</th>
        <th class="col-xs-5">Descripción del producto</th>
        <th class="col-xs-2">Precio Unitario</th>
        <th class="col-xs-3">Unidad de medida</th>
        <th class="col-xs-2">Importe</th>
        <th>&nbsp;</th>
      </tr>
    </thead>
    <tbody>
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
            <input type="text" id="ieps" name="ieps" class="form-control" required="" pattern="[0-9]+(\.[0-9]{2})?" value="0"/>
            <div class="input-group-addon">IEPS</div>
          </div>
          <div class="input-group">
            <div class="input-group-addon">%</div>
            <input type="text" id="iva" name="iva" class="form-control" required="" pattern="[0-9]+(\.[0-9]{2})?" value="16"/>
            <div class="input-group-addon">IVA</div>
          </div>
          <div class="input-group">
            <div class="input-group-addon">$</div>
            <input type="text" id="netprice" name="netprice" class="form-control" value="" readonly=""/>
          </div>
        </td>
        <td>
          <div class="input-group">
            <g:select id="unit"  name="unitType" from="${UnitType.values()}" class="form-control" noSelection="['':'Elija la unidad...']" required="" />
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
            <g:each in="${purchaseOrder.items.sort{it.id}}" var="item">
            <tr>
              <td>${item.quantity}</td>
              <td>${item.name}</td>
              <td>
                <dl class="dl-horizontal">
                  <dt>Precio:</dt>
                  <dd>${modulusuno.formatPrice(number:item.price)}</dd>
                  <dt>IEPS:</dt>
                  <dd>${modulusuno.formatPrice(number:item.amountIEPS)}</dd>
                  <dt>IVA:</dt>
                  <dd>${modulusuno.formatPrice(number:item.amountIVA)}</dd>
                  <dt>Precio Neto:</dt>
                  <dd>${modulusuno.formatPrice(number:item.netPrice)}</dd>
                </dl>
              </td>
              <td>${item.unitType}</td>
              <td class="text-right">${modulusuno.formatPrice(number:item.netAmount)}</td>
              <td class="text-center">
                <g:if test="${purchaseOrder.status == PurchaseOrderStatus.CREADA}">
                <g:link action="deleteItem" id="${item.id}" class="btn btn-danger">
                <i class="fa fa-minus"></i> Quitar
                </g:link>
                </g:if>
              </td>
            </tr>
            </g:each>
    </tbody>
    <tfooter>
    <tr>
      <td colspan="6" class="text-right"><strong>Subtotal</strong></td>
      <td class="text-right">
        ${modulusuno.formatPrice(number:purchaseOrder.subtotal)}
      </td>
    </tr>
    <tr>
      <td colspan="6" class="text-right"><strong>IEPS</strong></td>
      <td class="text-right">${modulusuno.formatPrice(number:purchaseOrder.totalIEPS)}</td>
    </tr>
    <tr>
      <td colspan="6" class="text-right"><strong>IVA</strong></td>
      <td class="text-right">${modulusuno.formatPrice(number:purchaseOrder.totalIVA)}</td>
    </tr>
    <tr>
      <td colspan="6" class="text-right"><strong>Total</strong></td>
      <td class="text-right">${modulusuno.formatPrice(number:purchaseOrder.total)}</td>
    </tr>
    </tfooter>
  </table>
  </g:form>
</div>

