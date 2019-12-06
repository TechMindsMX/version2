<%@ page import="com.modulus.uno.EmailType" %>

<div class="form-group">

  <div class="row">
    <div class="col-xs-3">
      <label><g:message code="email.type" default="Tipo" /></label>
    </div>
    <div class="col-xs-3">
      <g:select class="form-control" name="type" from="${EmailType.values()}" value="${email.type}" />
    </div>
  </div>

  <br />

  <div class="row">
    <div class="col-xs-3">
      <label><g:message code="email.address" default="Email" /></label>
    </div>
    <div class="col-xs-3">
      <input class="form-control" required="" type="email" name="address" placeholder="Ingrese la dirección de correo electrónico" value="${email.address}" />
    </div>
  </div>

</div>

