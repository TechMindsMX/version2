function complete(){
  var clabe = this.value
  var objBanco = null
  if ($("#bank").length) {
    objBanco = $("#bank option")
    $("#bank").removeAttr('readonly')
  } else {
    objBanco = $("#sbanco option")
    $("#sbanco").removeAttr('readonly')
  }
  objBanco.each(function(){
    if(clabe.substring(0,3) == this.value.substring(this.value.length-3, this.value.length)){
      if ($("#bank").length) {
        $("#bank").val(this.value)
        if (clabe.length)
          $("#bank").attr('readOnly', true)
      } else {
        $("#sbanco").val(this.value)
        if (clabe.length)
          $("#sbanco").attr('readOnly', true)
      }
      $("#branchNumber").val(clabe.substring(3,6))
      $("#accountNumber").val(clabe.substring(6,17))
    }
  })
}

$(document).ready(function(){
  $('#clabe').on('change', complete)
})

