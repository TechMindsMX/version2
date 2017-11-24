//= require ../third-party/jquery-validation/dist/jquery.validate.js
// //= require ../third-party/jquery-validation-bootstrap-tooltip/jquery-validate.bootstrap-tooltip.js
'use strict';

var QuotationRequestController = (function(){
  var selectors = {
    iva:'#iva',
    total:'#total',
    subtotal:'#subtotal'
  },

  calculate = function calculate(){
      var iva;
      var total; 
      console.log("Calculate");
      $(selectors.iva).val(iva);
      $(selectors.subtotal).on('keyup', function(){
          var subtotal = parseInt($(selectors.subtotal).val())
          iva = $(selectors.subtotal).val() * 0.16
          $(selectors.iva).val(iva);
          iva = parseInt(iva)
          total = iva + subtotal
          $(selectors.total).val(total);
      })
  },

  bindEvents = function bindEvents(){
    console.log("Start eventos reuqest js");
  }; 

  var start = function(){
    console.log("Start controller request js");
    bindEvents();
    calculate();

  };

  return{
    start:start
  }

}());
jQuery(function($){
	QuotationRequestController.start();
});