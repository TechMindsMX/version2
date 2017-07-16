package com.modulus.uno


class MaintainConnectionJob {

  def maintainConnectionService

  static triggers = {
    cron name:'maintainingConnection', cronExpression: '0 0 0-23 ? JAN-DEC SUN-SAT'
  }

  def group = "ModulusUno"
  def description = "Executing query to maintain connection to db"

  def execute() {
    maintainConnectionService.executeQueryForMaintainConnection()
  }
}
