databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1517512996757-1") {
        createTable(tableName: "dispersion_result_file") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "dispersion_result_filePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "bank_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "file_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "paysheet_id", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1517512996757-3") {
        addForeignKeyConstraint(baseColumnNames: "bank_id", baseTableName: "dispersion_result_file", constraintName: "FK14kmja1t1s45k9uu2umk8v64p", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "bank")
    }

    changeSet(author: "temoc (generated)", id: "1517512996757-4") {
        addForeignKeyConstraint(baseColumnNames: "paysheet_id", baseTableName: "dispersion_result_file", constraintName: "FKbqxwoxc4re2egv33jmswk8c2a", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "paysheet")
    }

    changeSet(author: "temoc (generated)", id: "1517512996757-5") {
        addForeignKeyConstraint(baseColumnNames: "file_id", baseTableName: "dispersion_result_file", constraintName: "FKilj4rwrq9ecviu99ifqqbydj7", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "s3asset")
    }
}
