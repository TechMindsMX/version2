var MachineCreateView = (function(){
  
  render = function(templateName,divDestiny,data){
    var source = $(templateName).html();
    var template = Handlebars.compile(source); 
    var html = template(data);
    $(divDestiny).html(html);
  };

  return{
    render:render
  }

}());
