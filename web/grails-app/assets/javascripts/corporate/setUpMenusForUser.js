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
      console.log("Data: " + JSON.stringify(data));
      $.each(data, function(k,v) {
        $("#listMenus").append("<tr><td colspan='2' class='col-md-10'>" + v.name + "</td><td class='col-md-2 text-center'><input class='menu" + v.id + "' type='checkBox' name='menuId" + v.id + "'/></td></tr>");
        $.each(v.menus, function(sk, sv) {
          $("#listMenus").append("<tr><td class='col-md-1 text-center'></td><td class='col-md-9'>" + sv.name + "</td><td class='col-md-2 text-center'><input class='submenu" + v.id + "' type='checkBox' name='menuId" + sv.id + "'/></td></tr>");
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
