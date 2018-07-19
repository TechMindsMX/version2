$(function () {
  $('#noSelectionAlert').hide();
});

$("#button_to_submit").on("click", () =>{
  if ($(".form-check-input").is(":checked")) {
    $('#noSelectionAlert').hide();
    $(".form_to_submit").submit();
  } else {
    $('#noSelectionAlert').show();
  }
});


