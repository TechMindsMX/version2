<!doctype html>
<html>
<head>
  <title>Modulus UNO | Servicios Financieros</title>
  <meta name="layout" content="main">
</head>
<body>

  <div class="page-title">
    <h1>
      <i class="fa fa-tachometer fa-3x"></i>
      Autorizaciones
      <sec:ifAnyGranted roles="ROLE_FICO_EJECUTOR, ROLE_FICO_VISOR">
      <small>Operaciones
      </sec:ifAnyGranted>
      <sec:ifAnyGranted roles="ROLE_AUTHORIZER_VISOR, ROLE_AUTHORIZER_EJECUTOR">
      | Órdenes por Autorizar de la Compañía
    </sec:ifAnyGranted>
  </small>
</h1>
</div>
<div class="row">
  <div class="col-md-6">
    <div class="circle-tile">
      <div class="circle-tile-heading blue">
        <i class="fa icon-retiro fa-3x"></i>
      </div>
      <div class="circle-tile-content blue">
        <div class="circle-tile-description">
          Retiros
          <span>
            <sec:ifAnyGranted roles="ROLE_FICO_EJECUTOR, ROLE_FICO_VISOR">por ejecutar</sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_AUTHORIZER_VISOR, ROLE_AUTHORIZER_EJECUTOR">|| por autorizar</sec:ifAnyGranted>
          </span>
        </div>
        <div class="circle-tile-number ">
          <sec:ifAnyGranted roles="ROLE_FICO_EJECUTOR, ROLE_FICO_VISOR">${cashOutOrderAuthorizedCount}</sec:ifAnyGranted>
          <sec:ifAnyGranted roles="ROLE_AUTHORIZER_VISOR, ROLE_AUTHORIZER_EJECUTOR">${cashOutOrderToAuthorizeCount}</sec:ifAnyGranted>
        </div>
        <div class="text-right">

          <div class="btn-group dropdown">
            <button class="btn btn-primary btn-sm dropdown-toggle" data-toggle="dropdown" aria-expanded="false">Opciones <i class="fa fa-caret-down"></i></button>
            <ul class="dropdown-menu">
              <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR,ROLE_LEGAL_REPRESENTATIVE_VISOR">
              <li><g:link controller="cashOutOrder" params="[status:'AUTHORIZED']" action="list">
                Ver autorizadas
              </g:link></li>
              <li>
                <g:link controller="cashOutOrder" action="list">
                  Ver todas
                </g:link>
              </li>

            </sec:ifAnyGranted>

            <sec:ifAnyGranted roles="ROLE_FICO_EJECUTOR, ROLE_FICO_VISOR">
            <li class="divider"></li>
            <li><g:link controller="cashOutOrder" params="[status:'AUTHORIZED']" action="list">
              Por Ejecutar
            </g:link>
          </li>

        </sec:ifAnyGranted>

        <sec:ifAnyGranted roles="ROLE_AUTHORIZER_VISOR, ROLE_AUTHORIZER_EJECUTOR">
        <li class="divider"></li>
        <li>
          <g:link controller="cashOutOrder" params="[status:'TO_AUTHORIZED']" action="list">
          Ver por autorizar
        </g:link>
      </li>
    </sec:ifAnyGranted>

  </ul>  
</div><!--Close .btn-group-->

</div>
</div>
</div>
</div>

