databaseChangeLog = {

  changeSet(author: "makingdevs (generated)", id: "1487617201649-1") {
    createTable(tableName: "group_notification") {
      column(autoIncrement: "true", name: "id", type: "BIGINT") {
        constraints(primaryKey: "true", primaryKeyName: "group_notificationPK")
      }

      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "name", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }

      column(name: "notification_id", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "makingdevs (generated)", id: "1487617201649-2") {
    createTable(tableName: "group_notification_user") {
      column(name: "group_notification_users_id", type: "BIGINT")

        column(name: "user_id", type: "BIGINT")
    }
  }

  changeSet(author: "makingdevs (generated)", id: "1487617201649-3") {
    addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "group_notification_user", constraintName: "FK_efr9u42sqjj05460tq9kvn7f3", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user")
  }

  changeSet(author: "makingdevs (generated)", id: "1487617201649-4") {
    addForeignKeyConstraint(baseColumnNames: "group_notification_users_id", baseTableName: "group_notification_user", constraintName: "FK_rr8ehwqj4pdygax7etxeuou0i", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "group_notification")
  }

}
