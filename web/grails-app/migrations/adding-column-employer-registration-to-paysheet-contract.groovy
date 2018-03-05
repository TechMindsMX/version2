databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1520267162482-1") {
        addColumn(tableName: "paysheet_contract") {
            column(name: "employer_registration", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1520267162482-2") {
        dropForeignKeyConstraint(baseTableName: "dispersion_result_file", constraintName: "FK14kmja1t1s45k9uu2umk8v64p")
    }

    changeSet(author: "temoc (generated)", id: "1520267162482-3") {
        dropForeignKeyConstraint(baseTableName: "dispersion_result_file", constraintName: "FKbqxwoxc4re2egv33jmswk8c2a")
    }

    changeSet(author: "temoc (generated)", id: "1520267162482-4") {
        dropForeignKeyConstraint(baseTableName: "dispersion_result_file", constraintName: "FKilj4rwrq9ecviu99ifqqbydj7")
    }

    changeSet(author: "temoc (generated)", id: "1520267162482-5") {
        dropForeignKeyConstraint(baseTableName: "dispersion_result_file_detail", constraintName: "FKnfl1p8ynamo4i18gyq2pexqbv")
    }

    changeSet(author: "temoc (generated)", id: "1520267162482-6") {
        dropTable(tableName: "dispersion_result_file")
    }

    changeSet(author: "temoc (generated)", id: "1520267162482-7") {
        dropTable(tableName: "dispersion_result_file_detail")
    }
}
