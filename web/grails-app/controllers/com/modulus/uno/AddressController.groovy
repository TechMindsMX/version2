package com.modulus.uno

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class AddressController {

  def companyService
  def addressService
  def businessEntityService
  def springSecurityService

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

  def index(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    respond Address.list(params), model:[addressCount: Address.count()]
  }

  def show(Address address) {
    respond address
  }

  def create() {
    def businessEntity = BusinessEntity.get(params.businessEntity)
    def addressTypes = addressService.getAddresTypesForOrganization(session.company.toLong())
    if (businessEntity)
      addressTypes = addressService.getAddressTypesForBusinessEntity(businessEntity)
    respond new Address(params),model:[businessEntity:businessEntity, addressTypes:addressTypes]
  }

  @Transactional
  def save(Address address) {
    if (address.hasErrors()) {
      respond address.errors, view:'create', model:[company:session.company]
      return
    }
    def domain = addressService.createAddressForAObject(address, params.long('businessEntityId'), session.company.toLong())

    redirect(controller:"${domain.getClass().getName()}",action:"show",id:domain.id)
  }

  def edit(Address address) {
    def businessEntity = BusinessEntity.get(params.businessEntityId)
    def addressTypes = addressService.getAddresTypesForOrganization(session.company.toLong())
    if (businessEntity)
      addressTypes = addressService.getAllAddressTypes()

    respond address,model:[addressTypes:addressTypes, relation:params.relation, businessEntity:businessEntity]
  }

  @Transactional
  def update(Address address) {
    if (address == null) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

    if (address.hasErrors()) {
      transactionStatus.setRollbackOnly()
      respond address.errors, view:'edit'
      return
    }

    address.save flush:true

    if(params.businessEntityId){
      redirect(controller:"businessEntity",action:"show",id:params.businessEntityId)
      return
    }

    if(session.company){
      redirect(controller:"company",action:"show",id:session.company)
      return
    }

  }

  @Transactional
  def delete(Address address) {

    if (address == null) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }

    address.delete flush:true

    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.deleted.message', args: [message(code: 'address.label', default: 'Address'), address.id])
        redirect action:"index", method:"GET"
      }
      '*'{ render status: NO_CONTENT }
    }
  }

  protected void notFound() {
    request.withFormat {
      form multipartForm {
        flash.message = message(code: 'default.not.found.message', args: [message(code: 'address.label', default: 'Address'), params.id])
        redirect action: "index", method: "GET"
      }
      '*'{ render status: NOT_FOUND }
    }
  }

}
