databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1519316974557-1") {
        createTable(tableName: "dispersion_result_file_detail") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "dispersion_result_file_detailPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "account", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "dispersion_result_file_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "result_message", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "status", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1519316974557-3") {
        addForeignKeyConstraint(baseColumnNames: "dispersion_result_file_id", baseTableName: "dispersion_result_file_detail", constraintName: "FKnfl1p8ynamo4i18gyq2pexqbv", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "dispersion_result_file")
    }

}
