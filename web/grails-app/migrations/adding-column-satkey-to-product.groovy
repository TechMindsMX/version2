databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1511807167739-1") {
        addColumn(tableName: "product") {
            column(name: "sat_key", type: "varchar(8)") {
                constraints(nullable: "false")
            }
        }
    }

  changeSet(author: "temoc (manual)", id: "updating-sat-key-to-products") {
    grailsChange {
      change {
        sql.execute("update product set sat_key='01010101'")
      }
    }
  }

}
