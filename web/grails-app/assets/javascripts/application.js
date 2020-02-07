//= require third-party/jquery/dist/jquery.js
//= require third-party/bootstrap/dist/js/bootstrap.js
//= require third-party/jquery-slimscroll/jquery.slimscroll.js
//= require third-party/hisrc/hisrc.js
//= require third-party/jquery-ui/jquery-ui.js
//= require third-party/modulus-uno-theme/js/flex.js
//= require home/home_controller.js

var App = (function(){

  var start = function(){
    HomeController.start();
  };

  return{
    start:start
  };

}());

jQuery(function($){
  App.start();
});
