databaseChangeLog = {

  changeSet(author: "temoc (manual)", id: "moving-uuid-to-folio-from-sale-order") {
    grailsChange {
      change {
        sql.execute("update sale_order set folio = uuid")
        sql.execute("update sale_order set uuid = ${UUID.randomUUID().toString().replace('-','')[0..15]}")
      }
    }
  }

}
