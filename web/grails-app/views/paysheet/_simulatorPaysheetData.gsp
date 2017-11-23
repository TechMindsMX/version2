<div class="col-md-12">
    <div class="table-responsive">
        <table class="table table-striped table-condensed">
            <tr>
                <th>Consecutivo</th>
                <th>Salario Imss</th>
                <th>Carga social del trabajador</th>
                <th>Subsidio</th>
                <th>ISR</th>
                <th>Total Imss</th>
                <th>Asimilable</th>
                <th>Subtotal</th>
                <th>Carga social de la empresa</th>
                <th>ISN</th>
                <th>Costo nominal</th>
                <th>Comisión</th>
                <th>Total nómina</th>
                <th>Iva</th>
                <th>Total Facturado</th>
            </tr>
            <g:each in="${paysheetEmployeeList}" var="paysheet">
                <tr>
                    <td>${paysheet.consecutivo}</td>
                    <td>${modulusuno.formatPrice(number:paysheet.salaryImss)}</td>
                    <td>${modulusuno.formatPrice(number:paysheet.socialQuota)}</td>
                    <td>${modulusuno.formatPrice(number:paysheet.subsidySalary)}</td>
                    <td>${modulusuno.formatPrice(number:paysheet.incomeTax)}</td>
                    <td>${modulusuno.formatPrice(number:paysheet.totalImss)}</td>
                    <td>${modulusuno.formatPrice(number:paysheet.salaryAssimilable)}</td>
                    <td>${modulusuno.formatPrice(number:paysheet.subtotal)}</td>
                    <td>${modulusuno.formatPrice(number:paysheet.socialQuotaEmployeeTotal)}</td>
                    <td>${modulusuno.formatPrice(number:paysheet.isn)}</td>
                    <td>${modulusuno.formatPrice(number:paysheet.nominalCost)}</td>
                    <td>${modulusuno.formatPrice(number:paysheet.commission)}</td>
                    <td>${modulusuno.formatPrice(number:paysheet.totalNominal)}</td>
                    <td>% ${paysheet.iva}</td>
                    <td>${modulusuno.formatPrice(number:paysheet.totalBill)}</td>
                </tr>
            </g:each>
        </table>
    </div>
</div>