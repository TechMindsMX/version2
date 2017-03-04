databaseChangeLog = {

  changeSet(author: "makingdevs (generated)", id: "1488585277445-1") {
    createTable(tableName: "transition_actions") {
      column(name: "transition_id", type: "BIGINT")
        column(name: "actions_string", type: "VARCHAR(255)")
    }
  }

  changeSet(author: "makingdevs (generated)", id: "1488585277445-2") {
    addColumn(tableName: "transition") {
      column(name: "machine_id", type: "bigint") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "makingdevs (generated)", id: "1488585277445-3") {
    addColumn(tableName: "state") {
      column(name: "name", type: "varchar(255)") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "makingdevs (generated)", id: "1488585277445-4") {
    addForeignKeyConstraint(baseColumnNames: "transition_id", baseTableName: "transition_actions", constraintName: "FK_84nq6ol4yhjifmv7h1e30khvu", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "transition")
  }

  changeSet(author: "makingdevs (generated)", id: "1488585277445-5") {
    addForeignKeyConstraint(baseColumnNames: "machine_id", baseTableName: "transition", constraintName: "FK_8dr9xy5e7trl0u6mk6bfw586g", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "machine")
  }

  changeSet(author: "makingdevs (generated)", id: "1488585277445-6") {
    dropForeignKeyConstraint(baseTableName: "machine_transition", constraintName: "FK_4gilg0ncwq427quffg4h6p5h3")
  }

  changeSet(author: "makingdevs (generated)", id: "1488585277445-7") {
    dropForeignKeyConstraint(baseTableName: "transition", constraintName: "FK_mmuwp8m7nxxn8ya5je65yhslp")
  }

  changeSet(author: "makingdevs (generated)", id: "1488585277445-8") {
    dropForeignKeyConstraint(baseTableName: "machine_transition", constraintName: "FK_ssa3s68fmoiejgu2qt04i7xtl")
  }

  changeSet(author: "makingdevs (generated)", id: "1488585277445-9") {
    dropTable(tableName: "action")
  }

  changeSet(author: "makingdevs (generated)", id: "1488585277445-10") {
    dropTable(tableName: "machine_transition")
  }

  changeSet(author: "makingdevs (generated)", id: "1488585277445-11") {
    dropColumn(columnName: "action_id", tableName: "transition")
  }

}
