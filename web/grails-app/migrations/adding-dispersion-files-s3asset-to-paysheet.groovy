databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1506215804823-1") {
        createTable(tableName: "paysheet_s3asset") {
            column(name: "paysheet_dispersion_files_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "s3asset_id", type: "BIGINT")
        }
    }

    changeSet(author: "temoc (generated)", id: "1506215804823-2") {
        addForeignKeyConstraint(baseColumnNames: "paysheet_dispersion_files_id", baseTableName: "paysheet_s3asset", constraintName: "FK6nwyju15lqbt8p70wv8ikg0xv", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "paysheet")
    }

    changeSet(author: "temoc (generated)", id: "1506215804823-3") {
        addForeignKeyConstraint(baseColumnNames: "s3asset_id", baseTableName: "paysheet_s3asset", constraintName: "FKofhsvukbpkeh38j4c53gbtpg", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "s3asset")
    }

}
