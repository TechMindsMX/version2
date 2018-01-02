import grails.util.BuildSettings
import grails.util.Environment
import org.springframework.boot.ApplicationPid
import java.nio.charset.Charset
import java.text.SimpleDateFormat

if (!System.getProperty("PID")) {
    System.setProperty("PID", (new ApplicationPid()).toString())
}

def basePath = System.getenv("TOMCAT_HOME") ?: "."

conversionRule 'clr', org.springframework.boot.logging.logback.ColorConverter
conversionRule 'wex', org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter

def bySecond = timestamp("yyyyMMdd'T'HHmmss")

appender('ROLLING',RollingFileAppender) {
  encoder(PatternLayoutEncoder){
    charset = Charset.forName('UTF-8')
    pattern =
      '%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} ' + // Date
      '%clr(%5p) ' + // Log level
      '%clr(%property{PID}){magenta} ' + // PID
      '%clr(---){faint} %clr([%15.15t]){faint} ' + // Thread
      '%clr(%-40.40logger{39}){cyan} %clr(:){faint} ' + // Logger
      '%m%n%wex' // Message
  }
  rollingPolicy(TimeBasedRollingPolicy){
    FileNamePattern = "${basePath}/logs/modulusuno-%d{yyyy-MM}.log"
  }

}

appender('DEBUGPROD',RollingFileAppender) {
  encoder(PatternLayoutEncoder){
    charset = Charset.forName('UTF-8')
    pattern =
      '%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} ' + // Date
      '%clr(%5p) ' + // Log level
      '%clr(%property{PID}){magenta} ' + // PID
      '%clr(---){faint} %clr([%15.15t]){faint} ' + // Thread
      '%clr(%-40.40logger{39}){cyan} %clr(:){faint} ' + // Logger
      '%m%n%wex' // Message
  }
  rollingPolicy(TimeBasedRollingPolicy){
    FileNamePattern = "${basePath}/logs/modulusuno-%d{yyyy-MM}-debug.log"
  }

}
def artefacts = ['controllers','services','domains','conf','init','taglib']

switch(Environment.current){
  case Environment.DEVELOPMENT:
    artefacts.each { artefact ->
      logger "grails.app.services.grails.plugin", WARN, ['ROLLING'], false // formfields.FormFieldsTemplateService
      logger "grails.app.${artefact}", DEBUG, ['ROLLING'], false
    }
    break
  case Environment.TEST:
    artefacts.each { artefact ->
      logger "grails.app.${artefact}", WARN, ['ROLLING'], false
    }
    break
  case Environment.PRODUCTION:
    artefacts.each { artefact ->
      logger "grails.app.${artefact}", ERROR, ['ROLLING'], false
      logger "grails.app.${artefact}", DEBUG, ['DEBUGPROD'], false
    }
    break
}

root(WARN, ['ROLLING'])
