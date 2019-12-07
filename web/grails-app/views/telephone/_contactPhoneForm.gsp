<%@ page import="com.modulus.uno.TelephoneType" %>

<div class="form-group">

  <div class="row">
    <div class="col-xs-3">
      <label><g:message code="telephone.type" default="Tipo" /></label>
    </div>
    <div class="col-xs-3">
      <g:select class="form-control" name="type" from="${TelephoneType.values()}" value="${telephone.type}" />
    </div>
  </div>

  <br />

  <div class="row">
    <div class="col-xs-3">
      <label><g:message code="telephone.number" /></label>
    </div>
    <div class="col-xs-3">
      <input class="form-control" required="" type="text" name="number" pattern="[0-9]{10}" title="Ingrese 10 dígitos exactamente del número teléfonico" maxLength="10" value="${telephone.number}" />
    </div>
  </div>

  <br/>

  <div class="row">
    <div class="col-xs-3">
      <label><g:message code="telephone.extension" /></label>
    </div>
    <div class="col-xs-3">
      <input class="form-control" type="text" name="extension" pattern="[0-9]*" title="Ingrese sólo números" maxLength="10" value="${telephone.extension}" />
    </div>
  </div>
</div>

