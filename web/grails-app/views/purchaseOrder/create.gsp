<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'purchaseOrder.label', default: 'PurchaseOrder')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
  </head>
  <body>

    <g:if test="${purchaseOrder.isMoneyBackOrder}">
      <g:set var="messageOrder" value="Orden de Reembolso"/>
      <g:set var="messageBusinessEntityOrder" value="Empleado"/>
    </g:if><g:else>
      <g:set var="messageOrder" value="Orden de Compra"/>
      <g:set var="messageBusinessEntityOrder" value="Proveedor"/>
    </g:else>

    <div class="page-title">
      <h1>
        <i class="fa fa-shopping-basket fa-3x"></i>
        Operaciones / ${messageOrder}
        <small>Creación de una ${messageOrder}</small>
      </h1>
    </div>

    <div class="row">
      <div class="col-lg-6">
        <div class="portlet portlet-default">
          <div class="portlet-heading">
            <div class="portlet-title">
              <h4>El ${messageBusinessEntityOrder} es...</h4>
            </div>
            <div class="clearfix"></div>
          </div>
          <div id="defaultPortlet" class="panel-collapse collapse in">
            <div class="portlet-body">
              <g:if test="${flash.message}">
                <div class="alert alert-warning">
                ${flash.message}
                </div>
              </g:if>
              <g:form action="searchProviderForPurchase" class="form-horizontal">
              <div class="form-group">
                <div class="col-sm-9">
                  <input type="text" name="q" class="form-control" value="${q}" placeholder="RFC ó Nombre" maxlength="20"/>
                  <input type="hidden" name="isMoneyBackOrder" value="${purchaseOrder.isMoneyBackOrder}"/>
                </div>
                <div class="col-sm-2">
                  <button class="btn btn-default">Buscar</button>
                </div>
              </div>
              </g:form>
              <g:if test="${providers}">
              <p class="text-muted">
                Selecciona un ${messageBusinessEntityOrder}...
              </p>
              <div class="table-responsive">
                <table class="table">
                  <thead>
                    <tr>
                      <th>#</th>
                      <th>RFC</th>
                      <th>
                        <g:if test="${purchaseOrder.isMoneyBackOrder}">
                          Nombre
                        </g:if><g:else>
                          Razón social
                        </g:else>
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    <g:each in="${providers.sort{it.rfc}}" var="provider" status="index">
                    <tr>
                      <td>${index+1}</td>
                      <td>
                        <g:link action="chooseProviderForPurchase" id="${provider.id}" params="[isMoneyBackOrder:purchaseOrder.isMoneyBackOrder,q:q]">
                        ${provider.rfc}
                        </g:link>
                      </td>
                      <td>
                        <g:link action="chooseProviderForPurchase" id="${provider.id}" params="[isMoneyBackOrder:purchaseOrder.isMoneyBackOrder,q:q]">
                        ${provider}
                        </g:link>
                      </td>
                    </tr>
                    </g:each>
                  </tbody>
                </table>
              </div>
              </g:if>
            </div>
          </div>
          <!--div class="portlet-footer">

          </div-->
        </div>
      </div>

      <g:if test="${provider}">
      <g:form action="save">
      <div class="col-lg-6">
        <div class="portlet portlet-default">
          <div class="portlet-heading">
            <div class="portlet-title">
              <h4>${provider}</h4>
            </div>
            <div class="clearfix"></div>
          </div>
          <div id="defaultPortlet" class="panel-collapse collapse in">
            <div class="portlet-body">
              <g:if test="${!purchaseOrder.isMoneyBackOrder}">
              <g:radio name="orderType" value="0" />&nbsp;&nbsp;Compra sin Factura
              <br/>
              <g:radio name="orderType" value="1" />&nbsp;&nbsp;Recibo de Honorarios
              <br/><br/>
              </g:if>

              <input type="hidden" name="providerId" value="${provider.id}" />
              <input type="hidden" name="isMoneyBackOrder" value="${purchaseOrder.isMoneyBackOrder}"/>
              <input type="hidden" name="company" value="${params.company.id}"/>
              <div class="">
                <label>Fecha de Pago<font color="red"> *Requerida</font></label>
                <input type="text" id="datepicker" name="fechaPago" required="required" autocomplete="off">
              </div>
              <br/>
              <div class="form-group">
                <label>Notas</label>
                <textarea class="form-control" rows="4" cols="50" name="note" id="note" ></textarea>
              </div>
              <br/>
              <p>
              <p><strong>${provider.rfc}</strong></p>
              <g:if test="${provider.banksAccounts}">
               <p>Elige la Cuenta:
               <g:radioGroup name="bankAccountId" values="${provider.banksAccounts*.id}"
                    labels="${provider.banksAccounts}" value="${provider.banksAccounts*.id.first()}" >
                  <p>${it.radio}  ${it.label} </p>
                </g:radioGroup>
                <button type="submit" class="btn btn-green">
                  Confirmar ${messageBusinessEntityOrder}/Cuenta y agregar detalle de orden
                </button>
                </p>
              </g:if>
              <g:else>
              <p>El ${messageBusinessEntityOrder} seleccionado no tiene cuentas de banco registradas</p>
                  <g:link controller="businessEntity" action="show" id="${provider.id}" class="btn btn-green btn-block">
                      Registrar Cuentas Bancarias
                  </g:link>
              </g:else>
           </div>
          </div>
          </div>
        </div>
      </div>
      </g:form>
      </g:if>
    </div>

    <asset:javascript src="purchaseOrder/create.js"/>
  </body>
</html>
