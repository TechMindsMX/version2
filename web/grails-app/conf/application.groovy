grails.plugin.springsecurity.providerNames = [
  'twoFactorAuthenticationProvider',
  'anonymousAuthenticationProvider',
  'rememberMeAuthenticationProvider']

grails.views.default.codec = "html"
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
grails.plugin.databasemigration.updateOnStart = true
grails.plugin.databasemigration.updateOnStartFileNames = ['changelog.groovy']
// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.modulus.uno.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.modulus.uno.UserRole'
grails.plugin.springsecurity.authority.className = 'com.modulus.uno.Role'
grails.plugin.springsecurity.securityConfigType = "InterceptUrlMap"
grails.plugin.springsecurity.successHandler.defaultTargetUrl = '/dashboard'
grails.plugin.springsecurity.logout.postOnly = false

grails.plugin.springsecurity.interceptUrlMap = [
  [pattern: '/',                       access: ['permitAll']],
  [pattern: '/error',                  access: ['permitAll']],
  [pattern: '/index',                  access: ['permitAll']],
  [pattern: '/index.gsp',              access: ['permitAll']],
  [pattern: '/shutdown',               access: ['permitAll']],
  [pattern: '/assets/**',              access: ['permitAll']],
  [pattern: '/**/js/**',               access: ['permitAll']],
  [pattern: '/**/css/**',              access: ['permitAll']],
  [pattern: '/**/images/**',           access: ['permitAll']],
  [pattern: '/**/favicon.ico',         access: ['permitAll']],
  [pattern: '/login',                  access: ['permitAll']],
  [pattern: '/login/**',               access: ['permitAll']],
  [pattern: '/logout',                 access: ['IS_AUTHENTICATED_FULLY']],
  [pattern: '/logout/**',              access: ['IS_AUTHENTICATED_FULLY']],
  [pattern: '/user/**',                access: ['permitAll']],
  [pattern: '/confirmation/**',        access: ['permitAll']],
  [pattern: '/recovery/**',            access: ['permitAll']],
  [pattern: '/twoFactorAuth/**',       access: ['permitAll']],
  [pattern: '/corporate/**',           access: ['IS_AUTHENTICATED_FULLY']],
  [pattern: '/dashboard/**',           access: ['IS_AUTHENTICATED_REMEMBERED']],
  [pattern: '/company/**',             access: ['IS_AUTHENTICATED_FULLY']],
  [pattern: '/address/**',             access: ['IS_AUTHENTICATED_FULLY']],
  [pattern: '/bankAccount/**',         access: ['IS_AUTHENTICATED_FULLY']],
  [pattern: '/uploadDocuments/**',     access: ['IS_AUTHENTICATED_FULLY']],
  [pattern: '/telephone/**',           access: ['IS_AUTHENTICATED_FULLY']],
  [pattern: '/legalRepresentative/**', access: ['IS_AUTHENTICATED_FULLY']],
  [pattern: '/requestCompany/**',      access: ['IS_AUTHENTICATED_FULLY']],
  [pattern: '/mamangerApplication/**', access: ['IS_AUTHENTICATED_FULLY']],
  [pattern: '/activation/**',          access: ['permitAll']],
  [pattern: '/documentTemplates/**',   access: ['permitAll']],
  [pattern: '/modulusuno/**',          access:['IS_AUTHENTICATED_FULLY']],
  [pattern: '/depositOrder/**',        access: ['IS_AUTHENTICATED_FULLY']],
  [pattern: '/cashOutOrder/**',        access: ['IS_AUTHENTICATED_FULLY']],
  [pattern: '/saleOrder/**',           access: ['IS_AUTHENTICATED_FULLY']],
  [pattern: '/**',                     access: ['IS_AUTHENTICATED_REMEMBERED']]
]
