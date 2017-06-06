package com.modulus.uno


class MaintainConnectionJob {

  def maintainConnectionService

  static triggers = {
    cron name:'maintainingConnection', cronExpression: '0 5,10,15,20,25,30,35 0-23 ? JAN-DEC SUN-SAT'
  }

  def group = "ModulusUno"
  def description = "Executing query to maintain connection to db"

  def execute() {
    maintainConnectionService.executeQueryForMaintainConnection()
  }
}
