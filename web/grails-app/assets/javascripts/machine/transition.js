//= require machine/state.js

var Transition = {
  stateFrom:null,
  stateTo:null, 
  actions:[],

  create:function(data){
    return $.extend({},this,data);
  }

};
