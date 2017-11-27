<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
      <div class="page-title">
         <h1>
         <i class="fa fa-shopping-bag fa-3x"></i>
           Registros / Mis productos &amp; servicios
           <small><g:message code="product.view.list.label" /></small>
         </h1>
          <ol class="breadcrumb">
            <li><i class="fa fa-caret-square-o-up"></i> Compañía</li>
            <li class="active">Productos/Servicios</li>
          </ol>
      </div>
      <div class="portlet portlet-blue">
        <div class="portlet-heading">
          <div class="portlet-title">
            <br />
            <br />
          </div>
          <div class="clearfix"></div>
        </div>
        <div id="list-product" class="panel-collapse collapse in">
          <div class="portlet-body">
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <div class="table-responsive">
             <table class="table">
               <tr>
                 <th><g:message code="product.satKey.label" /></th>
                 <th><g:message code="product.sku.label" /></th>
                 <th><g:message code="product.name.label" /></th>
                 <th><g:message code="product.price.label" /></th>
                 <th><g:message code="product.ieps.label" /></th>
                 <th><g:message code="product.iva.label" /></th>
                 <th><g:message code="product.unitType.label" /></th>
                 <th><g:message code="product.currencyType.label" /></th>
               </tr>
               <g:each in="${productList.sort{it.name}}" var="product">
               <tr>
                 <td>
                  <g:link action="show" id="${product.id}">
                     ${product.satKey}
                  </g:link>
                 </td>
                 <td>${product.sku}</td>
                 <td>${product.name}</td>
                 <td>${modulusuno.formatPrice(number:product.price)}</td>
                 <td>${product.ieps}</td>
                 <td>${product.iva}</td>
                 <td>${product.unitType}</td>
                 <td>${product.currencyType}</td>
               </tr>
               </g:each>
             </table>
            </div>
            <div class="pagination">
                <g:paginate total="${productCount ?: 0}" />
            </div>
          </div>
        </div>
        </div>
        <g:link class="create btn btn-primary" action="create"><g:message code="product.view.create.label" args="[entityName]" /></g:link>
      </div>
    </body>
</html>
