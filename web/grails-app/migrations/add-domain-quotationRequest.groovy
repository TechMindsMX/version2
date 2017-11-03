databaseChangeLog = {

    changeSet(author: "makingdevs (generated)", id: "1509572882760-1") {
        createTable(tableName: "quotation_request") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "quotation_requestPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "amount", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "biller_id", type: "BIGINT")

            column(name: "commission", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "description", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "quotation_contract_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "sale_order_id", type: "BIGINT")

            column(name: "sat_concept", type: "VARCHAR(255)")

            column(name: "status", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "makingdevs (generated)", id: "1509572882760-2") {
        addForeignKeyConstraint(baseColumnNames: "sale_order_id", baseTableName: "quotation_request", constraintName: "FK28t6rj591byahd19vsm07icwn", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "sale_order")
    }

    changeSet(author: "makingdevs (generated)", id: "1509572882760-3") {
        addForeignKeyConstraint(baseColumnNames: "biller_id", baseTableName: "quotation_request", constraintName: "FK4afbv9hqe0r2nuv7fniidbfwp", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "company")
    }

    changeSet(author: "makingdevs (generated)", id: "1509572882760-4") {
        addForeignKeyConstraint(baseColumnNames: "quotation_contract_id", baseTableName: "quotation_request", constraintName: "FK5g00gloq4ntap0ocpyy7q5p8c", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "quotation_contract")
    }
}
