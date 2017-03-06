databaseChangeLog = {
  changeSet(author: "temoc (manual)", id: "updating-type-venta-to-factura-in-commission") {
    grailsChange {
      change {
        sql.execute("update commission set type='FACTURA' where type='VENTA'")
      }
    }
  }
}

