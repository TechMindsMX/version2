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

      String expectDate = new SimpleDateFormat("dd-MM-yyyy").format(fin.time)
    when:
      String endDate = service.getEndDateCurrentMonth()
    then:
      expectDate == endDate
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

}
