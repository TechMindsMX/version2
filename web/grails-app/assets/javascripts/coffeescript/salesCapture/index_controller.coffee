class App.IndexController
  stateOfTable=1
  countertbody=0
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
    destinationnyDateTemplate: '#formDate'
    deleteItem: '.deleteItem'
  selectorsTablaCount:
    quantity: '.inputsQuantity'
    price: '.inputsPrice'
    amount: '.inputsAmount'
  selectorsTableHours:
    hours:'.inputsHours'
    rate:'.inputsRate'
    amount:'.inputsAmount'
  selectorsTableOnlyImport:
    amount:'.inputsAmount'

  selectorOfCalculationTotal:
    subtotal: '#inputSubtotal'
    discount: '#inputTotalDiscount'
    discountPayment: '#inputDiscountPayment'
    typeOfDiscount: '#selectorTypeDiscount'
    shippingCost: '#inputShippingCost'
    totalSale: '#inputTotalSale'
    shippingPayment: '#inputShippingPayment'


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
   index = index+1
   @findNumberOftbody()

  findNumberOftbody:() ->
    r = $('#articles-table').children('tbody')
    r.length + countertbody

  calculationOfAmount: (event) =>
    switch $('#typeOfOrden option:selected').text()
      when "Cantidad"
        quantity = $(event.target).parents("tr").find(@selectorsTablaCount.quantity).val()
        price = $(event.target).parents("tr").find(@selectorsTablaCount.price).val()
        tax =  (quantity * price)*0.16
        amount = (quantity * price) + tax
        $(event.target).parents("tr").find(@selectorsTablaCount.amount).val("$ #{amount}")
        @calculationTotal()
      when "Horas"
        console.log("Horas De la tabla")
        hours = $(event.target).parents("tr").find(@selectorsTableHours.hours).val()
        rate = $(event.target).parents("tr").find(@selectorsTableHours.rate).val()
        tax =  (hours * rate)*0.16
        console.log(tax)
        amount = (hours * rate) + tax
        $(event.target).parents("tr").find(@selectorsTableHours.amount).val("$ #{amount}")
        @calculationTotal()

      when "Solo importe"
        console.log("Solo importe de la tabla")
        @calculationTotal()

  calculationTotal: () =>
    regularExpresion = /\$ /
    switch $('#typeOfOrden option:selected').text()
      when "Cantidad"
        sumAmount = 0
        console.log("Cantidad")
        $.each $(@selectorsTablaCount.amount), (index, input) ->
            sumAmount = sumAmount + parseFloat($(input).val().replace(regularExpresion, ''))

      when "Horas"
        console.log("Horas")
        sumAmount = 0
        console.log("Cantidad")
        $.each $(@selectorsTableHours.amount), (index, input) ->
            sumAmount = sumAmount + parseFloat($(input).val().replace(regularExpresion, ''))

      when "Solo importe"
        console.log("Solo importe")
        sumAmount = 0
        console.log("Cantidad")
        $.each $(@selectorsTableOnlyImport.amount), (index, input) ->
            sumAmount = sumAmount + parseFloat($(input).val().replace(regularExpresion, ''))
        sumAmount = sumAmount + (sumAmount*0.16)
        console.log(sumAmount)

    $(@selectorOfCalculationTotal.subtotal).val("$ #{sumAmount.toFixed(2)}")
    discount = $(@selectorOfCalculationTotal.discountPayment).val()
    console.log(discount)
    if $('#selectorTypeDiscount option:selected').text()=="$" then discount = discount
    if $('#selectorTypeDiscount option:selected').text()=="%" then discount = sumAmount*(discount/100); console.log(discount)
    $(@selectorOfCalculationTotal.discount).val("$ #{discount}")
    sendPayment=parseFloat( $(@selectorOfCalculationTotal.shippingCost).val())
    $(@selectorOfCalculationTotal.shippingPayment).val("$ #{sendPayment}")
    totalPayment = sumAmount - discount + sendPayment
    $(@selectorOfCalculationTotal.totalSale).val("$ #{totalPayment}")

  bindEvents: () ->
    $("body").on('click',@selectors.partialPayment,@partialPaymentMethod)
    $("body").on('click',@selectors.addNewItem,@addNewItemBox)
    $('#typeOfOrden').change => @typeOfTable(); @calculationTotal()
    $('#hiddeAddress').click -> $('#mainAddress').toggle 'slow'
    @wordCounter()
    $('#selectDate').change => @specifyDate(); @datetimepicker()
    $('#buttonPreview').click => @getNumberAttribute(); @findNumberOftbody()
    $('body').on('click',@selectors.deleteItem,(e) => $(e.currentTarget).parents("tbody").remove(); countertbody++; @calculationTotal() )
    $('body').on("keyup",@selectorsTablaCount.quantity, @calculationOfAmount)
    $('body').on("keyup#{@selectorsTablaCount.price}", @calculationOfAmount)


new App.IndexController().start()
