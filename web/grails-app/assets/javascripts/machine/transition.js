var Transition = {
  stateFrom:'',
  stateTo:'', 
  action:'',
  actionId:'',

  create:function(data){
    return $.extend({},this,data);
  }

};
