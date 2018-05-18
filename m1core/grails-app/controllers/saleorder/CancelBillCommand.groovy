package com.modulus.uno.saleorder

import com.modulus.uno.MessageCommand

class CancelBillCommand implements MessageCommand {
  String uuid
  String rfc
  String id
}
