<%! import com.modulus.uno.PaymentMethod%>
            <div class="portlet-body">
              <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
              </g:if>
              <g:hasErrors bean="${paysheetProject}">
                <ul class="error alert alert-danger" role="alert">
                  <g:eachError bean="${paysheetProject}" var="error">
                    <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message code="paysheetProject.error.${error.field}" args="${[error.defaultMessage.replace('{0}','')]}"/></li>
                  </g:eachError>
                </ul>
              </g:hasErrors>
              <div class="row">
                <div class="col-md-12">
                  <div class="form-group">
                    <label><g:message code="Cliente"/></label>
                    <g:select name="quotation" class="form-control"
                              from="${quotationContractList}"
                              optionValue="client"
                              optionKey="id"></g:select>
                  </div>
                </div>
              </div>
              <div class="row">
                <div class="col-md-3">
                  <label><g:message code="Monto" /></label>
                  <input class="form-control" type="number" step="0.01" id="amonut" name="amount" required="required" pattern="([0-9]*[.])?[0-9]+">
                  <g:if test="${messageForErrorInBalances}">
                    <label><g:message code="Error: El monto en solicitud de pagos es mayor al saldo disponible" /></label>
                  </g:if>
                </div>
                <div class="col-md-9">
                  <label><g:message code="Nota" /></label>
                  <input class="form-control" name="note" type="text" required=""/>
                </div>
              </div>
              <br>
              <div class="row">
                <div class="col-md-3">
                  <label><g:message code="Modo de Pago" /></label>
                  <g:select class="form-control" name="paymentMethod" from="${PaymentMethod.values()}"/>
                </div>
              </div>
              <br>
            </div>

            <div class="portlet-footer">
              <div class="row">
                <div class="col-md-6">
                  <g:link class="btn btn-default" controller="quotationContract" action="index">Cancelar</g:link>
                </div>
                <div class="col-md-6 text-right">
                  <g:submitButton name="create" class="btn btn-primary" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                </div>
              </div>
            </div>
          