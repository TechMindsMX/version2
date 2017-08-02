package com.modulus.uno.purchaseOrder

import com.modulus.uno.PurchaseOrder
import com.modulus.uno.PurchaseOrderItem

class PurchaseOrderItemService {

  def deleteItemFromPurchaseOrder(PurchaseOrderItem item) {
    PurchaseOrderItem.executeUpdate("delete PurchaseOrderItem item where item.id = :id", [id: item.id])
  }

  def deleteCurrentItemsFromPurchaseOrder(PurchaseOrder order) {
    order.items.each { item ->
      deleteItemFromPurchaseOrder(item)
    }
    order.items.removeAll()
    order.save()
    order
  }

}