<div class="col-md-6">
  <div class="circle-tile">
    <div class="circle-tile-heading blue">
      <i class="fa icon-factura fa-3x"></i>
    </div>
    <div class="circle-tile-content blue">
      <div class="circle-tile-description">
        Cobranza
        <span>
          <sec:ifAnyGranted roles="ROLE_FICO_EJECUTOR, ROLE_FICO_VISOR">por ejecutar</sec:ifAnyGranted>
          <sec:ifAnyGranted roles="ROLE_AUTHORIZER_VISOR, ROLE_AUTHORIZER_EJECUTOR">|| por autorizar</sec:ifAnyGranted>
        </span>
      </div>
      <div class="circle-tile-number">
        <sec:ifAnyGranted roles="ROLE_FICO_EJECUTOR, ROLE_FICO_VISOR">${saleOrderAuthorizedCount}</sec:ifAnyGranted>
        <sec:ifAnyGranted roles="ROLE_AUTHORIZER_VISOR, ROLE_AUTHORIZER_EJECUTOR">${saleOrderToAuthorizeCount}</sec:ifAnyGranted>
      </div>
      <div class="text-right">
        <div class="btn-group dropdown">
          <button class="btn btn-primary btn-sm dropdown-toggle" data-toggle="dropdown" aria-expanded="false">Opciones <i class="fa fa-caret-down"></i></button>
          <ul class="dropdown-menu dropdown-menu-right">
            <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR,ROLE_LEGAL_REPRESENTATIVE_VISOR">
            <li>
              <g:link controller="saleOrder" params="[status:'AUTORIZADA']" action="list">
              Ver autorizadas
            </g:link>
          </li>
          <li>
            <g:link controller="saleOrder" action="list">
            Ver todas
          </g:link>
        </li>
        <li>
          <g:link controller="saleOrder" params="[status:'CANCELACION_AUTORIZADA']" action="list">
          Ver Cancelaciones por ejecutar <strong>[${saleOrderToCancelBillForExecuteCount}]</strong>
        </g:link>
      </li>
    </sec:ifAnyGranted>
    <sec:ifAnyGranted roles="ROLE_FICO_EJECUTOR, ROLE_FICO_VISOR">
    <li class="divider"></li>
    <li>
      <g:link controller="saleOrder" params="[status:'AUTORIZADA']" action="list">
      Por Ejecutar
    </g:link>
  </li>
  <li>
    <g:link controller="saleOrder" params="[status:'CANCELACION_AUTORIZADA']" action="list">
    Ver Cancelaciones por ejecutar <strong>[${saleOrderToCancelBillForExecuteCount}]</strong>
  </g:link>
</li>
</sec:ifAnyGranted>
<sec:ifAnyGranted roles="ROLE_AUTHORIZER_VISOR, ROLE_AUTHORIZER_EJECUTOR">
<li class="divider"></li>
<li>
  <g:link controller="saleOrder" params="[status:'POR_AUTORIZAR']" action="list">
  Ver por autorizar
</g:link>
</li>
<li>
  <g:link controller="saleOrder" params="[status:'CANCELACION_POR_AUTORIZAR']" action="list">
  Ver Cancelaciones por autorizar <strong>[${saleOrderToCancelBillForAuthorizeCount}]</strong>
</g:link>
</li>
</sec:ifAnyGranted>
</ul>
</div>

</div>
</div>
</div>
</div>
</div>


<div class="row">
  <div class="col-md-6">
    <div class="circle-tile">
      <div class="circle-tile-heading dark-blue">
        <i class="fa icon-proveedor fa-3x"></i>
      </div>

      <div class="circle-tile-content green">
        <div class="circle-tile-description">
          <sec:ifAnyGranted roles="ROLE_FICO_EJECUTOR, ROLE_FICO_VISOR">Pagos</sec:ifAnyGranted>
          <sec:ifAnyGranted roles="ROLE_AUTHORIZER_VISOR, ROLE_AUTHORIZER_EJECUTOR">Compras</sec:ifAnyGranted>
          <span>
            <sec:ifAnyGranted roles="ROLE_FICO_EJECUTOR, ROLE_FICO_VISOR">por ejecutar</sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_AUTHORIZER_VISOR, ROLE_AUTHORIZER_EJECUTOR">por autorizar</sec:ifAnyGranted>
          </span>
        </div>
        <div class="circle-tile-number">
          <sec:ifAnyGranted roles="ROLE_FICO_EJECUTOR, ROLE_FICO_VISOR">${purchaseOrderAuthorizedCount}</sec:ifAnyGranted>
          <sec:ifAnyGranted roles="ROLE_AUTHORIZER_VISOR, ROLE_AUTHORIZER_EJECUTOR">${purchaseOrderToAuthorizeCount}</sec:ifAnyGranted>
        </div>
        <div class="text-right">
          <div class="btn-group">
            <button class="btn btn-primary btn-sm dropdown-toggle" data-toggle="dropdown" aria-expanded="false">Opciones <i class="fa fa-caret-down"></i></button>
            <ul class="dropdown-menu">
             <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR,ROLE_LEGAL_REPRESENTATIVE_VISOR">
             <li>
              <g:link controller="purchaseOrder" action="list" params="[status:'AUTORIZADA']">
              Ver autorizadas
            </g:link>
          </li>
          <li>
            <g:link controller="purchaseOrder" action="list">
            Ver todas
          </g:link>
        </li>
      </sec:ifAnyGranted>
      <sec:ifAnyGranted roles="ROLE_FICO_EJECUTOR, ROLE_FICO_VISOR">
      <li class="divider"></li>
      <li>
        <g:link controller="purchaseOrder" action="list" params="[status:'AUTORIZADA']">
        Por Ejecutar
      </g:link>
    </li>
  </sec:ifAnyGranted>
  <sec:ifAnyGranted roles="ROLE_AUTHORIZER_VISOR, ROLE_AUTHORIZER_EJECUTOR">
  <li>
    <g:link controller="purchaseOrder" action="list" params="[status:'POR_AUTORIZAR']">
    Ver por autorizar
  </g:link>
