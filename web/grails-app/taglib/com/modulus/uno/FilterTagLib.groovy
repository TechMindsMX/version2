package com.modulus.uno

class FilterTagLib {
  static namespace = "modulusuno"
  static defaultEncodeAs = "raw"

  def showFilters = { attrs, body ->
    out << """
      <div class="row">
        <g:form controller=\"${attrs.controller}\" action=\"${attrs.action}\">
   """
    Integer noFilters = attrs.filters.size()<=4 ? attrs.filters.size() : 4
    Integer width = 10 / noFilters
    attrs.filters.eachWithIndex { filter, index ->
      out << """
        <div class="col-md-${width}">
          <label>${attrs.labels[index]}</label>
          <input class="form-control" name="${filter}" type="text"/>
        </div>
      """
    }
    out << """
      <div class="col-md-2 text-right">
        <br/>
        <button class="btn btn-primary" type="submit">Buscar</button>
      </div>
    </g:form>
    </div>
    <hr/>
    <br/>
    """
  }
}
