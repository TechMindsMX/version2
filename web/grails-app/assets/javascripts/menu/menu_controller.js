$("#button_to_submit").on("click", () =>{
  if ($(".form-check-input").is(":checked")) {
    $(".form_to_submit").submit()
  }
  else {
    $('#button_to_submit').popover('show');
  }
})
$(".form-check-input").on("click", ()=>{
  $('#button_to_submit').popover('hide');
})

