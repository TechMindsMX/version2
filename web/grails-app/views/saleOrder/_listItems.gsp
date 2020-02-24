<%! import com.modulus.uno.status.SaleOrderStatus %>

<div class="table-responsive">
  <table class="table table-condensed">
    <tbody>
    <g:each in="${saleOrder.items.sort{it.id}}" var="item">
    <tr>
      <td>${item.quantity}</td>
      <td>
        ${item.name}<br/>
        <small><i>${item.sku}</i></small><br/>
        <small><i>${item.satKey}</i></small>
      </td>
      <td>
        <dl class="dl-horizontal">
          <dt>Precio:</dt>
          <dd>${modulusuno.formatPrice(number:item.price, decimals: 4)}</dd>
          <dt>Descuento:</dt>
          <dd>${modulusuno.formatPrice(number:item.amountDiscount)}</dd>
          <dt>IVA:</dt>
          <dd>${modulusuno.formatPrice(number:item.amountIVA)}</dd>
          <dt>Retención IVA:</dt>
          <dd>${modulusuno.formatPrice(number:item.ivaRetention)}</dd>
          <dt>Neto:</dt>
          <dd>${modulusuno.formatPrice(number:item.netPrice)}</dd>
        </dl>
      </td>
      <td>${item.unitType}</td>
      <td class="text-right">
        <strong>${modulusuno.formatPrice(number:item.netAmount)}</strong>
      </td>
      <td class="text-center">
        <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR,ROLE_OPERATOR_EJECUTOR">
        <g:if test="${saleOrder.status == SaleOrderStatus.CREADA}">
        <g:link action="deleteItem" id="${item.id}" class="btn btn-danger">
        <i class="fa fa-minus"></i> Quitar
        </g:link>
        </g:if>
        </sec:ifAnyGranted>
      </td>
    </tr>
    </g:each>
    </tbody>
    <tr>
      <td colspan="5" class="text-right"><strong>Subtotal</strong></td>
      <td class="text-right">
        ${modulusuno.formatPrice(number:saleOrder.subtotal)}
      </td>
    </tr>
    <tr>
      <td colspan="5" class="text-right"><strong>Descuentos</strong></td>
      <td class="text-right">
        ${modulusuno.formatPrice(number:saleOrder.totalDiscount)}
      </td>
    </tr>
    <tr>
      <td colspan="5" class="text-right"><strong>IVA</strong></td>
      <td class="text-right">
        ${modulusuno.formatPrice(number:saleOrder.totalIVA)}
      </td>
    </tr>
    <tr>
      <td colspan="5" class="text-right"><strong>Retención IVA</strong></td>
      <td class="text-right">
        ${modulusuno.formatPrice(number:saleOrder.totalIvaRetention)}
      </td>
    </tr>
    <tr>
      <td colspan="5" class="text-right"><strong>Total</strong></td>
      <td class="text-right">
        <strong>
          ${modulusuno.formatPrice(number:saleOrder.total)}
        </strong>
      </td>
    </tr>
  </table>
</div>

