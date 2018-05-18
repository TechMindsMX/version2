databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1521744870623-1") {
        createTable(tableName: "extra_hour_incidence") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "extra_hour_incidencePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "amount", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "days", type: "INT") {
                constraints(nullable: "false")
            }

            column(name: "quantity", type: "INT") {
                constraints(nullable: "false")
            }

            column(name: "type", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1521744870623-2") {
        addColumn(tableName: "pre_paysheet_employee_incidence") {
            column(name: "extra_hour_incidence_id", type: "bigint")
        }
    }

    changeSet(author: "temoc (generated)", id: "1521744870623-3") {
        addForeignKeyConstraint(baseColumnNames: "extra_hour_incidence_id", baseTableName: "pre_paysheet_employee_incidence", constraintName: "FKolua0h6tqiy15hwd54pm2l3j5", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "extra_hour_incidence")
    }
}
