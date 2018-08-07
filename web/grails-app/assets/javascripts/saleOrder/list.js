$( function() {
  $( "#stampedDateInit" ).datepicker({
    dateFormat: 'dd/mm/yy',
    maxDate: 0
  });
} );

$( function() {
  $( "#stampedDateEnd" ).datepicker({
    dateFormat: 'dd/mm/yy',
    maxDate: 0
  });
} );

validateRangeOfDates = function() {
	var primera = Date.parse("10/01/2013"); //01 de Octubre del 2013
var segunda = Date.parse("10/03/2013"); //03 de Octubre del 2013
 
if (primera == segunda){
    alert("Primera es igual Segunda");
} else if (primera > segunda) {
    alert("Primera mayor que Segunda");
} else{
    alert("Segunda mayor que Primera");
}
}

var DateController = (function(){
  
  initValidations = function(){
  $( "#stampedDateInit" ).datepicker({
    dateFormat: 'dd/mm/yy',
    maxDate: 0
    })
  },

  bindEvents = function(){
    console.log("inician eventos")
  },

  start = function(){
    initValidations();
    bindEvents();
  };

   return {
    start:start
  };
}());

jQuery(function($){
	DateController.start();
});