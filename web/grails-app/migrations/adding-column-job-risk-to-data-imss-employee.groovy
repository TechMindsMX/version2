databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1522215982268-1") {
        addColumn(tableName: "data_imss_employee") {
            column(name: "job_risk", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

  changeSet(author: "temoc (manual)", id: "initializing-job-risk-into-data-imss-employee") {
    grailsChange {
      change {
        sql.execute("update data_imss_employee set job_risk='CLASS_01'")
      }
    }
  }

}
