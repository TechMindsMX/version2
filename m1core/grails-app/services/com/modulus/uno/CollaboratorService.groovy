package com.modulus.uno

import java.text.SimpleDateFormat
import static java.util.Calendar.*

class CollaboratorService {

  def grailsApplication

  String getBeginDateCurrentMonth(){
    def cini = Calendar.instance
    cini.set(date:1)
    new SimpleDateFormat("dd-MM-yyyy").format(cini.time)
  }

  String getEndDateCurrentMonth(){
    def cfin = Calendar.instance

    Boolean leapYear = false
    if ((cfin[YEAR]%4==0) && ((cfin[YEAR]%100!=0) || (cfin[YEAR]%400==0)))
      leapYear = true

    switch (cfin[MONTH]) {
      case FEBRUARY:
        if (leapYear) {
          cfin.set(date:29)
        } else {
          cfin.set(date:28)
        }
        break
      case [APRIL,JUNE,SEPTEMBER,NOVEMBER]:
        cfin.set(date:30)
        break
      default:
        cfin.set(date:31)
        break
    }
    cfin.set([hourOfDay:23, minute:59, second:59])

    new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(cfin.time)
  }

  Boolean periodIsValid(String beginDate, String endDate) {
    Date begin = new SimpleDateFormat("dd-MM-yyyy").parse(beginDate)
    Date end = new SimpleDateFormat("dd-MM-yyyy").parse(endDate)
    begin.before(end) || begin.equals(end)
  }

  Date getBeginDateOfCurrentWeek() {
    Calendar cal = Calendar.instance
    cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    String sdf = new SimpleDateFormat("dd-MM-yyyy").format(cal.time)
    new SimpleDateFormat("dd-MM-yyyy").parse(sdf)
  }

  Date getEndDateOfCurrentWeek() {
    Calendar cal = Calendar.instance
    cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY)
    String sdf = new SimpleDateFormat("dd-MM-yyyy").format(cal.time)
    new SimpleDateFormat("dd-MM-yyyy").parse(sdf)
  }

  Period getPeriodForConciliationStp() {
    Period period = new Period()
    Calendar cal = Calendar.instance
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    period.init = cal.time

    cal.set(Calendar.HOUR_OF_DAY, new Integer(grailsApplication.config.stp.finalTransfer.hour))
    cal.set(Calendar.MINUTE, new Integer(grailsApplication.config.stp.finalTransfer.minute))
    cal.set(Calendar.SECOND, new Integer(grailsApplication.config.stp.finalTransfer.second))
    period.end = cal.time
    period
  }

  Period getCurrentMonthPeriod() {
    new Period(
      init:new Date().parse("dd-MM-yyyy", getBeginDateCurrentMonth()),
      end:new Date().parse("dd-MM-yyyy HH:mm:ss", getEndDateCurrentMonth())
    )
  }

  Period createPeriod(String init, String end) {
    Date endDate = new Date().parse("dd-MM-yyyy", end)
    endDate.set(hourOfDay:23, minute:59, second:59)
    new Period(init:new Date().parse("dd-MM-yyyy", init), end:endDate)
  }

  Period getPeriodStpConciliationInDate(Date date) {
    Period period = new Period()
    Calendar cal = date.toCalendar()
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    period.init = cal.time

    cal.set(Calendar.HOUR_OF_DAY, new Integer(grailsApplication.config.stp.finalTransfer.hour))
    cal.set(Calendar.MINUTE, new Integer(grailsApplication.config.stp.finalTransfer.minute))
    cal.set(Calendar.SECOND, new Integer(grailsApplication.config.stp.finalTransfer.second))
    period.end = cal.time
    period
  }

}
