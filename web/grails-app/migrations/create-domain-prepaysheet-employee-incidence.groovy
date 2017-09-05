databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1503104515946-1") {
        createTable(tableName: "pre_paysheet_employee_incidence") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "pre_paysheet_employee_incidencePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "amount", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "description", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "payment_schema", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "pre_paysheet_employee_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "type", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tim (generated)", id: "1503104515946-2") {
        addForeignKeyConstraint(baseColumnNames: "pre_paysheet_employee_id", baseTableName: "pre_paysheet_employee_incidence", constraintName: "FKlv85f7tfqkgnd934y8h5xh8bf", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "pre_paysheet_employee")
    }

}
