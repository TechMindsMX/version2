databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1520273851870-1") {
        addColumn(tableName: "data_imss_employee") {
            column(name: "contract_type", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1520273851870-2") {
        addColumn(tableName: "data_imss_employee") {
            column(name: "department", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1520273851870-3") {
        addColumn(tableName: "data_imss_employee") {
            column(name: "job", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1520273851870-4") {
        addColumn(tableName: "data_imss_employee") {
            column(name: "regime_type", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1520273851870-5") {
        addColumn(tableName: "data_imss_employee") {
            column(name: "work_day_type", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

  changeSet(author: "temoc (manual)", id: "updating-contract-type-department-job-regime-type-and-work-day-type-for-existing-data-imss-employee") {
    grailsChange {
      change {
        sql.execute("update data_imss_employee set contract_type='INDEFINED', department='ADMINISTRACION', job='AUXILIAR ADMINISTRATIVO', regime_type='SALARIES', work_day_type='DIURNAL'")
      }
    }
  }

}
