databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1536768575635-1") {
        addColumn(tableName: "menu") {
            column(name: "position", type: "integer") {
                constraints(nullable: "false")
            }
        }
    }

  changeSet(author: "temoc (manual)", id: "initialize-position-into-menu") {
    grailsChange {
      change {
        sql.execute("update menu set position=1")
      }
    }
  }

}
