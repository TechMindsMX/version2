package com.modulus.uno.messages

import com.budjb.rabbitmq.publisher.RabbitMessagePublisher

class SenderQueueService {

  RabbitMessagePublisher rabbitMessagePublisher

  // This is hardcoded because external configuration is not working
  // if you know how to fix it, please tell me.
  // If you change the name of the queue change it in
  // com.modulus.uno.messages.MessagesConsumer class as well
  // Please check central configuration for grails-rabbitmq-native if you want to give it a try
  // http://budjb.github.io/grails-rabbitmq-native/3.x/latest/#_central_configuration
  def generateFacturaForSaleOrder(def saleOrderId) {
    rabbitMessagePublisher.send {
      routingKey = "factura.genera"
      body = [saleOrderId: saleOrderId]
    }
  }
}
