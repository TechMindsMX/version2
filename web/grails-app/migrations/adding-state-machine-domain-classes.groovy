databaseChangeLog = {

  changeSet(author: "makingdevs (generated)", id: "1487186868383-1") {
    createTable(tableName: "action") {
      column(autoIncrement: "true", name: "id", type: "BIGINT") {
        constraints(primaryKey: "true", primaryKeyName: "actionPK")
      }

      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "date_created", type: "datetime") {
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

  changeSet(author: "makingdevs (generated)", id: "1487186868383-2") {
    createTable(tableName: "machinery") {
      column(autoIncrement: "true", name: "id", type: "BIGINT") {
        constraints(primaryKey: "true", primaryKeyName: "machineryPK")
      }

      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "date_created", type: "datetime") {
        constraints(nullable: "false")
      }

      column(name: "initial_state_id", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "last_updated", type: "datetime") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "makingdevs (generated)", id: "1487186868383-3") {
    createTable(tableName: "machinery_link") {
      column(autoIncrement: "true", name: "id", type: "BIGINT") {
        constraints(primaryKey: "true", primaryKeyName: "machinery_linkPK")
      }

      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "date_created", type: "datetime") {
        constraints(nullable: "false")
      }

      column(name: "last_updated", type: "datetime") {
        constraints(nullable: "false")
      }

      column(name: "machinery_ref", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "type", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "makingdevs (generated)", id: "1487186868383-4") {
    createTable(tableName: "machinery_link_machinery") {
      column(name: "machinery_link_machineries_id", type: "BIGINT")

        column(name: "machinery_id", type: "BIGINT")
    }
  }

  changeSet(author: "makingdevs (generated)", id: "1487186868383-5") {
    createTable(tableName: "state") {
      column(autoIncrement: "true", name: "id", type: "BIGINT") {
        constraints(primaryKey: "true", primaryKeyName: "statePK")
      }

      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "date_created", type: "datetime") {
        constraints(nullable: "false")
      }

      column(name: "is_final_state", type: "BIT") {
        constraints(nullable: "false")
      }

      column(name: "last_updated", type: "datetime") {
        constraints(nullable: "false")
      }

      column(name: "machinery_id", type: "BIGINT") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "makingdevs (generated)", id: "1487186868383-6") {
    createTable(tableName: "transition") {
      column(autoIncrement: "true", name: "id", type: "BIGINT") {
        constraints(primaryKey: "true", primaryKeyName: "transitionPK")
      }

      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "action_id", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "date_created", type: "datetime") {
        constraints(nullable: "false")
      }

      column(name: "last_updated", type: "datetime") {
        constraints(nullable: "false")
      }

      column(name: "state_to_id", type: "BIGINT") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "makingdevs (generated)", id: "1487186868383-7") {
    addForeignKeyConstraint(baseColumnNames: "initial_state_id", baseTableName: "machinery", constraintName: "FK_1fyq80ollklggxteme85ucqhp", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "state")
  }

  changeSet(author: "makingdevs (generated)", id: "1487186868383-8") {
    addForeignKeyConstraint(baseColumnNames: "state_to_id", baseTableName: "transition", constraintName: "FK_1koe0yhvkysc02wsx7nx8f3y0", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "state")
  }

  changeSet(author: "makingdevs (generated)", id: "1487186868383-9") {
    addForeignKeyConstraint(baseColumnNames: "machinery_link_machineries_id", baseTableName: "machinery_link_machinery", constraintName: "FK_1xnw2f5kv6uvxmpp210ckqui5", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "machinery_link")
  }

  changeSet(author: "makingdevs (generated)", id: "1487186868383-10") {
    addForeignKeyConstraint(baseColumnNames: "machinery_id", baseTableName: "state", constraintName: "FK_4t88om7i1w22yn3s9q1cqcwq6", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "machinery")
  }

  changeSet(author: "makingdevs (generated)", id: "1487186868383-11") {
    addForeignKeyConstraint(baseColumnNames: "machinery_id", baseTableName: "machinery_link_machinery", constraintName: "FK_a36yghumnhv2l59vyov33ayks", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "machinery")
  }

  changeSet(author: "makingdevs (generated)", id: "1487186868383-12") {
    addForeignKeyConstraint(baseColumnNames: "action_id", baseTableName: "transition", constraintName: "FK_mmuwp8m7nxxn8ya5je65yhslp", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "action")
  }

}
