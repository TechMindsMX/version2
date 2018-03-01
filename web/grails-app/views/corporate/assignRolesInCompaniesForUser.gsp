<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main" />
    <title></title>
  </head>
  <body>
    <!-- BEGIN PAGE TITLE -->
    <div class="page-title">
      <h1>
        <i class="fa fa-user fa-3x"></i>
          ${user.profile.getFullName()}
        <small>
          ${user.profile.email}
        </small>
      </h1>
      <ol class="breadcrumb">
        <li><i class="fa fa-caret-square-o-up"></i> Compañia </li>
        <li class="active">Creación de Compañia</li>
      </ol>
    </div>

    <div class="row">
      <div class="col-md-12 col-lg-12">
        <g:form action="saveRolesForUser" method="POST" class="form-horizontal" role="form">
        <g:hiddenField name="username" value="${user.username}" />
        <div class="portlet portlet-blue">
          <div class="portlet-body">
            <div class="table-responsive">
            <table class="table table-condensed table-striped">
              <thead>
                <tr>
                  <th></th>
                  <g:each status="i" in="${roles}" var="role">
                    <th>
                      <a role="button" data-toggle="popover" data-trigger="hover" data-placement="top" title="${message(code:'role.authority.'+role.authority.toLowerCase())}" data-content="${message(code:'role.authority.'+role.authority.toLowerCase())}" class="information">
                        <g:message code="role.authority.${role.authority.toLowerCase()}" />
                      </a>
                    </th>
                  </g:each>
                </tr>
              </thead>
              <tbody>
                <g:each status="b" in="${companies.sort{it.bussinessName}}" var="company">
                  <tr>
                    <td>${company}</td>
                    <g:each status="a" in="${roles}" var="someRole">
                      <td>
                        <modulusuno:checkboxForRoleAtCompany company="${company}" role="${someRole}" rolesOfUser="${rolesOfUser}" />
                      </td>
                    </g:each>
                  </tr>
                </g:each>
              </tbody>
            </table>
            </div>
            <div class="row text-right">
              <input class="save btn btn-default" type="submit" value="Aplicar" />
            </div>
          </div>
        </div>
        </g:form>
      </div>
    </div>
  </body>
</html>
