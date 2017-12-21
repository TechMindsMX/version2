databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1510591622350-3") {
        dropNotNullConstraint(columnDataType: "varchar(11)", columnName: "account_number", tableName: "bank_account")
    }

    changeSet(author: "temoc (generated)", id: "1510591622350-4") {
        dropNotNullConstraint(columnDataType: "varchar(18)", columnName: "clabe", tableName: "bank_account")
    }

}
