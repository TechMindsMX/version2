<div class="">
<div class="portlet portlet-blue">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4>Archivos para Facturación</h4>
    </div>
    <div class="clearfix"></div>
  </div>
  <div id="defaultPortlet" class="panel-collapse collapse in">
    <div class="portlet-body">
      <g:if test="${!documents.status}">
        <g:form class="form-horizontal" action="sendFilesToCreateInvoice" name="documentsToInvoice" method="POST" enctype="multipart/form-data" >
          <g:render template="formDataToInvoice"/>
        </g:form>
      </g:if>
      <g:else>
      <ul>
        <li class="text-primary">
          <span class="label label-success">
            <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
          </span>
          Archivo .key
        </li>
        <li class="text-primary">
          <span class="label label-success">
            <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
          </span>
          Archivo .cer
        </li>
        <li class="text-primary">
          <span class="label label-success">
            <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
          </span>
          Logotipo
        </li>
        <li class="text-primary">
          <span class="label label-success">
            <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
          </span>
          Password del Certificado
         </li>
         <li class="text-primary">
           <span class="label label-success">
             <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
           </span>
           Número de Certificado
         </li>
         <li class="text-primary">
           Serie de Ingresos: ${documents.currentSerieIncomes ?: "SIN SERIE"} &nbsp;
            <button type="button" class="btn btn-info btn-xs" data-toggle="modal" data-target="#changeIncomesSerieModal" data-whatever="${documents.currentSerieIncomes}">
              Cambiar Serie
            </button>
            <!-- modal change date -->
            <div class="modal fade" id="changeIncomesSerieModal" tabindex="-1" role="dialog" aria-labelledby="changeSerieModalLabel">
              <div class="modal-dialog" role="document">
                <div class="modal-content">
                  <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="changeSerieModalLabel">Cambiar Serie y Folio Inicial de Facturación para Ingresos</h4>
                  </div>
                  <g:form action="changeSerieForInvoices" id="${company.id}">
                  <g:hiddenField name="type" value="ingresos"/>
                  <div class="modal-body">
                      <div class="form-group">
                        <label for="serie" class="control-label">Serie:</label>
                        <input type="text" class="form-control" id="serie" name="serie">
                      </div>
                      <div class="form-group">
                        <label for="folio" class="control-label">Folio Inicial:</label>
                        <input class="form-control" type="number" name="folio" min="1" step="1" required="required">
                      </div>
                  </div>
                  <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                    <button type="submit" class="btn btn-primary">Cambiar</button>
                  </div>
                  </g:form>
                </div>
              </div>
            </div>
            <!-- modal change date end -->

         </li>
         <li class="text-primary">
           Serie de Egresos: ${documents.currentSerieExpenses ?: "SIN SERIE"} &nbsp;
            <button type="button" class="btn btn-info btn-xs" data-toggle="modal" data-target="#changeExpensesSerieModal" data-whatever="${documents.currentSerieExpenses}">
              Cambiar Serie
            </button>
            <!-- modal change date -->
            <div class="modal fade" id="changeExpensesSerieModal" tabindex="-1" role="dialog" aria-labelledby="changeSerieModalLabel">
              <div class="modal-dialog" role="document">
                <div class="modal-content">
                  <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="changeSerieModalLabel">Cambiar Serie y Folio Inicial de Facturación para Egresos</h4>
                  </div>
                  <g:form action="changeSerieForInvoices" id="${company.id}">
                  <g:hiddenField name="type" value="egresos"/>
                  <div class="modal-body">
                      <div class="form-group">
                        <label for="serie" class="control-label">Serie:</label>
                        <input type="text" class="form-control" id="serie" name="serie">
                      </div>
                      <div class="form-group">
                        <label for="folio" class="control-label">Folio Inicial:</label>
                        <input class="form-control" type="number" name="folio" min="1" step="1" required="required">
                      </div>
                  </div>
                  <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                    <button type="submit" class="btn btn-primary">Cambiar</button>
                  </div>
                  </g:form>
                </div>
              </div>
            </div>
            <!-- modal change date end -->

         </li>



         <li class="text-primary">
           Template por default: &nbsp;
            <button type="button" class="btn btn-info btn-xs" data-toggle="modal" data-target="#changeTemplatesSerieModal" data-whatever="${documents.currentSerieTemplates}">
              Cambiar Template 
            </button>
            <!-- modal change date -->
            <div class="modal fade" id="changeTemplatesSerieModal" tabindex="-1" role="dialog" aria-labelledby="changeSerieModalLabel">
              <div class="modal-dialog" role="document">
                <div class="modal-content">
                  <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="changeSerieModalLabel">Cambiar template por default</h4>
                  </div>
                  <g:form action="changeCompanyTemplateByDefault" id="${company.id}">
                  <div class="modal-body">
                      <div class="form-group">
                        <companyInfo:listTemplatesPdfForCompany/>
                      </div>
                  </div>
                  <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                    <button type="submit" class="btn btn-primary">Cambiar</button>
                  </div>
                  </g:form>
                </div>
              </div>
            </div>
            <!-- modal change date end -->

         </li>

       </ul>
       <div class="text-right">
         <g:link class="btn btn-primary" action="changeStampDocuments" id="${company.id}">Cambiar</g:link>
       </div>
      </g:else>
    </div>
  </div>
  <div class="portlet-footer"></div>
  </div>
</div>
<asset:javascript src="company/invoicingDocuments.js"/>
<br />
<br />
