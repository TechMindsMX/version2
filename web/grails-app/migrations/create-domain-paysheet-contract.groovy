databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1508513374321-1") {
        createTable(tableName: "paysheet_contract") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "paysheet_contractPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "client_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "company_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "date_updated", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "executive_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "init_date", type: "datetime") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1508513374321-2") {
        createTable(tableName: "paysheet_contract_business_entity") {
            column(name: "paysheet_contract_employees_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "business_entity_id", type: "BIGINT")
        }
    }

    changeSet(author: "temoc (generated)", id: "1508513374321-3") {
        createTable(tableName: "paysheet_contract_paysheet_project") {
            column(name: "paysheet_contract_projects_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "paysheet_project_id", type: "BIGINT")
        }
    }

    changeSet(author: "temoc (generated)", id: "1508513374321-4") {
        addForeignKeyConstraint(baseColumnNames: "paysheet_contract_projects_id", baseTableName: "paysheet_contract_paysheet_project", constraintName: "FK2jx9urq7s72q80bqb16cod497", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "paysheet_contract")
    }

    changeSet(author: "temoc (generated)", id: "1508513374321-5") {
        addForeignKeyConstraint(baseColumnNames: "business_entity_id", baseTableName: "paysheet_contract_business_entity", constraintName: "FK4mqqus4ly0ieh6in5nebrsyvu", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "business_entity")
    }

    changeSet(author: "temoc (generated)", id: "1508513374321-6") {
        addForeignKeyConstraint(baseColumnNames: "paysheet_contract_employees_id", baseTableName: "paysheet_contract_business_entity", constraintName: "FK58cuunccicytpi835bvbd913d", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "paysheet_contract")
    }

    changeSet(author: "temoc (generated)", id: "1508513374321-7") {
        addForeignKeyConstraint(baseColumnNames: "paysheet_project_id", baseTableName: "paysheet_contract_paysheet_project", constraintName: "FK86ve27sbfvsosrg5ykk3crw5k", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "paysheet_project")
    }

    changeSet(author: "temoc (generated)", id: "1508513374321-8") {
        addForeignKeyConstraint(baseColumnNames: "executive_id", baseTableName: "paysheet_contract", constraintName: "FKdnl175mfegw0nx31so73912rd", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user")
    }

    changeSet(author: "temoc (generated)", id: "1508513374321-9") {
        addForeignKeyConstraint(baseColumnNames: "client_id", baseTableName: "paysheet_contract", constraintName: "FKfjw7u1lwtefurxcnrefp7qcj8", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "business_entity")
    }

    changeSet(author: "temoc (generated)", id: "1508513374321-10") {
        addForeignKeyConstraint(baseColumnNames: "company_id", baseTableName: "paysheet_contract", constraintName: "FKn73syqewbijqljdxgq7lgg438", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "company")
    }

}
