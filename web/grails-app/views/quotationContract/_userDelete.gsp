<g:each in="${quotationContract.users}" var="user">
<g:form action="deleteUserFromQuotationContract">
    <label><g:message code="${user.username}" /></label>
    <input type="hidden" name="id" value="${user.id}"/>
    <input type="hidden" name="quotationId" value="${quotationContract.id}"/>
    <g:submitButton name="remove" class="btn btn-primary" value="${message(code: 'default.button.remove.label', default: 'Quitar')}" />
</g:form>
</g:each>