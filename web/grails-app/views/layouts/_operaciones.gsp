
<ul class="collapse nav" id="operaciones-${action}">
  <li>
    <a href="javascript:;" data-parent="#operaciones" data-toggle="collapse" class="accordion-toggle" data-target="#ordenesDeposito-${action}">
      Depósitos <i class="fa fa-caret-down"></i>
    </a>
      <li>
        <ul class="collapse nav" id="ordenesDeposito-${action}" style="padding-left:2em;">
          <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR,ROLE_OPERATOR_EJECUTOR">
          <li>
            <g:link controller="depositOrder" action="create">Nueva</g:link>
          </li>
          </sec:ifAnyGranted>
          <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_VISOR,ROLE_OPERATOR_VISOR">
          <li>
            <g:link controller="depositOrder" action="list"> Listado</g:link>
          </li>
          </sec:ifAnyGranted>
        </ul>
      </li>
  </li>
  <li>
  <li>
    <a href="javascript:;" data-parent="#operaciones" data-toggle="collapse" class="accordion-toggle" data-target="#cashoutOrder-${action}">
      Retiros <i class="fa fa-caret-down"></i>
    </a>
      <li>
        <ul class="collapse nav" id="cashoutOrder-${action}" style="padding-left:2em;">
          <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR,ROLE_OPERATOR_EJECUTOR">
          <li>
            <g:link controller="cashOutOrder" action="create">Nueva</g:link>
          </li>
          </sec:ifAnyGranted>
          <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_VISOR,ROLE_OPERATOR_VISOR">
          <li>
            <g:link controller="cashOutOrder" action="list">Listado</g:link>
          </li>
          </sec:ifAnyGranted>
        </ul>
      </li>
  </li>
  <li>
    <a href="javascript:;" data-parent="#operaciones" data-toggle="collapse" class="accordion-toggle" data-target="#saleOrder-${action}">
      Órdenes de Venta <i class="fa fa-caret-down"></i>
    </a>
      <li>
        <ul class="collapse nav" id="saleOrder-${action}" style="padding-left:2em;">
          <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR,ROLE_OPERATOR_EJECUTOR">
          <li>
            <g:link controller="saleOrder" action="create">Nueva</g:link>
          </li>
          </sec:ifAnyGranted>
          <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_VISOR,ROLE_OPERATOR_VISOR">
          <li>
            <g:link controller="saleOrder" action="list">Listado</g:link>
          </li>
          </sec:ifAnyGranted>
        </ul>
      </li>
  </li>


  <li>
    <a href="javascript:;" data-parent="#operaciones" data-toggle="collapse" class="accordion-toggle" data-target="#ordenesCompra-${action}">
      Órdenes de Compra <i class="fa fa-caret-down"></i>
    </a>
      <li>
        <ul class="collapse nav" id="ordenesCompra-${action}" style="padding-left:2em;">
          <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR,ROLE_OPERATOR_EJECUTOR">
          <li>
            <g:link controller="purchaseOrder" action="create">Nueva</g:link>
          </li>
          </sec:ifAnyGranted>
          <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_VISOR,ROLE_OPERATOR_VISOR">
          <li>
            <g:link controller="purchaseOrder" action="list"> Listado</g:link>
          </li>
          </sec:ifAnyGranted>
        </ul>
      </li>
  </li>
  <li>
    <a href="javascript:;" data-parent="#operaciones" data-toggle="collapse" class="accordion-toggle" data-target="#ordenesReembolso-${action}">
      Reembolsos a Empleados <i class="fa fa-caret-down"></i>
    </a>
      <li>
        <ul class="collapse nav" id="ordenesReembolso-${action}" style="padding-left:2em;">
          <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR,ROLE_OPERATOR_EJECUTOR">
          <li>
            <g:link controller="purchaseOrder" action="createMoneyBackOrder">Nueva</g:link>
          </li>
          </sec:ifAnyGranted>
          <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_VISOR,ROLE_OPERATOR_VISOR">
          <li>
            <g:link controller="purchaseOrder" action="listMoneyBackOrders"> Listado</g:link>
          </li>
          </sec:ifAnyGranted>
        </ul>
      </li>
  </li>
  <li>
    <a href="javascript:;" data-parent="#consultas" data-toggle="collapse" class="accordion-toggle" data-target="#feesReceipt-${action}">
      Recibo de Honorarios<i class="fa fa-caret-down"></i>
    </a>
      <li>
        <ul class="collapse nav" id="feesReceipt-${action}" style="padding-left:2em;">
          <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_VISOR,ROLE_OPERATOR_VISOR">
          <li>
            <g:link controller="feesReceipt" action="list">
            Todas</a>
            </g:link>
          </li>
          <li>
            <g:link controller="feesReceipt" action="list" params="[status:'CREADA']">
            Creadas
            </g:link>
          </li>
          </sec:ifAnyGranted>
        </ul>
      </li>
  </li>
  <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_VISOR,ROLE_OPERATOR_VISOR">
    <li>
      <g:link controller="movimientosBancarios" >Movimiento</g:link>
    </li>
  </sec:ifAnyGranted>
  <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR,ROLE_OPERATOR_EJECUTOR">
    <li>
      <g:link controller="movimientosBancarios" action="multiMovimientos" >Subir Movimientos Bancarios</g:link>
    </li>
  </sec:ifAnyGranted>
</ul>
