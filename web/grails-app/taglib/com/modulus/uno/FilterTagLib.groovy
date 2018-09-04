package com.modulus.uno

import com.modulus.uno.status.SaleOrderStatus

class FilterTagLib {
  static namespace = "modulusuno"
  static defaultEncodeAs = "raw"

  def showFilters = { attrs, body ->
    out << """
      <div class="row">
        <form id=\"formFilters\" action=\"/${attrs.controller}/${attrs.action}\">
   """
    Integer noFilters = attrs.filters.size()<=5 ? attrs.filters.size() : 5
    Integer width = 10 / noFilters
    attrs.filters.eachWithIndex { filter, index ->
      String value = attrs.filterValues ? attrs.filterValues."${filter}" : ""
      if (attrs.filterTypes[index]=="select") {
        String optionsSelectSource = attrs.optionsSelectFilters ? attrs.optionsSelectFilters[index] : []
        def optionsSelect = "getOptionsFrom${optionsSelectSource}"()
        out << """
          <div class="col-md-${width}">
            <label>${attrs.labels[index]}</label>
            <select class="form-control" id="${filter}" name="${filter}">
            <option value="">Todas...</option>
        """
        optionsSelect.each { option ->
          out << """
            <option value="${option}" ${value==option ? 'selected' : ''}>${option}</option>
          """ 
        }
        out << """
            </select>
          </div>
        """
      } else {
        out << """
          <div class="col-md-${width}">
            <label>${attrs.labels[index]}</label>
              <input type="text" class="form-control" id="${filter}" name="${filter}" value="${value}"/>
          </div>
        """
      }
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
      <div id="modalAlert" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
              <h4 class="modal-title">ModulusUno</h4>
            </div>
            <div class="modal-body">
              <p id="messageAlert"></p>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
            </div>
          </div>
        </div>
      </div>

    </div>
    <hr/>
    <br/>
    """
  }

  def getOptionsFromSaleOrderStatus() {
    SaleOrderStatus.values().collect { it.name().toString() }
  }
}
