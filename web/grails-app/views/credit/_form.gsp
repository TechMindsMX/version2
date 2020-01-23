<%@ page import="com.modulus.uno.credit.ProductType" %>
<%@ page import="com.modulus.uno.credit.PortfolioManagementType" %>
<%@ page import="com.modulus.uno.credit.FrequencyType" %>
<%@ page import="com.modulus.uno.credit.CreditLineType" %>
<%@ page import="com.modulus.uno.credit.DispersionType" %>

<div class="form-group">
  <div class="row">
    <div class="col-xs-3">
      <label><g:message code="credit.name" default="Crédito" /></label>
    </div>
    <div class="col-xs-9">
      <g:textField class="form-control" required="" type="text" name="name" title="Nombre" value="${credit.name}" />
    </div>
  </div>
</div>

<div class="form-group">
  <div class="row">
    <div class="col-xs-3">
      <label><g:message code="credit.productType" default="Tipo de producto" /></label>
    </div>
    <div class="col-xs-9">
      <g:select  name="productType" from="${ProductType.values()}" optionValue="value" class="form-control" noSelection="['':'Tipo de producto...']" required="" value="${credit.productType}" />
    </div>
  </div>
</div>

<div class="form-group">
  <div class="row">
    <div class="col-xs-3">
      <label><g:message code="credit.portfolioManagementType" default="Tipo de Administración de Cartera" /></label>
    </div>
    <div class="col-xs-9">
      <g:select name="portfolioManagementType" from="${PortfolioManagementType.values()}" optionValue="value" class="form-control" required="" value="${credit.portfolioManagementType}" />
    </div>
  </div>
</div>

<div class="form-group">
  <div class="row">
    <div class="col-xs-3">
      <label><g:message code="credit.frequencyType" default="Frecuencia" /></label>
    </div>
    <div class="col-xs-9">
      <g:select name="frequencyType" from="${FrequencyType.values()}" optionValue="value" class="form-control" noSelection="['':'Periodicidad...']" required="" value="${credit.frequencyType}" />
    </div>
  </div>
</div>

<div class="form-group">
  <div class="row">
    <div class="col-xs-3">
      <label><g:message code="credit.creditLineType" default="Múltiplo Solicitud" /></label>
    </div>
    <div class="col-xs-9">
      <g:select name="creditLineType" from="${CreditLineType.values()}" optionValue="value" class="form-control" required="" value="${credit.creditLineType}" />
    </div>
  </div>
</div>

<div class="form-group">
  <div class="row">
    <div class="col-xs-3">
      <label><g:message code="credit.dispersionType" default="Tipo dispersión" /></label>
    </div>
    <div class="col-xs-9">
      <g:select name="dispersionType" from="${DispersionType.values()}" optionValue="value" class="form-control" required="" value="${credit.dispersionType}" />
    </div>
  </div>
</div>

<div class="form-group">
  <div class="row">
    <div class="col-xs-3">
      <label><g:message code="credit.enabled" default="Habilitado" /></label>
    </div>
    <div class="col-xs-9">
      <g:checkBox name="enabled" value="${credit.enabled}" />
    </div>
  </div>
</div>
