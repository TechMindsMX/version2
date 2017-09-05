databaseChangeLog = {

  changeSet(author: "temoc (manual)", id: "new-roles-for-paysheets") {
    grailsChange {
      change {
        ["ROLE_AUTHORIZER_PAYSHEET",
         "ROLE_OPERATOR_PAYSHEET"].each { roleName ->
           sql.execute("INSERT INTO role(version,authority) VALUES (0,${roleName})")
         }
      }
    }
  }

}
