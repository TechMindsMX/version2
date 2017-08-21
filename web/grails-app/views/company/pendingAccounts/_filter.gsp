<%! import java.text.SimpleDateFormat %>
  <div class="portlet portlet-blue">
    <div class="portlet-body">
    <div class="row">
      <g:form action="pendingAccounts">
        <div class="col-md-4">
          <p>Desde el:</p>
          <input type="text" id="startDate" name="startDate" required="required" value="${new SimpleDateFormat('dd/MM/yyyy').format(pendingAccounts.startDate)}">
        </div>
        <div class="col-md-4">
          <p>Hasta el:</p>
          <input type="text" id="endDate" name="endDate" required="required" value="${new SimpleDateFormat('dd/MM/yyyy').format(pendingAccounts.endDate)}">
        </div>
        <div class="col-md-2 text-righ text-right">
          <g:actionSubmit class="btn btn-default" value="Consultar" action="pendingAccounts"/>
        </div>
      </g:form>
      <div class="col-md-2 text-righ text-right">
        <g:link class="btn btn-default" action="generateXlsForPendingAccounts" params="[startDate:pendingAccounts.startDate, endDate:pendingAccounts.endDate]">
          <i class="fa fa-file-excel-o"></i> XLS
        </g:link>
      </div>
      </div>
    </div>
  </div>
