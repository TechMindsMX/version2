databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1506375210852-1") {
        addColumn(tableName: "payment_to_purchase") {
            column(name: "source", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

  changeSet(author: "temoc (manual)", id: "updating-source-into-payment-to-purchase") {
		grailsChange {
	    change {
        sql.execute("update payment_to_purchase set source='MODULUS_UNO'")
      }
		}
	}

}
