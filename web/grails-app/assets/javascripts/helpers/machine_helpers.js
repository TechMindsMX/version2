var MachineHelpers = (function(){

  var initHelpers = function(){
    Handlebars.registerHelper('isInitialState',function(state){
      var currentState = parseInt(state);
      return currentState == 1 ? "Inicio" : currentState;
    });
  };

  return{
    initHelpers:initHelpers 
  };

}());

jQuery(function($){
  MachineHelpers.initHelpers();
});
