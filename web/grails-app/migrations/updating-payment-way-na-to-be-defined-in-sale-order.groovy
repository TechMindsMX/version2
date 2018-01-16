databaseChangeLog = {

  changeSet(author: "temoc (manual)", id: "update-payment-way-na-to-be-defined-in-sale-order") {
    grailsChange {
      change {
        sql.execute("update sale_order set payment_way='POR_DEFINIR' where payment_way='NA'")
      }
    }
  }

}
