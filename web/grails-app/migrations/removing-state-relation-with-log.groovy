databaseChangeLog = {

  changeSet(author: "makingdevs (generated)", id: "1489009702169-1") {
    addColumn(tableName: "tracking_log") {
      column(name: "state", type: "varchar(255)") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "makingdevs (generated)", id: "1489009702169-2") {
    dropForeignKeyConstraint(baseTableName: "tracking_log", constraintName: "FK_ls87l50wof9a7mrjhcgqtwfhm")
  }

  changeSet(author: "makingdevs (generated)", id: "1489009702169-3") {
    dropColumn(columnName: "state_id", tableName: "tracking_log")
  }

}
