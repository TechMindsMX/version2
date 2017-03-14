class App.IndexViewController

  constructor: ->

  append:(template,destiny,data) ->
    source = $(template).html()
    template = Handlebars.compile(source)
    html = template(data)
    $(destiny).append(html)

  render:(template,destiny,data) ->
    source = $(template).html()
    template = Handlebars.compile(source)
    html = template(data)
    $(destiny).render(html)



