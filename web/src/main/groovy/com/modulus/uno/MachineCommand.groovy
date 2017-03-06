package com.modulus.uno

import grails.validation.Validateable

class MachineCommand {
  String initialState
  ArrayList<TransitionCommand> transitions;
}
