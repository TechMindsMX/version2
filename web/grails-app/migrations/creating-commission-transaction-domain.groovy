databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1491491937363-1") {
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

            column(name: "sale_order_of_charge_id", type: "BIGINT")

            column(name: "status", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "transaction_id", type: "BIGINT")

            column(name: "type", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "uuid", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tim (generated)", id: "1491491937363-3") {
        addForeignKeyConstraint(baseColumnNames: "company_id", baseTableName: "commission_transaction", constraintName: "FKc6nha0tw4yoftm8xh75qryejm", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "company")
    }

    changeSet(author: "tim (generated)", id: "1491491937363-4") {
        addForeignKeyConstraint(baseColumnNames: "sale_order_of_charge_id", baseTableName: "commission_transaction", constraintName: "FKqypmg7qp3bwqkkv6vi85k1keq", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "sale_order")
    }

    changeSet(author: "tim (generated)", id: "1491491937363-6") {
        addForeignKeyConstraint(baseColumnNames: "transaction_id", baseTableName: "commission_transaction", constraintName: "FKt10bsp11eb79fdha8i8qaol7y", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "transaction")
    }

}
