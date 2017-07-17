package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource

@TestFor(XlsImportService)
class XlsImportServiceSpec extends Specification {

  File xlsNoImssFile, textFile, emptyFile, notDataFile
  Resource xlsNoImss = new ClassPathResource("xlsOkSinImss.xlsx")
  Resource otherFile = new ClassPathResource("fileText.txt")
  Resource empty = new ClassPathResource("xlsErrorEmpty.xlsx")
  Resource notData = new ClassPathResource("xlsErrorNotData.xlsx")

  def setup() {
    xlsNoImssFile = xlsNoImss.getFile()
    textFile = otherFile.getFile()
    emptyFile = empty.getFile()
    notDataFile = notData.getFile()
  }

  void "Should create the workbook from xls file"() {
    when:
      def result = service.getWorkbookFromXlsFile(xlsNoImssFile)
    then:
      result
  }

  void "Should thrown exception when file is not xlsx file"(){
    when:
      def result = service.getWorkbookFromXlsFile(textFile)
    then:
      thrown BusinessException
  }

  void "Should throw exception when file is empty"() {
    when:
      def result = service.parseXlsMassiveEmployee(emptyFile)
    then:
      thrown BusinessException
  }

  void "Should throw exception when file has only headers"() {
    when:
      def result = service.parseXlsMassiveEmployee(notDataFile)
    then:
      thrown BusinessException
  }

}
