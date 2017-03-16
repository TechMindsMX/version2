var MachineView = (function(){
  
  render = function(templateName,divDestiny,data){
    console.log($(divDestiny));
    var source = $(templateName).html();
    var template = Handlebars.compile(source); 
    var html = template(data);
    console.log(data);
    $(divDestiny).html(html);
  };

  return{
    render:render
  }

}());
