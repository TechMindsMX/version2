databaseChangeLog = {

    changeSet(author: "iMacLeo (generated)", id: "1511535892037-14") {
        dropColumn(columnName: "amount", tableName: "quotation_request")
    }

  changeSet(author: "brandonVergara (manual)", id: "quotation_request_delete") {
    grailsChange {
      change {
        sql.execute("delete from quotation_request ")
      }
    }
  }
 
}
