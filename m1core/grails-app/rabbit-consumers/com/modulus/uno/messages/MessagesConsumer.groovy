package com.modulus.uno.messages

import com.budjb.rabbitmq.consumer.MessageContext
import com.modulus.uno.saleorder.SaleOrderService

class MessagesConsumer {

  SaleOrderService saleOrderService

  static rabbitConfig = [
    queue: "modulus.uno"
  ]

  /**
   * Handle an incoming RabbitMQ message.
   *
   * @param body    The converted body of the incoming message.
   * @param context Properties of the incoming message.
   * @return
   */
  def handleMessage(Map body, MessageContext messageContext) {
    saleOrderService.generateInvoiceFromSaleOrderId(body.saleOrderId)
  }
}
