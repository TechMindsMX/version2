$(function () {
  $('#noSelectionModal').modal('hide');
});

$("#button_to_submit").click(function() {
  var checkedSubmenues = $("#menuOption:checked").val() ? true : false;
  if (checkedSubmenues) {
    $('#noSelectionModal').modal('hide');
    document.formSubmenues.submit();
  } else {
    $('#noSelectionModal').modal('show');
  }
});


