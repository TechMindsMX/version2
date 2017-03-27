databaseChangeLog = {
  changeSet(author: "makingdevs (generated)", id: "1490631034478-43") {
    createTable(tableName: "notification_for_state") {
      column(autoIncrement: "true", name: "id", type: "BIGINT") {
        constraints(primaryKey: "true", primaryKeyName: "notification_for_statePK")
      }

      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "group_notification", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "state_machine", type: "BIGINT") {
        constraints(nullable: "false")
      }
    }
  }
}
