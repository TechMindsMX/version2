databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1525802688658-1") {
        createTable(tableName: "credit_note") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "credit_notePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "folio", type: "VARCHAR(255)")

            column(name: "invoice_purpose", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "payment_method", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "payment_way", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "sale_order_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "status", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1525802688658-2") {
        addForeignKeyConstraint(baseColumnNames: "sale_order_id", baseTableName: "credit_note", constraintName: "FK74j4pv5q72uwsc6lua0g46ist", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "sale_order")
    }
}
