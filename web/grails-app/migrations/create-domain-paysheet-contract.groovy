databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1508510045664-1") {
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

    changeSet(author: "temoc (generated)", id: "1508510045664-2") {
        createTable(tableName: "paysheet_contract_business_entity") {
            column(name: "paysheet_contract_employees_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "business_entity_id", type: "BIGINT")
        }
    }

    changeSet(author: "temoc (generated)", id: "1508510045664-3") {
        createTable(tableName: "paysheet_contract_paysheet_project") {
            column(name: "paysheet_contract_projects_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "paysheet_project_id", type: "BIGINT")
        }
    }

}
