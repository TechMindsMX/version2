//= require ../third-party/jquery-validation/dist/jquery.validate.js
// //= require ../third-party/jquery-validation-bootstrap-tooltip/jquery-validate.bootstrap-tooltip.js
'use strict';

var QuotationController = (function(){
  var selectors = {
    datepicker:'#datepicker',
    datepicker1:'#datepicker1',
    reuqestPaymentForm:'#requestPayment',
    availableRequest:'#available'
  },

  validateFormGeneralBalance = function validateFormGeneralBalance(){
    $("#formGeneralBalance").validate({
      rules:{
        initDate:{
          australianDate : true,
          required:true
        },
        lastDate:{
          required: true
        }
      },
      messages:{
        lastDate:{
          required:"Porfavor llene este campo"
        }
      },
   //   tooltip_options: {
   //     initDate: { placement: 'left' },
   //     lastDate: { placement: 'left' }
   //   },
      submitHandler: function(form) {
        // some other code
        // maybe disabling submit button
        // then:
        $(form).submit();
      }
     });

     $.validator.addMethod(
      "australianDate",
      function(value, element) {
          // put your own logic here, this is just a (crappy) example
          return value.match(/^\d\d?\-\d\d?\-\d\d\d\d$/);
      },
      "Porfavor use el siguiente formato dd-mm-yyyy."
      );
  },

  validateFormRequestPayment = function validateFormRequestPayment(){
    var avilitable = parseFloat($(selectors.availableRequest).val())
    $(selectors.reuqestPaymentForm).validate({
      rules:{
        amount:{
          required:true,
          max: avilitable
        },
        note:{
          required: true
        }
      },
      messages:{
        amount:{
          required:"Porfavor llene este campo",
          max:"El monto excede el disponible"
        }
      },
      submitHandler: function(form) {
        $(form).submit();
      }
     });

  },

  bindEvents = function bindEvents(){
    console.log("Satrt bindEvents");
    $(selectors.datepicker).datepicker({
      setDate: new Date(),
      dateFormat: 'dd-mm-yy'
    });
    $( "#datepickerQuotation" ).datepicker({
      setDate: new Date(),
      dateFormat: 'dd/mm/yy'
    });
    $(selectors.datepicker1).datepicker({
      dateFormat: 'dd-mm-yy'
    });
  };

  var start = function(){
    console.log("Start controller js");
    bindEvents();
    validateFormGeneralBalance();
    validateFormRequestPayment();
  };

  return{
    start:start
  }

}());
jQuery(function($){
	QuotationController.start();
});