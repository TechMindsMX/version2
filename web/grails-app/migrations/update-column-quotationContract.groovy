databaseChangeLog = {

    changeSet(author: "iMacLeo (generated)", id: "1509477976264-1") {
        addColumn(tableName: "quotation_contract") {
            column(name: "commission", type: "decimal(4, 2)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "iMacLeo (generated)", id: "1509478900186-14") {
      dropColumn(columnName: "commision", tableName: "quotation_contract")
    }

}
