class App.IndexController
  selectors:
    partialPayment:'#partialPayment'
    addNewItem: '.addMoreItem'
    articleInfoRowTemplate: '#article-info-row'
    articleInfoRoeTemplateHours: '#article-info-row-hours'
    articlesTable:'#articles-table'
    partialPaymentTemplate: '#partialPaymentTemplate'
    checkBoxPartialPayment: '#checkboxPartialPayment'

  constructor: ->

  start: () ->
    @bindEvents()
    @datetimepicker()
    @tooltip()
    @typeOfTable()

  datetimepicker: () ->
    console.log("hola")
    $('#datetimepicker1').datetimepicker({format: 'DD/MM/YYYY', locale: 'en' })

  tooltip: () ->
    $('[data-toggle="tooltip"]').tooltip()

  partialPaymentMethod: () =>
    if $('#partialPayment').is(':checked') then new App.IndexViewController().append(@selectors.partialPaymentTemplate,@selectors.checkBoxPartialPayment,{})
    else  $('#rowPartialPayment').remove()


  addNewItemBox: (event) =>
    event.preventDefault()
    switch $('#typeOfOrden option:selected').text()
      when "Cantidad" then console.log("Cantidad")
      when "Horas" then console.log("Horas")
      when "Solo Importe" then console.log("Solo importe")
    if $('#typeOfOrden option:selected').text() =="Cantidad" then  console.log("*****hola")
    new App.IndexViewController().append(@selectors.articleInfoRowTemplate,@selectors.articlesTable,{})

  typeOfTable: (event) =>
    switch $('#typeOfOrden option:selected').text()
      when "Cantidad" then console.log("Predeterminado")
      when "Horas" then console.log("Debe cambiar")
      when "Solo importe" then console.log("Si esta funcionando")

  bindEvents: () ->
    $(@selectors.partialPayment).on('click',@partialPaymentMethod)
    $(@selectors.addNewItem).on('click',@addNewItemBox)
    $('#typeOfOrden').change =>  @typeOfTable()


new App.IndexController().start()
