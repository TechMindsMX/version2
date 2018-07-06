import com.solab.alarms.channels.*
import com.solab.alarms.aop.*
import com.solab.alarms.*
import org.springframework.mail.SimpleMailMessage
import java.util.concurrent.TimeUnit
import org.springframework.beans.factory.config.MapFactoryBean
import com.modulus.uno.twoFactorAuth.TwoFactorAuthService
import com.modulus.uno.twoFactorAuth.TwoFactorAuthenticationDetailsSource
import com.modulus.uno.twoFactorAuth.TwoFactorAuthenticationProvider
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy

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

  twoFactorAuthValidator(TwoFactorAuthService)
  twoFactorAuthenticationProvider(TwoFactorAuthenticationProvider) {
    twoFactorAuthValidator = ref('twoFactorAuthValidator')
    userDetailsService = ref('userDetailsService')
    passwordEncoder = ref('passwordEncoder')
    userCache = ref('userCache')
    saltSource = ref('saltSource')
    preAuthenticationChecks = ref('preAuthenticationChecks')
    postAuthenticationChecks = ref('postAuthenticationChecks')
    authoritiesMapper = ref('authoritiesMapper')
    hideUserNotFoundExceptions = true
  }
  authenticationDetailsSource(TwoFactorAuthenticationDetailsSource)
  sessionAuthenticationStrategy(NullAuthenticatedSessionStrategy)
}
