databaseChangeLog = {

    changeSet(author: "brandonVergara (generated)", id: "1511664320886-1") {
        createTable(tableName: "quotation_contract_user") {
            column(name: "quotation_contract_users_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "user_id", type: "BIGINT")
        }
    }

    changeSet(author: "brandonVergara (generated)", id: "1511664320886-5") {
        addForeignKeyConstraint(baseColumnNames: "quotation_contract_users_id", baseTableName: "quotation_contract_user", constraintName: "FKc10a9nukec23dak2c5i2e5tw6", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "quotation_contract")
    }

    changeSet(author: "brandonVergara (generated)", id: "1511664320886-6") {
        addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "quotation_contract_user", constraintName: "FKd5ri6cubdo951mrulawyupx74", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user")
    }

}
