databaseChangeLog = {

  changeSet(author: "sohjiro (generated)", id: "1578807151040-1") {
    addColumn(tableName: "corporate") {
      column(name: "has_credit", type: "bit") {
        constraints(nullable: "false")
      }
    }
  }
}
