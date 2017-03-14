databaseChangeLog = {

  changeSet(author: "makingdevs (generated)", id: "1487714352840-1") {
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

  changeSet(author: "makingdevs (generated)", id: "1487714352840-2") {
    createTable(tableName: "log_record") {
      column(autoIncrement: "true", name: "id", type: "BIGINT") {
        constraints(primaryKey: "true", primaryKeyName: "log_recordPK")
      }

      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "current_state_id", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "date_created", type: "datetime") {
        constraints(nullable: "false")
      }

      column(name: "last_updated", type: "datetime") {
        constraints(nullable: "false")
      }

      column(name: "tracking_log_link_id", type: "BIGINT") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "makingdevs (generated)", id: "1487714352840-3") {
    createTable(tableName: "machine") {
      column(autoIncrement: "true", name: "id", type: "BIGINT") {
        constraints(primaryKey: "true", primaryKeyName: "machinePK")
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

  changeSet(author: "makingdevs (generated)", id: "1487714352840-4") {
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

      column(name: "machine_id", type: "BIGINT") {
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

  changeSet(author: "makingdevs (generated)", id: "1487714352840-5") {
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

      column(name: "final_state", type: "BIT") {
        constraints(nullable: "false")
      }

      column(name: "last_updated", type: "datetime") {
        constraints(nullable: "false")
      }

      column(name: "machine_id", type: "BIGINT") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "makingdevs (generated)", id: "1487714352840-6") {
    createTable(tableName: "tracking_log_link") {
      column(autoIncrement: "true", name: "id", type: "BIGINT") {
        constraints(primaryKey: "true", primaryKeyName: "tracking_log_linkPK")
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

      column(name: "tracking_ref", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "type", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "makingdevs (generated)", id: "1487714352840-7") {
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

      column(name: "state_id", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "state_to_id", type: "BIGINT") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "makingdevs (generated)", id: "1487714352840-8") {
    addForeignKeyConstraint(baseColumnNames: "state_to_id", baseTableName: "transition", constraintName: "FK_1koe0yhvkysc02wsx7nx8f3y0", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "state")
  }

  changeSet(author: "makingdevs (generated)", id: "1487714352840-9") {
    addForeignKeyConstraint(baseColumnNames: "state_id", baseTableName: "transition", constraintName: "FK_5gxuh0fyamf2txaxrr76wdfyi", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "state")
  }

  changeSet(author: "makingdevs (generated)", id: "1487714352840-10") {
    addForeignKeyConstraint(baseColumnNames: "tracking_log_link_id", baseTableName: "log_record", constraintName: "FK_epvhqrdg99srwcq3si46re2ii", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "tracking_log_link")
  }

  changeSet(author: "makingdevs (generated)", id: "1487714352840-11") {
    addForeignKeyConstraint(baseColumnNames: "initial_state_id", baseTableName: "machine", constraintName: "FK_gq016d8dojn6jcek1sg7gopd4", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "state")
  }

  changeSet(author: "makingdevs (generated)", id: "1487714352840-12") {
    addForeignKeyConstraint(baseColumnNames: "machine_id", baseTableName: "state", constraintName: "FK_jvls2o1bhaoy2wemcue85pwqv", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "machine")
  }

  changeSet(author: "makingdevs (generated)", id: "1487714352840-13") {
    addForeignKeyConstraint(baseColumnNames: "machine_id", baseTableName: "machinery_link", constraintName: "FK_l2aauyt0wvy7w1vyccexmlyg3", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "machine")
  }

  changeSet(author: "makingdevs (generated)", id: "1487714352840-14") {
    addForeignKeyConstraint(baseColumnNames: "action_id", baseTableName: "transition", constraintName: "FK_mmuwp8m7nxxn8ya5je65yhslp", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "action")
  }

  changeSet(author: "makingdevs (generated)", id: "1487714352840-15") {
    addForeignKeyConstraint(baseColumnNames: "current_state_id", baseTableName: "log_record", constraintName: "FK_ppne1f2e71fj5in1mtoad9206", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "state")
  }

}
