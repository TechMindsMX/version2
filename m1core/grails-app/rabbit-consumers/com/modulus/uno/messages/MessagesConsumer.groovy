package com.modulus.uno.messages

import com.budjb.rabbitmq.consumer.MessageContext
import com.modulus.uno.saleorder.SaleOrderService

class MessagesConsumer {

  SaleOrderService saleOrderService

  // This is hardcoded because external configuration is not working
  // if you know how to fix it, please tell me.
  // If you change the name of the queue change it in
  // com.modulus.uno.messages.SenderQueueService class as well
  // Please check central configuration for grails-rabbitmq-native if you want to give it a try
  // http://budjb.github.io/grails-rabbitmq-native/3.x/latest/#_central_configuration
  static rabbitConfig = [
    queue: "factura.genera"
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
