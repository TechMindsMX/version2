databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1514486162088-13") {
        dropUniqueConstraint(constraintName: "UC_COMPANYRFC_COL", tableName: "company")
    }

}
