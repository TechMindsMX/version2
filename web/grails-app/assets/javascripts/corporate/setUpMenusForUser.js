$("#divListMenus").hide();

$("#roleId").change( function() {
  var theRole = $("#roleId").val()
  $("#listMenus").empty();
  if ($("#roleId").val() == "") {
    $("#divListMenus").hide();
    return;
  }
  $("#divListMenus").show();

  var consulta = $.ajax({
    type:'GET',
    url:'../getMenusForRole',
    data:{role:theRole},
    datatype:'JSON'
  });

  /* En caso de que se haya retornado bien.. */
  consulta.done(function(data){
    if(data.error!==undefined){
      return false;
    } else {
      $.each(data, function(k,v) {
        $("#listMenus").append("<tr><td colspan='2' class='col-md-10'><strong>" + v.name.toUpperCase() + "</strong></td><td class='col-md-2 text-center'><input id='chkMenu_" + v.id + "' type='checkBox' name='menuId" + v.id + "'/></td></tr>");
        $.each(v.menus, function(sk, sv) {
          $("#listMenus").append("<tr><td class='col-md-1 text-center'></td><td class='col-md-9'>" + sv.name + "</td><td class='col-md-2 text-center'><input id='chkSubMenu_" + v.id + "' class='submenu_" + v.id + "' type='checkBox' name='menuId" + sv.id + "'/></td></tr>");
        });
      });
      return true;
    }
  });

  /* Si la consulta ha fallado.. */
  consulta.fail(function(){
    return false;
  });
});

$(document).on("click", "input[id^=chkMenu_]", function(event) {
  var targetId = event.target.id;
  var menuId = targetId.split("_")[1];
  var submenus = $("input.submenu_"+menuId+"[type=checkbox]");
  $.each(submenus, function(k, subm) {
    $(subm).prop("checked", event.target.checked);
  });
});

$(document).on("click", "input[id^=chkSubMenu_]", function(event) {
  var targetId = event.target.id;
  var menuId = targetId.split("_")[1];
  var submenus = $("input.submenu_"+menuId+"[type=checkbox]");
  var parentMenu = $("#chkMenu_"+menuId);
  if (event.target.checked) {
    $(parentMenu).prop("checked", true);
  } else {
    var nothingChecked = true;
    $.each(submenus, function(k, subm) {
      if ($(subm).prop("checked")) {
        nothingChecked = false;
      }
    });
    if (nothingChecked) {
      $(parentMenu).prop("checked", false);
    }
  }
});

$("#buttonApplyUserMenus").click(function(event) {
  event.stopImmediatePropagation();
  event.preventDefault();
  var checkedSubmenus = $("#formUserMenus input:checkbox:checked").length;
  if (checkedSubmenus) {
    $('#noSelectionModal').modal('hide');
    //document.formUserMenus.submit();
  } else {
    $('#noSelectionModal').modal('show');
  }
});

