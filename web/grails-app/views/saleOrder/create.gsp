<%! import com.modulus.uno.PaymentWay %>
<%! import com.modulus.uno.AddressType %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'saleOrder.label', default: 'SaleOrder')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
  </head>
  <body>

    <div class="page-title">
      <h1>
        <i class="icon-factura fa-3x"></i>
        Operaciones / Facturación
        <small>Creación de una orden de venta</small>
      </h1>
      <ol class="breadcrumb">
        <li><i class="fa fa-caret-square-o-up"></i> ${company}</li>
        <li class="active">Datos de la venta</li>
      </ol>
    </div>

    <div class="row">
      <div class="col-lg-6">
        <div class="portlet portlet-default">
          <div class="portlet-heading">
            <div class="portlet-title">
              <h4>La empresa cliente es...</h4>
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
              <g:form action="searchClientForSale" class="form-horizontal">
              <div class="form-group">
                <div class="col-sm-9">
                  <input type="text" name="q" class="form-control" value="${q}" placeholder="RFC ó Nombre" maxlength="20"/>
                  <input type="hidden" name="companyId" value="${company.id}" />
                </div>
                <div class="col-sm-2">
                  <button class="btn btn-primary">Buscar</button>
                </div>
              </div>
              </g:form>
              <g:if test="${clients}">
              <p class="text-muted">
              Selecciona un cliente...
              </p>
              <div class="table-responsive">
                <table class="table">
                  <thead>
                    <tr>
                      <th>#</th>
                      <th>RFC</th>
                      <th>Razón social</th>
                    </tr>
                  </thead>
                  <tbody>
                    <g:each in="${clients.sort{it.rfc}}" var="client" status="index">
                    <tr>
                      <td>${index+1}</td>
                      <td>
                        <g:link action="chooseClientForSale" id="${client.id}" params="[companyId:company.id,q:q]">
                        ${client.rfc}
                        </g:link>
                      </td>
                      <td>
                        <g:link action="chooseClientForSale" id="${client.id}" params="[companyId:company.id,q:q]">
                        ${client}
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

      <g:if test="${client}">
      <g:form action="save">
      <div class="col-lg-6">
        <div class="portlet portlet-default">
          <div class="portlet-heading">
            <div class="portlet-title">
              <h4>${client}</h4>
            </div>
            <div class="clearfix"></div>
          </div>
          <div id="defaultPortlet" class="panel-collapse collapse in">
            <div class="portlet-body">
              <p><strong>${client.rfc}</strong></p>
              <p>
              <g:if test="${client.addresses.find {it.addressType == AddressType.FISCAL}}">
              Direccion de facturación:
              <input type="hidden" name="addressId" value="${(client.addresses.find {it.addressType == AddressType.FISCAL}).id}"/>
              <p>${client.addresses.find {it.addressType == AddressType.FISCAL}}</p>
              <input type="hidden" name="companyId" value="${company.id}" />
              <input type="hidden" name="clientId" value="${client.id}" />
              </p>
              <div class="form-group">
                <input type="checkbox" id="currencyUsd" name="currencyUsd" value="USD" />&nbsp;&nbsp;<label>Factura en Dólares</label>
              </div>
              <div class="">
                <label>Fecha de Cobro:</label>
                <input type="text" id="datepicker" name="fechaCobro" required="required">
              </div>
              <br />
              <div class="form-group">
                <label>Notas:</label>
                <textarea class="form-control" rows="4" cols="50" name="note" id="note" ></textarea>
              </div>
              <br/>
              <div class="form-group">
                <label>Forma de Pago:</label>
                <g:select name="paymentWay" from="${PaymentWay.values()}"/>
              </div>
              <p>
             <p>
              <button type="submit" class="btn btn-green btn-block">
                Confirmar cliente/cuenta y agregar productos/servicios
              </button>
              </p>
              </g:if>
              <g:else>
                  <p>El cliente seleccionado no tiene dirección Fiscal registrada</p>
                  <g:link controller="businessEntity" action="show" id="${client.id}" class="btn btn-green btn-block">
                      Registrar Dirección
                  </g:link>
              </g:else>
            </div>
          </div>
        </div>
      </div>
      </g:form>
      </g:if>
    </div>
    <asset:javascript src="saleOrder/create.js"/>
  </body>
</html>
