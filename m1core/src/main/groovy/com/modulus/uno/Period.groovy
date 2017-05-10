package com.modulus.uno

class Period {
  Date init
  Date end

  Boolean validate() {
    init && end && !init.after(end)
  }
}
