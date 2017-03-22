package com.modulus.uno.machine

import org.grails.core.DefaultGrailsDomainClass

trait Machinery{

  Map getNotificationData(){
    ArrayList<String> properties = new DefaultGrailsDomainClass(this.class).persistentProperties*.name
    Map notificationData = [:]

    properties.each{ property ->
      notificationData[property] = this[property]
    }
  
    notificationData 
  }

}
