<g:each var="menu" in="${menus.sort{it.position}}">
  <li class="panel">
    <g:if test="${menu.menus.size() != 0 }">
      <a href="javascript:;" data-parent="#side" data-toggle="collapse" class="accordion-toggle" data-target="#${menu.name.replace(' ','')}">
        ${menu.name} <i class="fa fa-caret-down"></i>
      </a>
      <ul class="collapse nav" id="${menu.name.replace(' ','')}">
        <g:each var="submenu" in="${menu.menus.sort{it.position}}">
          <li>
            <a href="${submenu.internalUrl}${evaluate(submenu.parameters ?: '')}">${submenu.name}</a>
          </li>
        </g:each>
      </ul>
    </g:if>
    <g:else>
      <a href="${menu.internalUrl}${evaluate(menu.parameters ?: '')}">${menu.name}</a>
    </g:else>
  </li>
</g:each>
