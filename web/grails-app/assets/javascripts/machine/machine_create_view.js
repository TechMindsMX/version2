var MachineCreateView = (function(){
  var selectors = {
    templateName:'#transitionsTable',
    divDestiny:'#transitionsTableContainer'
  },
  
  render = function(data){
    var source = $(selectors.templateName).html();
    var template = Handlebars.compile(source); 
    var html = template({machine:data});
    $(selectors.divDestiny).html(html);
  };

  return{
    render:render
  }

}());
