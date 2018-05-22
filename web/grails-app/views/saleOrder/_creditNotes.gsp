<%! import com.modulus.uno.status.CreditNoteStatus %>

<div class="portlet portlet-default">
  <div class="portlet-heading">
    <div class="portlet-title">
      <h4>Notas de Crédito</h4>
    </div>
    <div class="clearfix"></div>
  </div>
  <div class="portlet-body">
    <div class="row">
      <div class="col-md-12 text-right">
        <g:link class="btn btn-primary" controller="creditNote" action="create" id="${saleOrder.id}">Crear</g:link>
      </div>
    </div>
    <div class="row">
      <div class="col-md-12">
        <div class="table-responsive">
          <table class="table table-striped table-condensed">
            <thead>
              <tr>
                <th class="text-center">No.</th>
                <th class="text-center">Fecha de Creación</th>
                <th class="text-center">Serie</th>
                <th class="text-center">Folio</th>
                <th class="text-center">Total</th>
                <th class="text-center">Estatus</th>
              </tr>
            </thead>
            <tbody>
              <g:each in="${saleOrder.creditNotes}" var="creditNote">
                <tr>
                  <td class="text-center"><g:link controller="creditNote" action="show" id="${creditNote.id}">${creditNote.id}</g:link></td>
                  <td class="text-center">${formatDate(date:creditNote.dateCreated, format:'dd-MM-yyyy')}</td>
                  <td class="text-center">${creditNote.invoiceSerie}</td>
                  <td class="text-center">${creditNote.invoiceFolio}</td>
                  <td class="text-right">${modulusuno.formatPrice(number:creditNote.total)}</td>
                  <td class="text-center">${creditNote.status}</td>
                </tr>
              </g:each>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</div>
