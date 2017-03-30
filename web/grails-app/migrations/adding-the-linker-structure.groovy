databaseChangeLog = {

  changeSet(author: "makingdevs (generated)", id: "1490915634713-1") {
    createTable(tableName: "combination") {
      column(autoIncrement: "true", name: "id", type: "BIGINT") {
        constraints(primaryKey: "true", primaryKeyName: "combinationPK")
      }

      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "class_left", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }

      column(name: "class_right", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }

      column(name: "date_created", type: "datetime") {
        constraints(nullable: "false")
      }

      column(name: "last_updated", type: "datetime") {
        constraints(nullable: "false")
      }

      column(name: "right_instance_id", type: "BIGINT") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "makingdevs (generated)", id: "1490915634713-2") {
    createTable(tableName: "combination_link") {
      column(autoIncrement: "true", name: "id", type: "BIGINT") {
        constraints(primaryKey: "true", primaryKeyName: "combination_linkPK")
      }

      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "combination_id", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "date_created", type: "datetime") {
        constraints(nullable: "false")
      }

      column(name: "instance_ref", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "last_updated", type: "datetime") {
        constraints(nullable: "false")
      }

      column(name: "type", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "makingdevs (generated)", id: "1490915634713-3") {
    addForeignKeyConstraint(baseColumnNames: "combination_id", baseTableName: "combination_link", constraintName: "FKrryphlpdlf6swyganswjtfhvs", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "combination")
  }

}
