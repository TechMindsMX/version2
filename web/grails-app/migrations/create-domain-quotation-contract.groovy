databaseChangeLog = {

    changeSet(author: "makingdevs (generated)", id: "1509125484272-1") {
        createTable(tableName: "quotation_contract") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "quotation_contractPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "client_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "commision", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "company_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "init_date", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "status", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "makingdevs (generated)", id: "1509125484272-6") {
        addForeignKeyConstraint(baseColumnNames: "company_id", baseTableName: "quotation_contract", constraintName: "FKog32j8b702pnfkwtnbb55701d", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "company")
    }

    changeSet(author: "makingdevs (generated)", id: "1509125484272-7") {
        addForeignKeyConstraint(baseColumnNames: "client_id", baseTableName: "quotation_contract", constraintName: "FKrh6ihqbgbl055dkoitnlwqoyu", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "business_entity")
    }

}
