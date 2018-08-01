databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1533139635005-1") {
        createTable(tableName: "user_business_entities_group") {
            column(name: "user_business_entities_groups_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "business_entities_group_id", type: "BIGINT")
        }
    }

    changeSet(author: "temoc (generated)", id: "1533139635005-2") {
        addForeignKeyConstraint(baseColumnNames: "business_entities_group_id", baseTableName: "user_business_entities_group", constraintName: "FKef1tr7vr37uorrta6uhnfru9m", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "business_entities_group")
    }

    changeSet(author: "temoc (generated)", id: "1533139635005-3") {
        addForeignKeyConstraint(baseColumnNames: "user_business_entities_groups_id", baseTableName: "user_business_entities_group", constraintName: "FKf4fxps1i71pb02dw409aihirw", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user")
    }

}
