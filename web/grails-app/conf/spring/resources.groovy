import com.solab.alarms.channels.*
import com.solab.alarms.aop.*
import com.solab.alarms.*
import org.springframework.mail.SimpleMailMessage
import java.util.concurrent.TimeUnit
import org.springframework.beans.factory.config.MapFactoryBean

beans = {

  xmlns aop:"http://www.springframework.org/schema/aop"

  localeResolver(org.springframework.web.servlet.i18n.SessionLocaleResolver) {
    defaultLocale = new Locale("es","ES_MX")
    java.util.Locale.setDefault(defaultLocale)
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
