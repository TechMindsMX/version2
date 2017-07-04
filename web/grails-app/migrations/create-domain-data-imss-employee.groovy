databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1498841597589-1") {
        createTable(tableName: "data_imss_employee") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "data_imss_employeePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "annual_bonus_days", type: "INT") {
                constraints(nullable: "false")
            }

            column(name: "base_imss_monthly_salary", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "employee_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "holiday_bonus_rate", type: "DECIMAL(5, 2)") {
                constraints(nullable: "false")
            }

            column(name: "net_monthly_salary", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "payment_period", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "registration_date", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "nss", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tim (generated)", id: "1498841597589-4") {
        addForeignKeyConstraint(baseColumnNames: "employee_id", baseTableName: "data_imss_employee", constraintName: "FKpdxcdux5fcfcek3bc12uh0869", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "employee_link")
    }

}
