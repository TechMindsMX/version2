package com.modulus.uno

class FilterTagLib {
  static namespace = "modulusuno"
  static defaultEncodeAs = "raw"

  def showFilters = { attrs, body ->
    out << """
      <div class="row">
        <form action=\"/${attrs.controller}/${attrs.action}\">
   """
    Integer noFilters = attrs.filters.size()<=4 ? attrs.filters.size() : 4
    Integer width = 10 / noFilters
    attrs.filters.eachWithIndex { filter, index ->
      String value = attrs.filterValues ? attrs.filterValues."${filter}" : ""
      out << """
        <div class="col-md-${width}">
          <label>${attrs.labels[index]}</label>
          <input class="form-control" id="${filter}" name="${filter}" type="text" value="${value}"/>
        </div>
      """
    }
    out << """
      <div class="col-md-2 text-right">
        <button class="btn btn-primary" type="submit">Buscar</button>
      """
      if (attrs.filterValues) {
        out << """
          <br/><br/>
          <a href=\"/${attrs.controller}/${attrs.viewAll}\" class="btn btn-primary">Ver todo</a>
        """
      }
      out << """
      </div>
    </form>
    </div>
    <hr/>
    <br/>
    """
  }
}
