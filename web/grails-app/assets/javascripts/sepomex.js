// TODO: Refactor
function complete() {
  var zipCode = $('#zipCode').val()

  var ajax = $.ajax({
    url: $('#sepomexUrl').val() + zipCode,
    dataType: 'json',
    crossDomain: true
  })

  ajax.done(function (response) {
    console.log(response)
    if(response.id != null) {
      var colonia = $('#colony')
      var delegacion = $('#town')
      var ciudad = $('#city')
      var estado = $('#federalEntity')
      var pais = $('#country')
      var currentCol = $('#currentColony')
      var ciudadEdit = ciudad.val()

      colonia.find('option').remove()
      delegacion.val('')
      ciudad.val('')
      estado.val('')
      pais.val('')

      $.each(response.dAsenta, function (k, v) {
        if (currentCol.val()==v) {
          colonia.append('<option value="' + v + '" selected>' + v + '</option>')
        } else {
          colonia.append('<option value="' + v + '">' + v + '</option>')
        }
      })

      delegacion.val(response.dMnpio)
      var cd = (response.dCiudad != '') ? response.dCiudad : ciudadEdit
      ciudad.val(cd)
      estado.val(response.dEstado)
      pais.val(response.country)
    }else{
      error()
    }
  })

  ajax.error(function (xhr, ajaxOptions, thrownError) {
    error()
  })
}

function error() {

}

function submitForm() {
  console.log("hola");
  $("#submitFormAddress").on('click', function(){
    var colonyValue = $("#colony").val();
    if (colonyValue == "") {
      var neighboorhood = $("#neighboorhood").val();
      $("#colony").value = neighboorhood;
      $("#colony option").val(neighboorhood);
    }
    $("#formCreateAddress").submit();

  });
}

$(document).ready(function(){
  submitForm();
  $('#zipCode').on('change',complete)
  if ($('#zipCode').val()!='') {
    complete()
  }
})
