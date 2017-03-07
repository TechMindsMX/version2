package com.modulus.uno.machine

import grails.validation.Validateable

class MachineCommand implements Validateable {
  String initialState
  ArrayList<TransitionCommand> transitions;
}
