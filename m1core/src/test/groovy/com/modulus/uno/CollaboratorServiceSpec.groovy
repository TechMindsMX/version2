package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification
import java.lang.Void as Should
import spock.lang.Unroll
import java.text.SimpleDateFormat
import java.util.Calendar
import static java.util.Calendar.*

@TestFor(CollaboratorService)
class CollaboratorServiceSpec extends Specification {

  def setup() {
    grailsApplication.config.stp.finalTransfer.hour = "17"
    grailsApplication.config.stp.finalTransfer.minute = "35"
    grailsApplication.config.stp.finalTransfer.second = "0"
  }

  void "Should get first day of the current month"(){
    given:
      def ini = Calendar.instance
      ini.set(date:1)
      String now = new SimpleDateFormat("dd-MM-yyyy").format(ini.time)
    when:
      String date = service.getBeginDateCurrentMonth()
    then:
      now == date
  }

  void "Should get last day of the current month"(){
    given:
      String expectDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(endCurrentMonth().time)
    when:
      String endDate = service.getEndDateCurrentMonth()
    then:
      expectDate == endDate
  }

  private Calendar endCurrentMonth() {
    def fin = Calendar.instance

    Boolean leapYear = false
    if ((fin[YEAR]%4==0) && ((fin[YEAR]%100!=0) || (fin[YEAR]%400==0)))
      leapYear = true

      switch (fin[MONTH]) {
        case [APRIL,JUNE,SEPTEMBER,NOVEMBER]:
        fin.set(date:30)
        break
        case FEBRUARY:
        if (leapYear)
          fin.set(date:29)
        else
          fin.set(date:28)
          break
        default:
        fin.set(date:31)
        break
      }

    fin.set([hourOfDay:23, minute:59, second:59])
    fin
  }

  @Unroll
  void "Should return #expectResult when beginDate is #beginDate and endDate is #endDate"(){
    when:
      Boolean result = service.periodIsValid(beginDate, endDate)
    then:
      expectResult == result
    where:
    beginDate | endDate | expectResult
    "01-01-16"  | "31-01-16"  | true
    "16-01-16"  | "01-01-16"  | false
    "01-01-16"  | "01-01-16"  | true
  }

  void "should get today period"() {
    given:"A format"
      SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss")
      SimpleDateFormat sdfDay = new SimpleDateFormat("dd-MM-yyyy")
    when:
      Period period = service.getPeriodForConciliationStp()
    then:
      sdfTime.format(period.init) == "00:00:00"
      sdfTime.format(period.end) == "17:35:00"
      sdfDay.format(period.init) == sdfDay.format(period.end)
  }

  void "should get current month period"() {
    when:
      Period period = service.getCurrentMonthPeriod()
    then:
      period.init.format("MM-yyyy") == period.end.format("MM-yyyy")
      period.init.format("dd") == "01"
      period.end.format("dd") == endCurrentMonth().time.format("dd")
  }

}
