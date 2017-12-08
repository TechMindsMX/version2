<g:each in="${quotationContract.users}" var="user">
<div class="row" style="margin-bottom: 5px;">
<g:form action="deleteUserFromQuotationContract">
    <div class="col-md-6">
    <label><g:message code="${user.username}" /></label>
    </div>
    <div class="col-md-6">
    <input type="hidden" name="id" value="${user.id}"/>
    <input type="hidden" name="quotationId" value="${quotationContract.id}"/>
    <button name="remove" class="btn btn-danger" value="${message(code: 'default.button.remove.label', default: 'Quitar')}" ><i class="fa fa-trash"></i>Quitar</button>
    </div>
</g:form>
</div>
</g:each>