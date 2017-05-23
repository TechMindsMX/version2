<%! import com.modulus.uno.RejectReason %>
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
    <div class="col-md-12">
    <div class="portlet">
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
            <g:if test="${purchaseOrder.isAnticipated}">
              <g:render template="addItems"/>
            </g:if>
            <g:if test="${purchaseOrder.items}">
              <g:render template="listItems"/>
            </g:if>
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
        </div>

      </div>
      </div>
        </div>
      </div><!--Close Portlet-->

      </div>
      
    </div>

    <div class="row">
    <div class="col-md-12">
    <div class="portlet">
      <g:if test="${params.badfile}">
        <div class="alert alert-warning">${params.badfile}</div>
      </g:if>
     <sec:ifAnyGranted roles="ROLE_LEGAL_REPRESENTATIVE_EJECUTOR,ROLE_OPERATOR_EJECUTOR">
      <g:if test="${enableAddDocuments || !purchaseOrder.isAnticipated}">
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
      </div>
    </div>
    <asset:javascript src="purchaseOrder/show.js"/>



        </div>
      </div>
    </div>
    </div>
 </body>
</html>
