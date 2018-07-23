databaseChangeLog = {

  changeSet(author: "temoc (manual)", id: "updating-contract-type-indefined-to-undefined-into-data-imss-employee") {
    grailsChange {
      change {
        sql.execute("update data_imss_employee set contract_type='UNDEFINED' where contract_type='INDEFINED'")
      }
    }
  }

}
