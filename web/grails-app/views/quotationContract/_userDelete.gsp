<g:form action="deleUser">

    <label><g:message code="${user}" /></label>
    <input type="hidden" name="id" value="${user}"/>
    <g:submitButton name="remove" class="btn btn-primary" value="${message(code: 'default.button.remove.label', default: 'Quitar')}" />

</g:form>