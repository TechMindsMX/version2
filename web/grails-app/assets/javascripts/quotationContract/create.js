//= require ../third-party/jquery-validation/dist/jquery.validate.js
'use strict';

var QuotationController = (function(){
  var selectors = {
    datepicker:'#datepicker',
    datepicker1:'#datepicker1'
  },

  validateFormGeneralBalance = function validateFormGeneralBalance(){
    $("#formGeneralBalance").validate({
      rules:{
        initDate:{
          australianDate : true
        }

      },
      messages:{
        lastDate:{
          required:"Porfavor llene este campo"
        }
      },
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
    $(selectors.datepicker).datepicker('setDate', new Date());

  };

  var start = function(){
    console.log("Start controller js");
    bindEvents();
    validateFormGeneralBalance();

  };

  return{
    start:start
  }

}());
jQuery(function($){
	QuotationController.start();
});