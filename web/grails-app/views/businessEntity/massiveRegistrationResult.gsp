<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'businessEntity.label', default: 'BusinessEntity')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-title">
      <h1>
        <i class="fa fa-user-plus fa-3x"></i>
        Registros / Relaciones Comerciales
        <small><g:message code="businessEntity.view.massive.result.label" /></small>
      </h1>
    </div>
    <div id="edit-address" class="content scaffold-edit" role="main">
      <div class="portlet portlet-blue">
        <div id="horizontalFormExample" class="panel-collapse collapse in">

          <div class="portlet-body">
            <div class="row">
              <div class="col-md-12">
                <div class="table-responsive">
                  <table class="table table-striped table-condensed">
                    <tr>
                    <g:each in="${resultImport.headers}" var="header">
                      <th>${header}</th>             
                    </g:each>
                      <th>RESULTADO</th>
                    </tr>
                    <g:if test="${resultImport.headers.size() == 19}">
                      <g:each in="${resultImport.data}" var="values" status="i">
                        <td>${values.PERSONA}</td>
                        <td>${values.RFC}</td>
                        <td>${values.SITIO_WEB}</td>
                        <td>${values.RAZON_SOCIAL}</td>
                        <td>${values.PATERNO}</td>
                        <td>${values.MATERNO}</td>
                        <td>${values.NOMBRE}</td>
                        <td>${values.CLAVE_BANCO}</td>
                        <td>${values."ULTIMOS_4_DIGITOS_TARJETA"}</td>
                        <td>${values.CALLE}</td>
                        <td>${values.NUMEXTERIOR}</td>
                        <td>${values.NUMINTERIOR}</td>
                        <td>${values.CODIGO_POSTAL}</td>
                        <td>${values.COLONIA}</td>
                        <td>${values."DELEGACION/MUNICIPIO"}</td>
                        <td>${values.PAIS}</td>
                        <td>${values.CIUDAD}</td>
                        <td>${values.ENTIDAD_FEDERATIVA}</td>
                        <td>${values.TIPO_DE_DIRECCION}</td>
                        <td>${resultImport.results[i]}</td>  
                        <tr> </tr>
                      </g:each>
                    </g:if>
                    <g:elseif test="${resultImport.headers.size() == 18}">
                      <g:each in="${resultImport.data}" var="values" status="i">
                        <td>${values.PERSONA}</td>
                        <td>${values.RFC}</td>
                        <td>${values.SITIO_WEB}</td>
                        <td>${values.RAZON_SOCIAL}</td>
                        <td>${values.PATERNO}</td>
                        <td>${values.MATERNO}</td>
                        <td>${values.NOMBRE}</td>
                        <td>${values.CLABE}</td>
                        <td>${values.CALLE}</td>
                        <td>${values.NUMEXTERIOR}</td>
                        <td>${values.NUMINTERIOR}</td>
                        <td>${values.CODIGO_POSTAL}</td>
                        <td>${values.COLONIA}</td>
                        <td>${values."DELEGACION/MUNICIPIO"}</td>
                        <td>${values.PAIS}</td>
                        <td>${values.CIUDAD}</td>
                        <td>${values.ENTIDAD_FEDERATIVA}</td>
                        <td>${values.TIPO_DE_DIRECCION}</td>
                        <td>${resultImport.results[i]}</td>  
                        <tr> </tr>
                      </g:each>
                    </g:elseif>
                    <g:else>
                      <g:each in="${resultImport.data}" var="values" status="i">
                        <td>${values.RFC}</td>
                        <td>${values.CURP}</td>
                        <td>${values.PATERNO}</td>
                        <td>${values.MATERNO}</td>
                        <td>${values.NOMBRE}</td>
                        <td>${values.NO_EMPL}</td>
                        <td>${values.CLABE}</td>
                        <td>${values.NUMTARJETA}</td>
                        <td>${values.IMSS}</td>
                        <td>${values.NSS}</td>
                        <td>${values.FECHA_ALTA}</td>
                        <td>${values.BASE_COTIZA}</td>
                        <td>${values.NETO}</td>
                        <td>${values.PRIMA_VAC}</td>
                        <td>${values.DIAS_AGUINALDO}</td>
                        <td>${values.PERIODO_PAGO}</td>
                        <td>${resultImport.results[i]}</td>  
                        <tr> </tr> 
                      </g:each>
                    </g:else>           
                  </table>
                </div>
              </div>
            </div>
          </div>
          <div class="portlet-footer">

            <div class="row">
              <div class="col-md-6">
                <g:link class="btn btn-default" controller="businessEntity" action="massiveRegistration">Regresar</g:link>
              </div>
              <div class="col-md-6 text-right">
                <g:link class="btn btn-default" controller="businessEntity" action="index">Relaciones Comerciales</g:link>
              </div>
            </div>

          </div>
        </div>
      </div>
    </div>
  </body>
</html>
