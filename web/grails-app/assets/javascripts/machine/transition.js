//= require machine/state.js

var Transition = {
  stateFrom:null,
  stateTo:null, 
  actions:[],

  create: function(data){
    var newTransition = $.extend({},this,data);
    newTransition.actions = [];
    return newTransition;
  }

};
