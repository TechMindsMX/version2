package com.modulus.uno

class HomeController {

  def managerApplicationService

  def index() {
    [privacyNotice:managerApplicationService.getPrivacyNotice()]
  }

}
