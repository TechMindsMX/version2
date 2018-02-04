<%! import com.modulus.uno.PurchaseOrderStatus %>
<g:each in="${purchaseOrder.items.sort{it.id}}" var="item">
<tr>
  <td>${item.quantity}</td>
  <td>${item.name}</td>
  <td>
    <dl class="dl-horizontal">
      <dt>Precio:</dt>
      <dd>${modulusuno.formatPrice(number:item.price)}</dd>
      <dt>Descuento:</dt>
      <dd>${modulusuno.formatPrice(number:item.amountDiscount)}</dd>
      <dt>IVA:</dt>
      <dd>${modulusuno.formatPrice(number:item.amountIVA)}</dd>
      <dt>Retención IVA:</dt>
      <dd>${modulusuno.formatPrice(number:item.ivaRetention)}</dd>
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

