<%! import com.modulus.uno.PurchaseOrderStatus %>
<%! import com.modulus.uno.RejectReason %>
<%! import com.modulus.uno.UnitType %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'purchaseOrder.label', default: 'PurchaseOrder')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <g:if test="${purchaseOrder.isMoneyBackOrder}">
      <g:set var="messageOrder" value="Orden de Reembolso"/>
      <g:set var="messageBusinessEntityOrder" value="Empleado"/>
      <g:set var="messageDocuments" value="PDF, XML, IMAGEN(PNG,JPG)"/>
    </g:if><g:else>
      <g:set var="messageOrder" value="Orden de Compra"/>
      <g:set var="messageBusinessEntityOrder" value="Proveedor"/>
      <g:set var="messageDocuments" value="PDF y XML"/>
    </g:else>

    <div class="page-title">
      <h1>
        <i class="fa fa-shopping-basket fa-3x"></i>
        Detalle de la ${messageOrder}
      </h1>
    </div>


    <div class="row">
      <g:render template="datosBusinessEntity" />
      <g:render template="datosOrder" />
      <g:render template="pagosParciales" />
    </div>


    <g:if test="${!purchaseOrder.bankAccount}">
      <div class="alert alert-danger">
        La Orden de ${messageOrder} no tiene cuenta bancaria definida
      </div>
    </g:if>
   <p></p>


    <div class="row">
      <g:if test="${flash.message}">
        <div class="alert alert-info">${flash.message}</div>
      </g:if>
      <g:if test="${insufficientBalance}">
        <div class="alert alert-danger" role="alert">${insufficientBalance}</div>
      </g:if>
      <g:if test="${amountExcceds}">
        <div class="alert alert-danger" role="alert">${amountExcceds}</div>
      </g:if>
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
              <td><h3>${item.quantity}</h3></td>
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
    </div>

    <div class="row">
      <g:if test="${params.badfile}">
        <div class="alert alert-warning">${params.badfile}</div>
      </g:if>
     <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR,ROLE_OPERATOR_EJECUTOR">
      <g:if test="${enableAddDocuments}">
      <h3>Agregar documentos</h3><h4>${messageDocuments}</h4>
      <g:form class="form-horizontal" action="addDocumentToPurchaseOrder" name="documentForPurchaseOrder" method="POST" enctype="multipart/form-data" id="${purchaseOrder.id}">
      <g:hiddenField name="purchaseOrder.id" value="${purchaseOrder.id}"/>
      <g:if test="${!purchaseOrder.documents}">
        <input type="file" required="" class="form-control" name="invoiceDocument" accept="application/pdf" maxlength="5000000" />
      </g:if><g:else>
        <g:if test="${purchaseOrder.documents?.size()==1}">
          <input type="file" required="" class="form-control" name="invoiceDocument" accept="text/xml" maxlength="5000000" />
        </g:if>
        <g:else>
          <input type="file" required="" class="form-control" name="invoiceDocument" accept="application/pdf,image/*" maxlength="5000000" />
        </g:else>
      </g:else>
     <br/>
      <input type="submit" class="btn btn-default" value="Subir documento de facturación" />
      </g:form>
      </g:if>
      </sec:ifAnyGranted>
      <br>
      <g:if test="${purchaseOrder.documents}">
      <h3>Documentos adjuntos</h3>
      <ul>
        <g:each in="${purchaseOrder.documents}" var="document">
        <li>
         <a href="${baseUrlDocuments}/${document.title}.${document.mimeType}"><i class="glyphicon glyphicon-download-alt"></i> ${document}</a>
        </li>
        </g:each>
      </ul>
      </g:if>
    </div>
    <asset:javascript src="purchaseOrder/show.js"/>
 </body>
</html>
