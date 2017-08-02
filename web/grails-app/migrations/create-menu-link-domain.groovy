databaseChangeLog = {

  changeSet(author: "makingdevs (generated)", id: "1500953985051-1") {
    createTable(tableName: "menu_link") {
      column(autoIncrement: "true", name: "id", type: "BIGINT") {
        constraints(primaryKey: "true", primaryKeyName: "menu_linkPK")
      }

      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "menu_id", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "menu_ref", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "type", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "makingdevs (generated)", id: "1500953985051-2") {
    addForeignKeyConstraint(baseColumnNames: "menu_id", baseTableName: "menu_link", constraintName: "FKbcixesrm2m0r9myvdl5bnw9h3", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "menu")
  }
}
