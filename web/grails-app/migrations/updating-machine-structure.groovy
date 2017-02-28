databaseChangeLog = {

  changeSet(author: "makingdevs (generated)", id: "1488297563036-1") {
    createTable(tableName: "machine_transition") {
      column(name: "machine_transitions_id", type: "BIGINT")

        column(name: "transition_id", type: "BIGINT")
    }
  }

  changeSet(author: "makingdevs (generated)", id: "1488297563036-2") {
    createTable(tableName: "tracking_log") {
      column(autoIncrement: "true", name: "id", type: "BIGINT") {
        constraints(primaryKey: "true", primaryKeyName: "tracking_logPK")
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

      column(name: "machinery_link_id", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "state_id", type: "BIGINT") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "makingdevs (generated)", id: "1488297563036-3") {
    addColumn(tableName: "machinery_link") {
      column(name: "machinery_ref", type: "bigint") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "makingdevs (generated)", id: "1488297563036-4") {
    addColumn(tableName: "transition") {
      column(name: "state_from_id", type: "bigint") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "makingdevs (generated)", id: "1488297563036-5") {
    addForeignKeyConstraint(baseColumnNames: "transition_id", baseTableName: "machine_transition", constraintName: "FK_4gilg0ncwq427quffg4h6p5h3", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "transition")
  }

  changeSet(author: "makingdevs (generated)", id: "1488297563036-6") {
    addForeignKeyConstraint(baseColumnNames: "machinery_link_id", baseTableName: "tracking_log", constraintName: "FK_i4ybd4ois5uw2uo2f16hls4n5", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "machinery_link")
  }

  changeSet(author: "makingdevs (generated)", id: "1488297563036-7") {
    addForeignKeyConstraint(baseColumnNames: "state_id", baseTableName: "tracking_log", constraintName: "FK_ls87l50wof9a7mrjhcgqtwfhm", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "state")
  }

  changeSet(author: "makingdevs (generated)", id: "1488297563036-8") {
    addForeignKeyConstraint(baseColumnNames: "state_from_id", baseTableName: "transition", constraintName: "FK_lu4fu49pufeusyluwqfd9b3pr", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "state")
  }

  changeSet(author: "makingdevs (generated)", id: "1488297563036-9") {
    addForeignKeyConstraint(baseColumnNames: "machine_transitions_id", baseTableName: "machine_transition", constraintName: "FK_ssa3s68fmoiejgu2qt04i7xtl", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "machine")
  }

  changeSet(author: "makingdevs (generated)", id: "1488297563036-10") {
    dropForeignKeyConstraint(baseTableName: "transition", constraintName: "FK_5gxuh0fyamf2txaxrr76wdfyi")
  }

  changeSet(author: "makingdevs (generated)", id: "1488297563036-11") {
    dropForeignKeyConstraint(baseTableName: "log_record", constraintName: "FK_epvhqrdg99srwcq3si46re2ii")
  }

  changeSet(author: "makingdevs (generated)", id: "1488297563036-12") {
    dropForeignKeyConstraint(baseTableName: "action", constraintName: "FK_eyb39jmfkulkar6tr9n2j0rcm")
  }

  changeSet(author: "makingdevs (generated)", id: "1488297563036-13") {
    dropForeignKeyConstraint(baseTableName: "log_record", constraintName: "FK_ppne1f2e71fj5in1mtoad9206")
  }

  changeSet(author: "makingdevs (generated)", id: "1488297563036-14") {
    dropTable(tableName: "log_record")
  }

  changeSet(author: "makingdevs (generated)", id: "1488297563036-15") {
    dropTable(tableName: "tracking_log_link")
  }

  changeSet(author: "makingdevs (generated)", id: "1488297563036-16") {
    dropColumn(columnName: "company_id", tableName: "action")
  }

  changeSet(author: "makingdevs (generated)", id: "1488297563036-17") {
    dropColumn(columnName: "company_ref", tableName: "machinery_link")
  }

  changeSet(author: "makingdevs (generated)", id: "1488297563036-18") {
    dropColumn(columnName: "state_id", tableName: "transition")
  }

  changeSet(author: "makingdevs (generated)", id: "1488297563036-197") {
    dropNotNullConstraint(columnDataType: "bigint", columnName: "initial_state_id", tableName: "machine")
  }

}
