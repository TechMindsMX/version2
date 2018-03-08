databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1519311923809-1") {
        addColumn(tableName: "dispersion_result_file") {
            column(name: "payment_schema", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

}
