package com.modulus.uno.machine

import grails.transaction.Transactional
import groovyx.gpars.actor.DynamicDispatchActor

class MachineEventExecuterSupervisorService extends DynamicDispatchActor {

  SupervisedActor supervised

  void link(SupervisedActor supervisedActor){
    this.supervised = supervisedActor
  }

  void onMessage(EventImplementer eventImplementer){
    supervised << eventImplementer
  }

  void onMessage(SupervisedException exception){
    supervised.start()
  }

}
