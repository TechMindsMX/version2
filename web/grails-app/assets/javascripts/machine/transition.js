//= require machine/state.js

var Transition = {
  stateFrom:null,
  stateTo:null, 
  action:'',
  actionId:'',

  create:function(data){
    return $.extend({},this,data);
  }

};
