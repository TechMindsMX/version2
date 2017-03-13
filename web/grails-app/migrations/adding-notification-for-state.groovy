databaseChangeLog = {

  changeSet(author: "makingdevs (generated)", id: "1489422143942-1") {
    createTable(tableName: "notificacion_for_state") {
      column(autoIncrement: "true", name: "id", type: "BIGINT") {
        constraints(primaryKey: "true", primaryKeyName: "notificacion_for_statePK")
      }

      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "group_notification_id", type: "BIGINT") {
        constraints(nullable: "false")
      }
    }
  }
}
