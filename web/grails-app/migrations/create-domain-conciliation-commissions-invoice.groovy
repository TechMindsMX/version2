databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1493133007598-1") {
        createTable(tableName: "conciliation_commissions_invoice") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "conciliation_commissions_invoicePK")
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

            column(name: "payment_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "status", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tim (generated)", id: "1493133007598-2") {
        addForeignKeyConstraint(baseColumnNames: "invoice_id", baseTableName: "conciliation_commissions_invoice", constraintName: "FKeb2h1efutfyjbx53bus7gneu4", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "commissions_invoice")
    }

    changeSet(author: "tim (generated)", id: "1493133007598-3") {
        addForeignKeyConstraint(baseColumnNames: "payment_id", baseTableName: "conciliation_commissions_invoice", constraintName: "FKnrxpxyue0mb1etgc1vc5g8mr2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "paymentm1emitter")
    }

}
