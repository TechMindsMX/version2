databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1492610512462-1") {
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

            column(name: "folio_sat", type: "VARCHAR(255)")

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

    changeSet(author: "tim (generated)", id: "1492610512462-4") {
        addForeignKeyConstraint(baseColumnNames: "receiver_id", baseTableName: "commissions_invoice", constraintName: "FKkcn2gvcuxjcmajb63nm4d99m7", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "company")
    }

}
