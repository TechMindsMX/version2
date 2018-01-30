
databaseChangeLog = {

		changeSet(author: "brandon", id: "add-roles-quotation") {
			grailsChange {
				change {
          sql.execute("INSERT INTO role(version,authority) VALUES (0,'ROLE_OPERATOR_QUOTATION')")
          sql.execute("INSERT INTO role(version,authority) VALUES (0,'ROLE_EXECUTOR_QUOTATION')")
          sql.execute("INSERT INTO role(version,authority) VALUES (0,'ROLE_EMPLOYEE')")
				}
			}
		}

}
