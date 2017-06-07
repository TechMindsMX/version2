<f:with bean="user">
<g:if test="${authorize}">
  <label>Rol:</label>
  <g:radioGroup name="roleId" values="${authorities*.id}" labels="${authorities*.authority}" value="${params.roleId ?: authorities*.id.first()}">
  ${it.radio}  <g:message code="${it.label}"/>&nbsp;&nbsp;&nbsp;
  </g:radioGroup>
</g:if>

<f:field property="username" label="${message(code:'user.username')}*" wrapper="home"/>
<f:field property="password" label="${message(code:'user.password')}">
  <g:passwordField name="password" value="${user.password}" class="form-control" placeholder="${label}"/>
</f:field>
<f:field property="passwordCheck" label="${message(code:'user.passwordCheck')}" required="true">
  <g:passwordField name="passwordCheck" value="${user.passwordCheck}" class="form-control" placeholder="${label}"/>
</f:field>
<f:field property="name" label="${message(code:'user.name')}*" wrapper="home"/>
<f:field property="lastName" label="${message(code:'user.lastName')}*" wrapper="home"/>
<f:field property="motherLastName" label="${message(code:'user.motherLastName')}*" wrapper="home"/>
<f:field property="email" label="${message(code:'user.email')}*" wrapper="home"/>
<f:field property="emailCheck" label="${message(code:'user.emailCheck')}*" required="true" wrapper="home"/>
<g:if test="${legal}" >
  <f:field property="rfc" label="${message(code:'user.rfc')}*" required="true" wrapper="home"/>
  <f:field property="curp" label="${message(code:'user.curp')}*" required="true" wrapper="home"/>
  <div class="form-group">
    <label><g:message code="user.birthdate" /></label>
    <g:datePicker name="birthDate" value="${user.birthDate ?: new Date()}" class="form-control" precision="day"/>
  </div>
  <div class="form-group">
    <label class="control-label"><g:message code="user.gender" /><span class="required-indicator">*</span></label>
    <g:select name="gender" from="${com.modulus.uno.Gender.values()}" class="form-control" aria-controls="example-table"/>
  </div>
  <div class="form-group">
    <label class="control-label"><g:message code="user.nationality" /><span class="required-indicator">*</span></label>
    <g:select name="nationality" from="${com.modulus.uno.Nationality.values()}" class="form-control" aria-controls="example-table"/>
  </div>
  <f:field property="number" label="${message(code:'user.number')}" required="true" wrapper="home"/>
  <f:field property="extension" label="${message(code:'user.extension')}"  wrapper="home"/>
  <div class="form-group">
    <label class="control-label"><g:message code="user.telephoneType" /><span class="required-indicator">*</span></label>
    <g:select name="telephoneType" from="${com.modulus.uno.TelephoneType.values().findAll{ !it.value.contains('Trabajo')} }" class="form-control" aria-controls="example-table" value="${user.telephoneType}"/>
  </div>
</g:if>

<div class="checkbox" >
  <label for="terms">
    <g:checkBox name="terms"/>
        <button type="button" class="btn btn-link" data-toggle="modal" data-target="#modalTermsAndCondition">
          Acepto términos y condiciones*
        </button>

        <div class="modal fade" id="modalTermsAndCondition" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
          <div class="modal-dialog" role="document">
            <div class="modal-content">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">Términos y Condiciones</h4>
              </div>
              <div class="modal-body">
                <textarea class="form-control" rows="15">${conditionsAndTerms}</textarea>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
              </div>
            </div>
          </div>
        </div>

  </label>
</div>
</f:with>
<input type="hidden" name="legal" value="${legal}" />
