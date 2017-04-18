databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1492528639030-1") {
        createTable(tableName: "commissions_invoice") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "commissions_invoicePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "receiver_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "status", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tim (generated)", id: "1492528639030-2") {
        createTable(tableName: "commissions_invoice_commission_transaction") {
            column(name: "commissions_invoice_commissions_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "commission_transaction_id", type: "BIGINT")
        }
    }

    changeSet(author: "tim (generated)", id: "1492528639030-3") {
        addForeignKeyConstraint(baseColumnNames: "commission_transaction_id", baseTableName: "commissions_invoice_commission_transaction", constraintName: "FK7mbcl98y2x83fwsc1g019trf", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "commission_transaction")
    }

    changeSet(author: "tim (generated)", id: "1492528639030-4") {
        addForeignKeyConstraint(baseColumnNames: "commissions_invoice_commissions_id", baseTableName: "commissions_invoice_commission_transaction", constraintName: "FK8nsk3hvv2nl479hjl1gwtnblh", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "commissions_invoice")
    }

    changeSet(author: "tim (generated)", id: "1492528639030-5") {
        addForeignKeyConstraint(baseColumnNames: "receiver_id", baseTableName: "commissions_invoice", constraintName: "FKkcn2gvcuxjcmajb63nm4d99m7", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "company")
    }

}
