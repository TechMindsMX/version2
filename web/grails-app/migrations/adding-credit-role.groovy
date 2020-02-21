databaseChangeLog = {

    changeSet(author: "karla", id: "add-credit-role") {
			grailsChange {
				change {
          sql.execute("INSERT INTO role(version,authority) VALUES (0,'ROLE_CREDIT')")
				}
			}
		}
}
