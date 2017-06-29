import liquibase.statement.core.*

databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1498750734226-3") {
        addColumn(tableName: "business_entity") {
            column(name: "status", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author:"tim (manual)", id:"initializing-status-for-all-business-entity") {
      grailsChange {
        change {
          sql.execute("update business_entity set status='ACTIVE'")
        }
      }
    }
}
