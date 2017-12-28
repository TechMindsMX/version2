databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1512416578117-1") {
        addColumn(tableName: "sale_order_item") {
            column(name: "sat_key", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

  changeSet(author: "temoc (manual)", id: "updating-sat-key-into-sale-order-items") {
    grailsChange {
      change {
        sql.execute("update sale_order_item set sat_key='01010101'")
      }
    }
  }

}
