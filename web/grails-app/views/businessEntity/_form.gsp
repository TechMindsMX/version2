<%! import com.modulus.uno.BusinessEntityType %>
<%! import com.modulus.uno.NameType %>
<%! import com.modulus.uno.LeadType %>
<f:with bean="businessEntity">

<div class="form-group">
  <g:render template="businessEntityTypes"/>
</div>

<div id="person">
  <label>${message(code:"businessEntity.type")}</label>
  <g:if test="${businessEntity?.id}">
    <g:select name="type" from="${BusinessEntityType.values()}" value="${businessEntity.type}" disabled="disabled" class="form-control" />
  </g:if><g:else>
    <g:select name="type" from="${BusinessEntityType.values()}" class="form-control" value="${businessEntity.type}"/>
  </g:else>
</div>
<p></p>

<label id="rfcLabel"><g:message code="businessEntity.rfc" /><span class="required-indicator">*</span></label>
<g:if test="${businessEntity?.id}">
  <input id="rfc" name="rfc" value="${businessEntity.rfc}" class="form-control" style="text-transform:uppercase" readOnly="true" />
</g:if><g:else>
  <input id="rfc" name="rfc" value="${businessEntity.rfc}" class="form-control" style="text-transform:uppercase" required="" />
</g:else>

<g:if test="${clientProviderType == LeadType.EMPLEADO.toString()}">
  <label id="curpLabel"><g:message code="businessEntity.curp" /><span class="required-indicator">*</span></label>
  <input id="curp" name="curp" value="${businessEntity.getCurp()}" class="form-control" style="text-transform:uppercase" required="" />
  <label id="numberLabel"><g:message code="businessEntity.number" /></label>
  <input id="number" name="number" value="${businessEntity.getNumber()}" class="form-control" style="text-transform:uppercase"/>
</g:if>

<div id="website">
  <f:field property="website" label="${message(code:"businessEntity.website")}" wrapper="home">
  <g:textField name="website" value="${businessEntity.website}"/>
  </f:field>
</div>

<div class="fieldcontain fisica">
  <label id="nameLabel"><g:message code="businessEntity.name" /><span class="required-indicator">*</span></label>
  <g:if test="${businessEntity?.id}">
    <g:each var="name" in="${businessEntity.names}">
      <g:if test="${name.type == NameType.NOMBRE}">
        <input id="name" name="name" value="${name.value}" class="form-control" required=""/>
      </g:if>
    </g:each>
  </g:if>
  <g:else>
    <input id="name" name="name" class="form-control" value="${params.name}"/>
  </g:else>
</div>
<div class="fieldcontain fisica">
  <label id="lastNameLabel"><g:message code="businessEntity.lastName" /><span class="required-indicator">*</span></label>
  <g:if test="${businessEntity?.id}">
    <g:each var="name" in="${businessEntity.names}">
      <g:if test="${name.type == NameType.APELLIDO_PATERNO}">
        <input id="lastName" name="lastName" value="${name.value}" class="form-control"/>
      </g:if>
    </g:each>
  </g:if>
  <g:else>
    <input id="lastName" name="lastName" class="form-control" value="${params.lastName}"/>
  </g:else>
</div>
<div class="fieldcontain fisica">
  <label id="motherLastNameLabel"><g:message code="businessEntity.motherLastName" /><span class="required-indicator">*</span></label>
  <g:if test="${businessEntity?.id}">
    <g:each var="name" in="${businessEntity.names}">
      <g:if test="${name.type == NameType.APELLIDO_MATERNO}">
        <input id="motherLastName" name="motherLastName" value="${name.value}" class="form-control"/>
      </g:if>
    </g:each>
  </g:if>
  <g:else>
    <input id="motherLastName" name="motherLastName" class="form-control" value="${params.motherLastName}"/>
  </g:else>
</div>

<div class="fieldcontain moral">
  <label id="businessNameLabel"><g:message code="businessEntity.businessName" /><span class="required-indicator">*</span></label>
  <g:if test="${businessEntity?.id}">
    <g:each var="name" in="${businessEntity.names}">
      <g:if test="${name.type == NameType.RAZON_SOCIAL}">
        <input id="businessName" name="businessName" value="${name.value}" class="form-control"/>
      </g:if>
    </g:each>
  </g:if>
  <g:else>
    <input id="businessName" name="businessName" class="form-control" value="${params.businessName}"/>
  </g:else>
</div>
</f:with>

<input type="hidden" id="company" name="company" value="${session.company}" />
<input type="hidden" id="persona" name="persona"/>

<input type="hidden" id="regimenBusiness" value="${params.regimen}" />

<asset:javascript src="selector.js" />
<asset:javascript src="businessEntity/business_entity.js" />
