databaseChangeLog = {

    changeSet(author: "iMacLeo (generated)", id: "1511542438780-1") {
        createTable(tableName: "quotation_commission") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "quotation_commissionPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "amount", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "commission_apply", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "quotation_request_id", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "iMacLeo (generated)", id: "1511542438780-5") {
        addForeignKeyConstraint(baseColumnNames: "quotation_request_id", baseTableName: "quotation_commission", constraintName: "FKklberf8ejd0ex1k3ua88c25v4", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "quotation_request")
    }

 
}
