databaseChangeLog = {

  changeSet(author: "makingdevs (generated)", id: "1489178419182-1") {
    addColumn(tableName: "machine") {
      column(name: "uuid", type: "varchar(255)") {
        constraints(nullable: "false")
      }
    }
  }

}
