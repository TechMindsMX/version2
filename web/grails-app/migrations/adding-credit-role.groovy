databaseChangeLog = {

    changeSet(author: "karlaju (generated)", id: "1582242414259-1") {
        dropColumn(columnName: "credit_advance", tableName: "corporate")
    }

    changeSet(author: "karla", id: "add-credit-role") {
			grailsChange {
				change {
          sql.execute("INSERT INTO role(version,authority) VALUES (0,'ROLE_CREDIT')")
				}
			}
		}
}
