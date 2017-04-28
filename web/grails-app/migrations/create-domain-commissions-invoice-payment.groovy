databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1493147513830-1") {
        createTable(tableName: "commissions_invoice_payment") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "commissions_invoice_paymentPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "amount", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "invoice_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "datetime") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tim (generated)", id: "1493147513830-2") {
        addForeignKeyConstraint(baseColumnNames: "invoice_id", baseTableName: "commissions_invoice_payment", constraintName: "FKj4xktc5wlhtu2bq4c88nklrnc", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "commissions_invoice")
    }

}
