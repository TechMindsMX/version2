$( window ).on( "load", function() {
  $( function() {
    $( "#datepicker" ).datepicker({
      setDate: new Date(),
      dateFormat: 'dd-mm-yy'
    });
  } );
  $( function() {
    $( "#datepickerQuotation" ).datepicker({
      setDate: new Date(),
      dateFormat: 'dd/mm/yy'
    });
  } );

  $( function() {
    $( "#datepicker1" ).datepicker({
      dateFormat: 'dd-mm-yy'
    });
  } );
  $('#datepicker').datepicker('setDate', new Date());
});

