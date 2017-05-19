<%! import com.modulus.uno.BusinessEntityType %>
<%! import com.modulus.uno.NameType %>

<g:if test="${businessEntity.type == BusinessEntityType.FISICA}">
  <g:each var="name" in="${businessEntity.names}">
    <g:if test="${name.type == NameType.NOMBRE}">
      ${name.value}
    </g:if>
  </g:each>
  <g:each var="name" in="${businessEntity.names}">
    <g:if test="${name.type == NameType.APELLIDO_PATERNO}">
      ${name.value}
    </g:if>
  </g:each>
  <g:each var="name" in="${businessEntity.names}">
    <g:if test="${name.type == NameType.APELLIDO_MATERNO}">
      ${name.value}
    </g:if>
  </g:each>
</g:if>

<g:if test="${businessEntity.type == BusinessEntityType.MORAL}">
  <g:each var="name" in="${businessEntity.names}">
    <g:if test="${name.type == NameType.RAZON_SOCIAL}">
      ${name.value}
    </g:if>
  </g:each>
</g:if>