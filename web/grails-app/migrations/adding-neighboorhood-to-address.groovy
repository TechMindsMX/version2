databaseChangeLog = {

  changeSet(author: "cggg88jorge (generated)", id: "1490892433443-1") {
    addColumn(tableName: "address") {
      column(name: "neighboorhood", type: "varchar(255)")
    }
  }

  changeSet(author: "cggg88jorge (generated)", id: "1490892433443-198") {
    dropNotNullConstraint(columnDataType: "varchar(255)", columnName: "colony", tableName: "address")
  }
  
}
