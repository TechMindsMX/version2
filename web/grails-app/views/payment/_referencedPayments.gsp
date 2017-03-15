<div class="table-responsive">
  <table class="table">
    <tr>
      <th>Fecha del Pago</th>
      <th>Monto</th>
      <th>Cliente</th>
      <th>Tipo de Conciliación</th>
      <th></th>
    </tr>
    <g:form action="chooseInvoice" id="4">
    <tr>
      <td>10/02/2017</td>
      <td>${modulusuno.formatPrice(number: 17000)}</td>
      <td>Cliente Equis</td>
      <td>
        <g:radio name="conciliationType" value="1" checked="true"/> Pago completo<br/>
        <g:radio name="conciliationType" value="2"/> Pago parcial<br/>
        <g:radio name="conciliationType" value="3"/> Pago múltiple
      </td>
      <td class="text-center">
        <button class="btn btn-primary">Elegir Factura</button>
      </td>
    </tr>
    </g:form>
  </table>
</div>

