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
    specifyDateTemplate: '#specifyDateTemplate'
    destinationnyDateTemplate: '#divVencimiento'
    deleteItem: '.deleteItem'

  constructor: ->

  start: () ->
    @bindEvents()
    @datetimepicker()
    @tooltip()
    @getNumberAttribute()

  datetimepicker: () ->
    $('#datetimepicker1').datetimepicker({format: 'DD/MM/YYYY', locale: 'en' })
    $('#datetimepicker2').datetimepicker({format: 'DD/MM/YYYY', locale: 'en' })

  tooltip: () ->
    $('[data-toggle="tooltip"]').tooltip()

  partialPaymentMethod: () =>
    if $('#partialPayment').is(':checked') then new App.IndexViewController().append(@selectors.partialPaymentTemplate,@selectors.checkBoxPartialPayment,{})
    else  $('#rowPartialPayment').remove()


  addNewItemBox: (event) =>
    event.preventDefault()
    switch $('#typeOfOrden option:selected').text()
      when "Cantidad" then new App.IndexViewController().append(@selectors.articleInfoRowTemplate,@selectors.articlesTable,{index:@getNumberAttribute()})
      when "Horas" then new App.IndexViewController().append(@selectors.articleInfoRowTemplateHours,@selectors.articlesTable,{index:@getNumberAttribute()})
      when "Solo importe" then new App.IndexViewController().append(@selectors.tbodyTemplateOnlyAmount,@selectors.articlesTable,{index:@getNumberAttribute()})
    if $('#typeOfOrden option:selected').text() =="Cantidad" then  console.log("*****hola")

  deleteItemBox: =>
    console.log('escuchado')
    tbodyToDelete= $('.deleteItem').parents('tbody')
    console.log(tbodyToDelete)
    console.log('++++++++++++++++')
    d =event.currentTarget
    r = $(d).parents('tbody')
    console.log(r)

  typeOfTable: (event) =>
    switch $('#typeOfOrden option:selected').text()
      when "Cantidad" then  new App.IndexViewController().render(@selectors.tableTemplateCount,@selectors.articlesTable,{})
      when "Horas" then  new App.IndexViewController().render(@selectors.tableTemplate,@selectors.articlesTable,{})
      when "Solo importe" then new App.IndexViewController().render(@selectors.tableTemplateOnlyImport,@selectors.articlesTable,{})

  specifyDate: (event)->
    if $('#selectDate option:selected').text() == "En la fecha especificada" then new App.IndexViewController().append(@selectors.specifyDateTemplate,@selectors.destinationnyDateTemplate,{})
    else $('body #divDataTimer').remove()

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

  getNumberAttribute: () ->
   attribute =  $('body #itemName_0').attr 'name'
   regularExpresion = /\d/
   index = parseInt(regularExpresion.exec(attribute))
   console.log(index)
   index = index+1
   @findNumberOftbody()

  findNumberOftbody:() ->
    r = $('#articles-table').children('tbody')
    console.log(r.length)
    r.length

  bindEvents: () ->
    $("body").on('click',@selectors.partialPayment,@partialPaymentMethod)
    $("body").on('click',@selectors.addNewItem,@addNewItemBox)
    $('#typeOfOrden').change => @typeOfTable()
    $('#hiddeAddress').click -> $('#mainAddress').toggle 'slow'
    @wordCounter()
    $('#selectDate').change => @specifyDate(); @datetimepicker()
    $('#buttonPreview').click => @getNumberAttribute(); @findNumberOftbody()
    $('body').on('click',@selectors.deleteItem,(e) -> console.log($(e.currentTarget).parents("tbody").remove()) )


new App.IndexController().start()
