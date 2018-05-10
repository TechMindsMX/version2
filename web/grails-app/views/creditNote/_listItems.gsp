<div class="table-responsive">
  <table class="table table-condensed">
    <tbody>
      <g:each in="${creditNote.items.sort{it.id}}" var="item">
      <tr>
        <td class="col-xs-4">
          ${item.name}<br/>
          <small><i>${item.sku}</i></small><br/>
          <small><i>${item.satKey}</i></small>
        </td>
        <td class="col-xs-1">${item.quantity}</td>
        <td class="col-xs-3">
          <dl class="dl-horizontal">
            <dt>Precio:</dt>
            <dd>${modulusuno.formatPrice(number:item.price)}</dd>
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
        <td class="col-xs-2">${item.unitType}</td>
        <td class="col-xs-2">
          <strong>${modulusuno.formatPrice(number:item.netAmount)}</strong>
        </td>
        <td>&nbsp;</td>
      </tr>
      </g:each>
    </tbody>
    <tr>
      <td colspan="5" class="text-right"><strong>Subtotal</strong></td>
      <td class="text-right">
        ${modulusuno.formatPrice(number:creditNote.subtotal)}
      </td>
    </tr>
    <tr>
      <td colspan="5" class="text-right"><strong>Descuentos</strong></td>
      <td class="text-right">
        ${modulusuno.formatPrice(number:creditNote.totalDiscount)}
      </td>
    </tr>
    <tr>
      <td colspan="5" class="text-right"><strong>IVA</strong></td>
      <td class="text-right">
        ${modulusuno.formatPrice(number:creditNote.totalIVA)}
      </td>
    </tr>
    <tr>
      <td colspan="5" class="text-right"><strong>Retención IVA</strong></td>
      <td class="text-right">
        ${modulusuno.formatPrice(number:creditNote.totalIvaRetention)}
      </td>
    </tr>
    <tr>
      <td colspan="5" class="text-right"><strong>Total</strong></td>
      <td class="text-right">
        <strong>
          ${modulusuno.formatPrice(number:creditNote.total)}
        </strong>
      </td>
    </tr>

  </table>
</div>
