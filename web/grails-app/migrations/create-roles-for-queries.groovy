databaseChangeLog = {

  changeSet(author: "neodevelop (manual)", id: "new-roles-for-queries") {
    grailsChange {
      change {
        ["ROLE_BUSINESS_REPORT"].each { roleName ->
           sql.execute("INSERT INTO role(version,authority) VALUES (0,${roleName})")
         }
      }
    }
  }
}
