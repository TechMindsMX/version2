
<ul class="collapse nav" id="operaciones-${action}">
  <li>
  <li>
    <a href="javascript:;" data-parent="#operaciones" data-toggle="collapse" class="accordion-toggle" data-target="#cashoutOrder-${action}">
      Retiros <i class="fa fa-caret-down"></i>
    </a>
      <li>
        <ul class="collapse nav" id="cashoutOrder-${action}">
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
        <ul class="collapse nav" id="saleOrder-${action}">
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
        <ul class="collapse nav" id="ordenesCompra-${action}">
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
        <ul class="collapse nav" id="ordenesReembolso-${action}">
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
        <ul class="collapse nav" id="feesReceipt-${action}">
          <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_VISOR,ROLE_OPERATOR_VISOR">
          <li>
            <g:link controller="feesReceipt" action="list">Todas</g:link>
          </li>
          <li>
            <g:link controller="feesReceipt" action="list" params="[status:'CREADA']">Creadas</g:link>
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

  <sec:ifAnyGranted roles="ROLE_OPERATOR_EJECUTOR">
  <li>
    <a href="javascript:;" data-parent="#consultas" data-toggle="collapse" class="accordion-toggle" data-target="#paysheet-${action}">
      Nómina<i class="fa fa-caret-down"></i>
    </a>
      <li>
        <ul class="collapse nav" id="paysheet-${action}">
          <li>
            <g:link controller="prePaysheet" action="create">Crear Pre-Nómina</a></g:link>
          </li>
          <li>
            <g:link controller="prePaysheet" action="list">Lista de Pre-Nóminas</a></g:link>
          </li>
          <li>
            <g:link controller="paysheet" action="list">Lista de Nóminas</a></g:link>
          </li>
        </ul>
      </li>
  </li>
  </sec:ifAnyGranted>


</ul>
