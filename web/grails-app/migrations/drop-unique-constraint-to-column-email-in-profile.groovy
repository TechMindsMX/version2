databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1510716056378-11") {
        dropUniqueConstraint(constraintName: "UC_PROFILEEMAIL_COL", tableName: "profile")
    }

}