</li>
</sec:ifAnyGranted>
</ul>
</div>
</div>
</div>
</div>
</div>
<div class="col-md-6">
  <div class="circle-tile">

    <div class="circle-tile-heading blue">
      <i class="fa icon-reembolso fa-3x"></i>
    </div>

    <div class="circle-tile-content blue">
      <div class="circle-tile-description">
        Reembolsos
        <span>
          <sec:ifAnyGranted roles="ROLE_FICO_EJECUTOR, ROLE_FICO_VISOR">por ejecutar</sec:ifAnyGranted>
          <sec:ifAnyGranted roles="ROLE_AUTHORIZER_VISOR, ROLE_AUTHORIZER_EJECUTOR">por autorizar</sec:ifAnyGranted>
        </span>
      </div>
      <div class="circle-tile-number">
        <sec:ifAnyGranted roles="ROLE_FICO_EJECUTOR, ROLE_FICO_VISOR">${moneyBackOrderAuthorizedCount}</sec:ifAnyGranted>
        <sec:ifAnyGranted roles="ROLE_AUTHORIZER_VISOR, ROLE_AUTHORIZER_EJECUTOR">${moneyBackOrderToAuthorizeCount}</sec:ifAnyGranted>
      </div>
      <div class="text-right">
        <div class="btn-group">
          <button class="btn btn-primary btn-sm dropdown-toggle" data-toggle="dropdown" aria-expanded="false">Opciones <i class="fa fa-caret-down"></i></button>
          <ul class="dropdown-menu">
            <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR,ROLE_LEGAL_REPRESENTATIVE_VISOR">
            <li>
              <g:link controller="purchaseOrder" action="listMoneyBackOrders" params="[status:'AUTORIZADA']">
              Ver autorizadas
            </g:link>
          </li>
          <li>
            <g:link controller="purchaseOrder" action="listMoneyBackOrders">
            Ver todas
          </g:link>
        </li>
      </sec:ifAnyGranted>
      <sec:ifAnyGranted roles="ROLE_FICO_EJECUTOR, ROLE_FICO_VISOR">
      <li class="divider"></li>
      <li>
        <g:link controller="purchaseOrder" action="listMoneyBackOrders" params="[status:'AUTORIZADA']">
        Por Ejecutar
      </g:link>
    </li>
  </sec:ifAnyGranted>
  <sec:ifAnyGranted roles="ROLE_AUTHORIZER_VISOR, ROLE_AUTHORIZER_EJECUTOR">
  <li class="divider"></li>
  <li>
    <g:link controller="purchaseOrder" action="listMoneyBackOrders" params="[status:'POR_AUTORIZAR']">
    Ver por autorizar
  </g:link>
</li>
</sec:ifAnyGranted>
</ul>
</div>
</div>
</div>
</div>
</div>

