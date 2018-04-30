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
    FileNamePattern = "${basePath}/logs/modulusuno.log"
  }

}

logger 'grails.artefact.Interceptor', DEBUG, ['ROLLING'], false
logger 'grails.app.controllers', DEBUG, ['ROLLING'], false
logger 'grails.app.services', DEBUG, ['ROLLING'], false
logger('org.springframework', WARN)
root(WARN, ['ROLLING'])
