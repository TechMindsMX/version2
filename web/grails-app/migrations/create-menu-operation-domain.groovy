databaseChangeLog = {

  changeSet(author: "makingdevs (generated)", id: "1500410536615-1") {
    createTable(tableName: "menu_operation") {
      column(autoIncrement: "true", name: "id", type: "BIGINT") {
        constraints(primaryKey: "true", primaryKeyName: "menu_operationPK")
      }

      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "date_created", type: "datetime") {
        constraints(nullable: "false")
      }

      column(name: "internal_url", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }

      column(name: "last_updated", type: "datetime") {
        constraints(nullable: "false")
      }

      column(name: "name", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }
    }
  }
}
