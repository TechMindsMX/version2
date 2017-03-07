package webservices

class BootStrap {

  def grailsApplication

  def init = { servletContext ->
    grailsApplication.getArtefacts("Domain").each{dc->
      if(dc.hasProperty("marshaller")){
        dc.clazz.marshaller()
      }
    }
  }
  def destroy = {
  }
}
