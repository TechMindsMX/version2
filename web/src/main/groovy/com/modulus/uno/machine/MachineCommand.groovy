package com.modulus.uno.machine

import grails.validation.Validateable

@Validateable
class MachineCommand {
  String initialState
  ArrayList<TransitionCommand> transitions;
}
