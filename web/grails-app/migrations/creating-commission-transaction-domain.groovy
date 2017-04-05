databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1491430769354-1") {
        createTable(tableName: "commission_transaction") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "commission_transactionPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "amount", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "company_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "sale_order_of_charge", type: "VARCHAR(255)")

            column(name: "status", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "type", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "uuid", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tim (generated)", id: "1491430769354-3") {
        addForeignKeyConstraint(baseColumnNames: "company_id", baseTableName: "commission_transaction", constraintName: "FKc6nha0tw4yoftm8xh75qryejm", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "company")
    }

}
