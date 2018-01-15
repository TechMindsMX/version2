databaseChangeLog = {

    changeSet(author: "iMacLeo (generated)", id: "1513883824035-5") {
        addColumn(tableName: "corporate") {
            column(name: "has_quotation_contract", type: "bit") {
                constraints(nullable: "false")
            }
        }
    }

}
