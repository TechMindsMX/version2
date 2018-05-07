import com.modulus.uno.saleorder.SaleOrder

databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1488922962037-1") {
        addColumn(tableName: "sale_order") {
            column(name: "change_type", type: "decimal(19, 2)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tim (generated)", id: "1488922962037-2") {
        addColumn(tableName: "sale_order") {
            column(name: "currency", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (manual)", id: "update-currency-to-existing-sale-orders") {
      grailsChange {
        change {
          SaleOrder.list().each() {
            it.currency = "MXN"
            it.save()
          }
        }
      }
    }

}
