import com.modulus.uno.FeesReceipt

databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1490732070064-1") {
        addColumn(tableName: "fees_receipt") {
            column(name: "date_created", type: "datetime")
        }
    }

    changeSet(author: "tim (generated)", id: "1490732070064-2") {
        addColumn(tableName: "fees_receipt") {
            column(name: "last_updated", type: "datetime")
        }
    }

  changeSet(author: "temoc (manual)", id: "initializing-dates-for-existing-fees-receipts") {
    grailsChange {
      change {
        FeesReceipt.list().each {
          it.dateCreated = new Date()
          it.lastUpdated = new Date()
          it.save()
        }
      }
    }
  }

}
