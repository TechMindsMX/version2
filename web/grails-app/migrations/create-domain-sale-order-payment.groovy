databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1489776853825-1") {
        createTable(tableName: "sale_order_payment") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "sale_order_paymentPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "amount", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "conciliation_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "sale_order_id", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tim (generated)", id: "1489776853825-3") {
        addForeignKeyConstraint(baseColumnNames: "conciliation_id", baseTableName: "sale_order_payment", constraintName: "FKq4dgsc1nkxnuc8q2sm7u2gies", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "conciliation")
    }

    changeSet(author: "tim (generated)", id: "1489776853825-5") {
        addForeignKeyConstraint(baseColumnNames: "sale_order_id", baseTableName: "sale_order_payment", constraintName: "FKt17io5eu2eh38qbacep8cp8gb", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "sale_order")
    }

}
