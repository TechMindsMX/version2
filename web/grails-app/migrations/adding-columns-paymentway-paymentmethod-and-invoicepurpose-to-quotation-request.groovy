databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1516404863711-1") {
        addColumn(tableName: "quotation_request") {
            column(name: "invoice_purpose", type: "varchar(255)") {
                constraints(nullable: "true")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1516404863711-3") {
        addColumn(tableName: "quotation_request") {
            column(name: "payment_method", type: "varchar(255)") {
                constraints(nullable: "true")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1516404863711-4") {
        addColumn(tableName: "quotation_request") {
            column(name: "payment_way", type: "varchar(255)") {
                constraints(nullable: "true")
            }
        }
    }

  changeSet(author: "temoc (manual)", id: "initializing-values-in-new-columns-for-existing-records-into-quotation-request") {
    grailsChange {
      change {
        sql.execute("update quotation_request set invoice_purpose='P01', payment_method='PUE', payment_way='TRANSFERENCIA_ELECTRONICA' where status='PROCESSED'")
      }
    }
  }

}
