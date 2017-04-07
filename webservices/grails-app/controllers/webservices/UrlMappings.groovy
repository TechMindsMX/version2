package webservices

class UrlMappings {

  static mappings = {
    "/$controller/$action?/$id?(.$format)?"{
      constraints {
        // apply constraints here        
      }
    }

    "/"(controller:"api")
    "500"(view:'/error')
    "404"(view:'/notFound')
  }
}
