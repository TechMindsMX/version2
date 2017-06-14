<fieldset class="form">
  <input type="hidden" name="user" value="${user.id}" />
  <f:with bean="user">
  <f:field property="username" label="${message(code:'user.username')}" wrapper="home"/>
  <f:field property="profile.name" label="${message(code:'user.name')}" wrapper="home"/>
  <f:field property="profile.lastName" label="${message(code:'user.lastName')}" wrapper="home"/>
  <f:field property="profile.motherLastName" label="${message(code:'user.motherLastName')}"wrapper="home"/>
  <f:field property="profile.email" label="${message(code:'user.email')}" wrapper="home"/>
  </f:with>
</fieldset>
<fieldset class="buttons">
  <input class="save btn btn-default" type="submit" value="${message(code: 'default.button.update.label', default: 'Update')}" />
</fieldset>

