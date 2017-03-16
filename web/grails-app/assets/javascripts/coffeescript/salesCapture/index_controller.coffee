class App.IndexController
  selectors:
    partialPayment:'#partialPayment'
    addNewItem: '.addMoreItem'
    articleInfoRowTemplate: '#article-info-row'
    articleInfoRowTemplateHours: '#article-info-row-hours'
    articlesTable:'body #articles-table'
    partialPaymentTemplate: '#partialPaymentTemplate'
    checkBoxPartialPayment: '#checkboxPartialPayment'
    tableTemplate: '#tableHours'
    tableTemplateCount: '#tableCount'
    tableTemplateOnlyImport: '#tableOnlyImport'
    tbodyTemplateOnlyAmount: '#tbodyOnlyAmount'

  constructor: ->

  start: () ->
    @bindEvents()
    @datetimepicker()
    @tooltip()

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
      when "Cantidad" then new App.IndexViewController().append(@selectors.articleInfoRowTemplate,@selectors.articlesTable,{})
      when "Horas" then new App.IndexViewController().append(@selectors.articleInfoRowTemplateHours,@selectors.articlesTable,{})
      when "Solo importe" then new App.IndexViewController().append(@selectors.tbodyTemplateOnlyAmount,@selectors.articlesTable,{})
    if $('#typeOfOrden option:selected').text() =="Cantidad" then  console.log("*****hola")

  typeOfTable: (event) =>
    switch $('#typeOfOrden option:selected').text()
      when "Cantidad" then  new App.IndexViewController().render(@selectors.tableTemplateCount,@selectors.articlesTable,{})
      when "Horas" then  new App.IndexViewController().render(@selectors.tableTemplate,@selectors.articlesTable,{})
      when "Solo importe" then new App.IndexViewController().render(@selectors.tableTemplateOnlyImport,@selectors.articlesTable,{})

  wordCounter: () ->
    max_chars = 4000
    $('#terms').keyup ->
        chars = $(this).val().length
        diff = max_chars - chars
        $('#termsChars').html diff
    $('#notes').keyup ->
        chars = $(this).val().length
        diff = max_chars - chars
        $('#notesChars').html diff



  bindEvents: () ->
    $("body").on('click',@selectors.partialPayment,@partialPaymentMethod)
    $("body").on('click',@selectors.addNewItem,@addNewItemBox)
    $('#typeOfOrden').change => @typeOfTable()
    $('#hiddeAddress').click -> $('#mainAddress').toggle 'slow'
    @wordCounter()


new App.IndexController().start()
