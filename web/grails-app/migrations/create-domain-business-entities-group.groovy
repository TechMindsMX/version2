databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1532031528460-1") {
        createTable(tableName: "business_entities_group") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "business_entities_groupPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "company_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "description", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "type", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1532031528460-2") {
        createTable(tableName: "business_entities_group_business_entity") {
            column(name: "business_entities_group_business_entities_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "business_entity_id", type: "BIGINT")
        }
    }

    changeSet(author: "temoc (generated)", id: "1532031528460-3") {
        addForeignKeyConstraint(baseColumnNames: "business_entity_id", baseTableName: "business_entities_group_business_entity", constraintName: "FK8tvbhgeoohb0e6t7ttnsf14jc", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "business_entity")
    }

    changeSet(author: "temoc (generated)", id: "1532031528460-4") {
        addForeignKeyConstraint(baseColumnNames: "business_entities_group_business_entities_id", baseTableName: "business_entities_group_business_entity", constraintName: "FKmw8q5t8nkdjbg3qfcr6ufwv98", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "business_entities_group")
    }

    changeSet(author: "temoc (generated)", id: "1532031528460-5") {
        addForeignKeyConstraint(baseColumnNames: "company_id", baseTableName: "business_entities_group", constraintName: "FKskxhqw0veabdtftifvsxuvha5", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "company")
    }

}
