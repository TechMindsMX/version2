databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1520276262962-1") {
        addColumn(tableName: "paysheet_project") {
            column(name: "federal_entity", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

  changeSet(author: "temoc (manual)", id: "updating-federal-entity-for-existing-paysheet-projects") {
    grailsChange {
      change {
        sql.execute("update paysheet_project set federal_entity='DIF'")
      }
    }
  }

}
