databaseChangeLog = {

  changeSet(author: "temoc (manual)", id: "increase-size-column-name-into-sale-order-item") {
    grailsChange {
      change {
        sql.execute("alter table sale_order_item modify name varchar(500)")
      }
    }
  }

  changeSet(author: "temoc (manual)", id: "increase-size-column-name-into-product") {
    grailsChange {
      change {
        sql.execute("alter table product modify name varchar(500)")
      }
    }
  }

}