</div><!---Close .row-->
<div class="row">
<div class="col-md-6">
  <div class="circle-tile">

    <div class="circle-tile-heading dark-blue">
      <i class="fa icon-recibo fa-3x"></i>
    </div>

    <div class="circle-tile-content green">
      <div class="circle-tile-description">
        Recibos de Honorarios
        <span>
          <sec:ifAnyGranted roles="ROLE_FICO_EJECUTOR, ROLE_FICO_VISOR">por ejecutar</sec:ifAnyGranted>
          <sec:ifAnyGranted roles="ROLE_AUTHORIZER_VISOR, ROLE_AUTHORIZER_EJECUTOR">por autorizar</sec:ifAnyGranted>
        </span>
      </div>
      <div class="circle-tile-number">
        <sec:ifAnyGranted roles="ROLE_FICO_EJECUTOR, ROLE_FICO_VISOR">${feesReceiptCount}</sec:ifAnyGranted>
        <sec:ifAnyGranted roles="ROLE_AUTHORIZER_VISOR, ROLE_AUTHORIZER_EJECUTOR">${feesReceiptToAuthorizeCount}</sec:ifAnyGranted>
      </div>
      <div class="text-right">
        <div class="btn-group dropup">
        <button class="btn btn-primary btn-sm dropdown-toggle" data-toggle="dropdown" aria-expanded="false">Opciones <i class="fa fa-caret-down"></i></button>
          <ul class="dropdown-menu">
            <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR,ROLE_LEGAL_REPRESENTATIVE_VISOR">
            <li>
              <g:link controller="feesReceipt" action="listToExecute">
              Ver pendientes
            </g:link>
          </li>
          <li>
            <g:link controller="feesReceipt" action="listAll">
            Ver todas
          </g:link>
        </li>
      </sec:ifAnyGranted>
      <sec:ifAnyGranted roles="ROLE_FICO_EJECUTOR, ROLE_FICO_VISOR">
      <li class="divider"></li>
      <li>
        <g:link controller="feesReceipt" action="listToExecute">
        Por Ejecutar
      </g:link>
    </li>
  </sec:ifAnyGranted>
  <sec:ifAnyGranted roles="ROLE_AUTHORIZER_VISOR, ROLE_AUTHORIZER_EJECUTOR">
  <li class="divider"></li>
  <li>
    <g:link controller="feesReceipt" action="listToAuthorize">
    Ver  por autorizar
  </g:link>
</li>
</sec:ifAnyGranted>
</ul>
</div><!--Close .btn-group-->
</div><!--Close .text-right-->
</div><!---Close .circle-tile-content-->
</div><!---Close .circle-tile-->
</div><!---Close .col-md-6-->

<div class="col-md-6">
  <div class="circle-tile">

    <div class="circle-tile-heading blue">
      <i class="fa icon-reembolso fa-3x"></i>
    </div>

    <div class="circle-tile-content blue">
      <div class="circle-tile-description">
        Relaciones Comerciales
        <span>
          <sec:ifAnyGranted roles="ROLE_FICO_EJECUTOR, ROLE_FICO_VISOR">por ejecutar</sec:ifAnyGranted>
          <sec:ifAnyGranted roles="ROLE_AUTHORIZER_VISOR, ROLE_AUTHORIZER_EJECUTOR">por autorizar</sec:ifAnyGranted>
        </span>
      </div>
      <div class="circle-tile-number">
      </div>
      <div class="text-right">
        <div class="btn-group">
          <button class="btn btn-primary btn-sm dropdown-toggle" data-toggle="dropdown" aria-expanded="false">Opciones <i class="fa fa-caret-down"></i></button>
          <ul class="dropdown-menu">
            <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR,ROLE_LEGAL_REPRESENTATIVE_VISOR">
            <li>
              <g:link controller="businessEntity" action="index">
              Ver todas
            </g:link>
          </li>
          <li>
            <g:link controller="businessEntity" action="showToAuthorizeEntities">
            Ver por autorizar
          </g:link>
        </li>
      </sec:ifAnyGranted>
      <sec:ifAnyGranted roles="ROLE_FICO_EJECUTOR, ROLE_FICO_VISOR">
  </sec:ifAnyGranted>
</ul>
</div>
</div>
</div>
</div>
</div>

</div>


</body>
</html>
