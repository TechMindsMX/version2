import com.solab.alarms.channels.*
import com.solab.alarms.aop.*
import com.solab.alarms.*
import org.springframework.mail.SimpleMailMessage

import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory
import com.ullink.slack.simpleslackapi.impl.SlackWebSocketSessionImpl
import com.ullink.slack.simpleslackapi.SlackSession
import java.util.concurrent.TimeUnit
import org.springframework.beans.factory.config.MapFactoryBean

beans = {

  xmlns aop:"http://www.springframework.org/schema/aop"

  localeResolver(org.springframework.web.servlet.i18n.SessionLocaleResolver) {
    defaultLocale = new Locale("es","ES_MX")
    java.util.Locale.setDefault(defaultLocale)
  }

  def token = grailsApplication.config.slack.token

  slackSession(SlackWebSocketSessionImpl, token, false) { bean ->
    bean.initMethod = 'connect'
  }

  springDataSourceBeanPostProcessor(net.bull.javamelody.SpringDataSourceBeanPostProcessor)

  monitoringAdvice(net.bull.javamelody.MonitoringSpringInterceptor)

  aop {
    config {
      pointcut(expression:"execution(* com.modulus.uno..**..*(..))",id:"monitoringPointcut")
      advisor("advice-ref":"monitoringAdvice","pointcut-ref":"monitoringPointcut")
    }
  }

}
