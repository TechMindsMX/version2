databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1519747695925-6") {
        dropUniqueConstraint(constraintName: "UC_DATAIMSSEMPLOYEE_NSS", tableName: "data_imss_employee")
    }

}
