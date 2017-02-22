databaseChangeLog = {

  changeSet(author: "makingdevs (generated)", id: "1487798983216-1") {
    addColumn(tableName: "machinery_link") {
      column(name: "company_ref", type: "bigint") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "makingdevs (generated)", id: "1487798983216-2") {
    dropColumn(columnName: "machinery_ref", tableName: "machinery_link")
  }

}
