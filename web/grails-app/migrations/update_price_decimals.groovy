databaseChangeLog = {
  changeSet(author: "sohjiro (generated)", id: "update_price_decimals") {
    modifyDataType(tableName: "product", columnName: "price", newDataType: "decimal(11,4)")
    modifyDataType(tableName: "purchase_order_item", columnName: "price", newDataType: "decimal(11,4)")
    modifyDataType(tableName: "sale_order_item", columnName: "price", newDataType: "decimal(11,4)")
    modifyDataType(tableName: "credit_note_item", columnName: "price", newDataType: "decimal(11,4)")
  }
}
