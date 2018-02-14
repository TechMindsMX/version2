databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1518564437331-22") {
        dropUniqueConstraint(constraintName: "UC_COMPANYRFC_COL", tableName: "company")
    }

}
