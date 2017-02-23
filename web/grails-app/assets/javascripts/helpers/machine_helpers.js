var MachineHelpers = (function(){

  var initHelpers = function(){
    console.log('It Works');
    Handlebars.registerHelper('isInitialState',function(state){
      var currentState = parseInt(state);
      console.log(currentState);
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
