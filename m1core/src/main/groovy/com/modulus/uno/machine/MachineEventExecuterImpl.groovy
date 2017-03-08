package com.modulus.uno.machine

class MachineEventExecuterImpl implements MachineEventExecuter {
  
  ArrayList<MachineEvent> events = [];

  void executeEvent(){
    events.each{ event ->
      event.execute()
    }          
  }

}
