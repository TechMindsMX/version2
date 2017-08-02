databaseChangeLog = {

  changeSet(author: "makingdevs (generated)", id: "1500865900596-1") {
    createTable(tableName: "menu") {
      column(autoIncrement: "true", name: "id", type: "BIGINT") {
        constraints(primaryKey: "true", primaryKeyName: "menuPK")
      }

      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "internal_url", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }

      column(name: "name", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }

      column(name: "parent_menu_id", type: "BIGINT")
    }
  }

  changeSet(author: "makingdevs (generated)", id: "1500865900596-2") {
    addForeignKeyConstraint(baseColumnNames: "parent_menu_id", baseTableName: "menu", constraintName: "FKht6h4dvumr09qxk95r1qcgjd8", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "menu")
  }
}
