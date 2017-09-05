
<ul class="collapse nav" id="registros">
  <li>
    <g:link action="create" controller="businessEntity" params='[clientProviderType:"${com.modulus.uno.LeadType.CLIENTE}"]' >Alta Cliente</g:link>
  </li>
  <li>
    <g:link action="create" controller="businessEntity"  params='[clientProviderType:"${com.modulus.uno.LeadType.PROVEEDOR}"]'>Alta Proveedor</g:link>
  </li>
  <li>
    <g:link action="create" controller="businessEntity"  params='[clientProviderType:"${com.modulus.uno.LeadType.EMPLEADO}"]'>Alta Empleado</g:link>
  </li>
  <li>
    <g:link action="create" controller="product">Alta Producto/Servicio</g:link>
  </li>
  <li>
    <g:link controller="businessEntity" action="index">Lista de Relaciones Comerciales</g:link>
  </li>
  <li>
    <g:link controller="businessEntity" action="massiveRegistration">Alta Masiva de Relaciones Comerciales</g:link>
  </li>
  <li>
    <g:link controller="product" action="index">Mis Productos/Servicios</g:link>
  </li>
</ul>
