
$('#editAliasStp').on('show.bs.modal', function (event) {
  var button = $(event.relatedTarget)
  var recipient = button.data('whatever')
  var items = recipient.split("|")
  var modal = $(this)
  modal.find('#company').val(items[0])
  modal.find('#companyName').val(items[1])
  modal.find('#aliasStp').val(items[2])
});

