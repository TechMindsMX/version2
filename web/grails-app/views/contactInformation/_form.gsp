<div class="form-group">
  <div class="row">
    <div class="col-xs-3">
      <label><g:message code="contactInformation.name" default="Nombre completo" /></label>
    </div>
    <div class="col-xs-3">
      <input class="form-control" required="" type="text" name="name" placeholder="Nombre completo del contacto" value="${contactInformation.name}" />
    </div>
  </div>
  <br/>

  <div class="row">
    <div class="col-xs-3">
      <label><g:message code="contactInformation.department" default="Departamento" /></label>
    </div>
    <div class="col-xs-3">
      <input class="form-control" type="text" name="department" placeholder="Departamento" value="${contactInformation.department}" />
    </div>
  </div>
  <br/>

  <div class="row">
    <div class="col-xs-3">
      <label><g:message code="contactInformation.position" default="Posición" /></label>
    </div>
    <div class="col-xs-3">
      <input class="form-control" type="text" name="position" placeholder="Posición" value="${contactInformation.position}" required="" />
    </div>
  </div>
</div>
