databaseChangeLog = {

    changeSet(author: "makingdevs (generated)", id: "1529514934518-1") {
        addColumn(tableName: "sale_order") {
            column(name: "stamped_date", type: "datetime")
        }
    }

}
