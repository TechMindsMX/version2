var GraphView = (function(){
 
  var renderGraph = function(graph){
    render(inner, graph);
    var center = ($('svg').width() - graph.graph().width) / 2;
    inner.attr("transform", "translate(" + center + ", 20)");
    svg.attr("height", graph.graph().height + 40);
  },

  svg = null,
  inner = null,
  render = null,

  initView = function(){
    svg = d3.select("svg");
    inner = svg.append("g");
    render = new dagreD3.render();
  };

  return{
    initView:initView,
    renderGraph:renderGraph
  };

}());
