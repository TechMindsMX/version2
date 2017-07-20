<!doctype html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Welcome to Grails</title>

    <asset:link rel="icon" href="favicon.ico" type="image/x-ico" />
  </head>
  <body>
    <div class="row">
      <!-- BEGIN PAGE TITLE -->
      <div class="page-title">
        <h1>
          Información de ModulusUno
          <i class="fa fa-info-circle fa-2x"></i>
        </h1>
      </div>
      <!-- END OF PAGE TITLE -->
    </div>

    <div class="row">
      <div class="col-md-6">
        <div class="portlet portlet-default">
          <div class="portlet-heading">
            <div class="portlet-title">
              <h3>Application status</h3>
            </div>
            <div class="clearfix"></div>
          </div>
          <div id="defaultPortlet" class="panel-collapse collapse in">
            <div class="portlet-body">
              <ul>
                <li>Environment: ${grails.util.Environment.current.name}</li>
                <li>App profile: ${grailsApplication.config.grails?.profile}</li>
                <li>App version: <g:meta name="info.app.version"/></li>
                <li>Grails version: <g:meta name="info.app.grailsVersion"/></li>
                <li>Groovy version: ${GroovySystem.getVersion()}</li>
                <li>JVM version: ${System.getProperty('java.version')}</li>
                <li>Reloading active: ${grails.util.Environment.reloadingAgentEnabled}</li>
              </ul>
            </div>
          </div>
        </div>
      </div>
      <div class="col-md-6">
        <div class="portlet portlet-default">
          <div class="portlet-heading">
            <div class="portlet-title">
              <h3>Artefacts</h3>
            </div>
            <div class="clearfix"></div>
          </div>
          <div id="defaultPortlet" class="panel-collapse collapse in">
            <div class="portlet-body">
              <ul>
                <li>Controllers: ${grailsApplication.controllerClasses.size()}</li>
                <li>Domains: ${grailsApplication.domainClasses.size()}</li>
                <li>Services: ${grailsApplication.serviceClasses.size()}</li>
                <li>Tag Libraries: ${grailsApplication.tagLibClasses.size()}</li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="row">
      <div class="col-md-6">
        <div class="portlet portlet-default">
          <div class="portlet-heading">
            <div class="portlet-title">
              <h3>Installed plugins</h3>
            </div>
            <div class="clearfix"></div>
          </div>
          <div id="defaultPortlet" class="panel-collapse collapse in">
            <div class="portlet-body">
              <ul>
                <g:each var="plugin" in="${applicationContext.getBean('pluginManager').allPlugins}">
                <li>${plugin.name} - ${plugin.version}</li>
                </g:each>
              </ul>
            </div>
          </div>
        </div>
      </div>
      <div class="col-md-6">
        <div class="portlet portlet-default">
          <div class="portlet-heading">
            <div class="portlet-title">
              <h3>Available controllers</h3>
            </div>
            <div class="clearfix"></div>
          </div>
          <div id="defaultPortlet" class="panel-collapse collapse in">
            <div class="portlet-body">
              <ul>
                <g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName } }">
                <li class="controller">
                  <g:link controller="${c.logicalPropertyName}">${c.fullName}</g:link>
                </li>
                </g:each>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>

  </body>
</html>
