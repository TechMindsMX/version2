package com.modulus.uno

import com.modulus.uno.machine.MachineEventImplementer

class EventLoggerImplementerService implements MachineEventImplementer {

  void executeEvent(String className,Long instanceId){
    log.info("Updating the state of the instance with class ${className} and id ${instanceId}")
  }

}
