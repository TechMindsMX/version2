databaseChangeLog = {

    changeSet(author: "makingdevs (generated)", id: "1514484226053-3") {
        addColumn(tableName: "corporate") {
            column(name: "status", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author:"makingdevs (manual)", id:"initializing-status-for-all-corporates") {
      grailsChange {
        change {
          sql.execute("update corporate set status='ENABLED'")
        }
      }
    }
}
