databaseChangeLog = {

  changeSet(author: "makingdevs (generated)", id: "1487281778501-1") {
    addColumn(tableName: "transition") {
      column(name: "state_id", type: "bigint") {
        constraints(nullable: "false")
      }
    }
  }

}
