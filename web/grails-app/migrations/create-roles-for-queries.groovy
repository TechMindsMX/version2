databaseChangeLog = {

  changeSet(author: "temoc (manual)", id: "new-roles-for-paysheets") {
    grailsChange {
      change {
        ["ROLE_PAYROLL_OPERATOR",
         "ROLE_PAYROLL_OFFICER",
         "ROLE_BUSINESS_REPORT"].each { roleName ->
           sql.execute("INSERT INTO role(version,authority) VALUES (0,${roleName})")
         }
      }
    }
  }
}
