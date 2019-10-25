package com.modulus.uno

import org.grails.exceptions.ExceptionUtils

class ErrorController {

  def springSecurityService

  def serverError() {
    try{
      def req = request
      def exception = request?.exception
      String message = ExceptionUtils.getRootCause(exception)?.message

      log.error "User: ${springSecurityService?.currentUser?.username ?: 'No user identified(Maybe an API call)'}, Exception: ${exception?.className}, Line: ${exception?.lineNumber}, Throw: ${exception?.cause}, Origin: ${request?.getHeader('referer')}, Destiny: ${req?.strippedServletPath}, Parameters: ${request?.parameterMap.toString()}"
      render view:"serverError", model:[exception:exception, message: message, originUrl:request?.getHeader('referer'), destinyUrl:req?.strippedServletPath]
    } catch(e){
      render "No se puede manejar el error"
    }
  }
}
