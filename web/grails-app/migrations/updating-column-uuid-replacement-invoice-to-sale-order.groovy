databaseChangeLog = {

  changeSet(author: "karla (manual)", id: "updating-column-uuid-replacement-invoice-to-sale-order") {
    grailsChange {
      change {
        sql.execute("ALTER TABLE sale_order CHANGE uuid_replacement uuid_replacement VARCHAR(255);")
      }
    }
  }

}
