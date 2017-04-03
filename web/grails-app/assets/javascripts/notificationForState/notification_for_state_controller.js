//= require third-party/jquery-validation/dist/jquery.validate.js

var NotificationForStateController = (function(){
  
  var selectors = {
    notificationForStateForm:'form[name=notificationForStateForm]',
    entitySelect:'select[name=entity]',
    companySelect:'select[name=company]',
    actionListURL:'#actionListURL'
  },

  initValidations = function(){
    $(selectors.notificationForStateForm).validate({
      rules:{
        entity:{
          required:true
        },
        company:{
          required:true
        }
      },
      errorPlacement: function(error,element){
        $(element).parent('div').addClass('has-error');
      },
      success: function(label,element){
        $(element).parent('div').removeClass('has-error');
      }
    });
  },

  showMachines = function(event){
    event.preventDefault();

    var entitySelect = $(selectors.entitySelect),
    companySelect = $(selectors.companySelect);

    var machines = [];

    $.get($(selectors.actionListURL).val(),{className:entitySelect.val(),
                                            company:companySelect.val()},function(data){
      data.forEach(function(machine){
        machines.push(machine);
      });

    })
  },

  bindEvents = function(){
    $(selectors.notificationForStateForm).on('submit',showMachines);
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
  NotificationForStateController.start();
});
