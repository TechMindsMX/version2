package com.modulus.uno

class ErrorController {

  def springSecurityService

  def serverError() {
    try{
      def req = request
      def exception = request?.exception
      log.error "User: ${springSecurityService?.currentUser?.username ?: 'No user identified(Maybe an API call)'}, Exception: ${exception?.className}, Line: ${exception?.lineNumber}, Throw: ${exception?.cause}, Origin: ${request?.getHeader('referer')}, Destiny: ${req?.strippedServletPath}, Parameters: ${request?.parameterMap.toString()}"
      render view:"serverError", model:[exception:exception, originUrl:request?.getHeader('referer'), destinyUrl:req?.strippedServletPath]
    } catch(e){
      render "No se puede manejar el error"
    }
  }
}
