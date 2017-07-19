$("#selectAll").click( function() {
  if ($("#selectAll").is(":checked")) {
    $("input[name='checkBe']").each ( function(index) {
      $(this).prop("checked", true);
      addIdBusinessEntityToAuthorize($(this).val())
    });
  } else {
    $("input[name='checkBe']").each ( function(index) {
      $(this).prop("checked", false);
      quitIdBusinessEntityToAuthorize($(this).val())
    });
  }
});

function addIdBusinessEntityToAuthorize(id) {
  if ($("#entities").val() == "") {
    $("#entities").val(id);
  } else {
    $("#entities").val($("#entities").val()+","+id);
  }
  verifyCheckSelectAll();
}

function quitIdBusinessEntityToAuthorize(id) {
  var entities = $("#entities").val();
  entities = entities.replace(','+id,'').replace(id+',','').replace(id,'').replace(',,',',');
  $("#entities").val(entities);
  verifyCheckSelectAll();
}

function verifyCheckSelectAll() {
  var total = $("input[name='checkBe']").length;
  var countCheckeds = 0;
  $("input[name='checkBe']").each ( function(index) {
    if ($(this).prop("checked")) {
      countCheckeds++;
    }
  });
  if (countCheckeds == total) {
    $("#selectAll").prop("checked", true);
  } else {
    $("#selectAll").prop("checked", false);
  }
}

$('.entity').click(function() {
  var valor = $(this).val();
  if ($(this).prop("checked")) {
    addIdBusinessEntityToAuthorize($(this).val())
  } else {
    quitIdBusinessEntityToAuthorize($(this).val())
  }
});
