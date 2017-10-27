<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
    </head>
    <body>
      <div class="page-title">
        <h1><g:message code="document.create"/></h1>
        <ol class="breadcrumb">
          <li><i class="fa fa-caret-square-o-up"></i> Compañia</li>
          <li class="active">Visor de Documentos</li>
        </ol>
      </div>
      <div id="create-address" class="content scaffold-create" role="main">
        <div class="portlet portlet-blue">
          <div class="portlet-heading">
            <div class="portlet-title">
              <br />
              <br />
            </div>
            <div class="clearfix"></div>
          </div>
          <div id="horizontalFormExample" class="panel-collapse collapse in">
            <div class="portlet-body clearfix">
              <div class="col-sm-4 col-md-4">
                <ul class="not-style">
                  <g:each var="document" in="${documents}">
                  <li>
                    <p>
                    <a href="${baseUrlDocuments}/${document.title}.${document.mimeType}" class="btn btn-default" download><i class="fa fa-download"></i></a>
                      <a href="#" class="documentIframe" path="${baseUrlDocuments}/${document.title}.${document.mimeType}" ><g:message code="document.type.${document.type}" /> </a>
                    </p>
                  </li>
                  </g:each>
                  <p><hr></p>
                  <g:if test="${infoUser}">
                    <g:render template="infoUser" bean="${infoUser}" />
                  </g:if>
                  <g:if test="${infoCompany}">
                    <g:render template="infoCompany" bean="${infoCompany}" />
                  </g:if>
                </ul>
              </div>
              <div class="col-sm-8 col-md-8">
                <iframe id="frameView" src="" width="100%" height="400px">
                </iframe>
              </div>
              <p align="center">
                <a href='${createLink(controller:"company",action:"show", id:"${company}")}' class="btn btn-default">Regresar</a>
              </p>
            </div>
          </div>
        </div>
      </div>
      <g:javascript>
        $(".documentIframe").on("click",function(){
          var url = $(this).attr("path");
          $("#frameView").attr("src",url);
        });
      </g:javascript>
    </body>
</html>
