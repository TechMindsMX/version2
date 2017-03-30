databaseChangeLog = {

  changeSet(author: "makingdevs (generated)", id: "1490723447694-1") {
    createTable(tableName: "mock_instance") {
      column(autoIncrement: "true", name: "id", type: "BIGINT") {
        constraints(primaryKey: "true", primaryKeyName: "mock_instancePK")
      }

      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "name", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }
    }
  }
}
