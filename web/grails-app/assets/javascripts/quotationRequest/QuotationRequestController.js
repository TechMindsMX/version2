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
      var ivaRate = parseFloat($("#ivaRate").val())
      var subtotal;
      console.log("Calculate");
      $(selectors.subtotal).on('keyup', function(){
          subtotal = parseFloat($(selectors.subtotal).val())
          iva = $(selectors.subtotal).val() * (ivaRate/100)
          $(selectors.iva).val(iva);
          iva = parseFloat(iva)
          total = subtotal * (1.0 + (ivaRate/100))
          total = Math.round(total * 100) / 100
          iva = Math.round(iva * 100) / 100 
          $(selectors.iva).val(iva);
          $(selectors.total).val(total);
      });
      $(selectors.total).on('keyup', function(){
        var total = parseFloat($(selectors.total).val())
        $(selectors.iva).val(iva);
        subtotal = total / (1.0 + (ivaRate/100))
        subtotal = Math.round(subtotal * 100) / 100
        iva = total - subtotal
        iva = Math.round(iva * 100) / 100 
        $(selectors.iva).val(iva);
        $(selectors.subtotal).val(subtotal);
    });
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