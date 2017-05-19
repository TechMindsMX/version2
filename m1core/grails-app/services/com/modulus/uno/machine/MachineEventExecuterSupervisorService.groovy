package com.modulus.uno.machine

import grails.transaction.Transactional

import groovyx.gpars.actor.DynamicDispatchActor

class MachineEventExecuterSupervisorService extends DynamicDispatchActor {

  def grailsApplication
  MachineEventExecuterActorService supervised

  void link(MachineEventExecuterActorService supervisedActor){
    this.supervised = supervisedActor
  }

  void onMessage(EventImplementer eventImplementer){
    supervised << eventImplementer
  }

  void onMessage(SupervisedException exception){
    log.error("The actor dies with exception ${exception.message}")
    this.supervised = new MachineEventExecuterActorService()
    this.supervised.machineEventExecuterSupervisorService = this
    this.supervised.grailsApplication = this.grailsApplication
    supervised.start()
  }

}
