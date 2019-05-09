databaseChangeLog = {
  changeSet(author: "karla (manual)", id: "adding-column-replacement-invoice-to-sale-order") {
    addColumn(tableName: "sale_order"){
      column(name: "uuid_replacement", type: "varchar(25)")
    }
  }
}