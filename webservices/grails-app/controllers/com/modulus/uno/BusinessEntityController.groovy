package com.modulus.uno

import com.github.rahulsom.swaggydoc.*
import com.wordnik.swagger.annotations.*

@Api
class BusinessEntityController {

  static allowedMethods = [saveClient: "POST", saveProvider: "POST", saveEmployee: "POST"]

  def businessEntityService

  @SwaggyList
  def index() {
    respond BusinessEntity.list(),status: 200, formats: ['json']
  }

  def save() {
    BusinessEntity businessEntity = new BusinessEntity(params.properties)

    if (params.clientProviderType.equals("EMPLEADO")) {
      businessEntity.website="http://www.employee.com"
      businessEntity.type = BusinessEntityType.FISICA
      params.persona = 'fisica'
    }

    Company company = Company.findById(params.companyId)
    businessEntityService.generatedBussinessEntityProperties(businessEntity, params, company)

    respond businessEntity, status: 201, formats: ['json']
  }

  @SwaggySave(extraParams = [
      @ApiImplicitParam(name = 'rfc', value = '', dataType = 'string',paramType = 'form'),
      @ApiImplicitParam(name = 'website', value = 'http://www.example.com', dataType = 'string',paramType = 'form'),
      @ApiImplicitParam(name = 'name', value = '', dataType = 'string',paramType = 'form'),
      @ApiImplicitParam(name = 'lastName', value = '', dataType = 'string',paramType = 'form'),
      @ApiImplicitParam(name = 'motherLastName', value = '', dataType = 'string',paramType = 'form'),
      @ApiImplicitParam(name = 'type', value = 'FISICA, MORAL', dataType = 'string',paramType = 'form'),
      @ApiImplicitParam(name = 'clientProviderType', value = 'CLIENTE', dataType = 'string',paramType = 'form'),
      @ApiImplicitParam(name = 'businessName', value = 'Requerido si si es MORAL', dataType = 'number',paramType = 'form'),
      @ApiImplicitParam(name = 'companyId', value = '', dataType = 'number',paramType = 'form'),
      @ApiImplicitParam(name = 'artemisaId', value = '', dataType = 'number',paramType = 'form')
      ])
  def saveClient() {
    BusinessEntity businessEntity = new BusinessEntity()
    businessEntity.rfc = params.rfc
    businessEntity.website = params.website
    businessEntity.type = BusinessEntityType."${params.type.toUpperCase()}"
    businessEntity.artemisaId = params.artemisaId
    Company company = Company.findById(params.companyId)
    params.put('persona',"${params.type.toLowerCase()}")
    businessEntityService.generatedBussinessEntityProperties(businessEntity, params, company)
    respond businessEntity, status: 201, formats: ['json']
  }

  @SwaggySave(extraParams = [
      @ApiImplicitParam(name = 'rfc', value = '', dataType = 'string',paramType = 'form'),
      @ApiImplicitParam(name = 'website', value = 'http://www.example.com', dataType = 'string',paramType = 'form'),
      @ApiImplicitParam(name = 'name', value = '', dataType = 'string',paramType = 'form'),
      @ApiImplicitParam(name = 'lastName', value = '', dataType = 'string',paramType = 'form'),
      @ApiImplicitParam(name = 'motherLastName', value = '', dataType = 'string',paramType = 'form'),
      @ApiImplicitParam(name = 'type', value = 'FISICA, MORAL', dataType = 'string',paramType = 'form'),
      @ApiImplicitParam(name = 'businessName', value = 'Requerido si si es MORAL', dataType = 'number',paramType = 'form'),
      @ApiImplicitParam(name = 'clientProviderType', value = 'PROVEEDOR', dataType = 'string',paramType = 'form'),
      @ApiImplicitParam(name = 'companyId', value = '', dataType = 'number',paramType = 'form'),
      @ApiImplicitParam(name = 'artemisaId', value = '', dataType = 'number',paramType = 'form')
      ])
  def saveProvider() {
    BusinessEntity businessEntity = new BusinessEntity()
    businessEntity.rfc = params.rfc
    businessEntity.website = params.website
    businessEntity.type = BusinessEntityType."${params.type.toUpperCase()}"
    businessEntity.artemisaId = params.artemisaId
    Company company = Company.findById(params.companyId)
    params.put('persona',"${params.type.toLowerCase()}")
    businessEntityService.generatedBussinessEntityProperties(businessEntity, params, company)
    respond businessEntity, status: 201, formats: ['json']
  }

  @SwaggySave(extraParams = [
      @ApiImplicitParam(name = 'rfc', value = '', dataType = 'string',paramType = 'form'),
      @ApiImplicitParam(name = 'curp', value = '', dataType = 'string',paramType = 'form'),
      @ApiImplicitParam(name = 'website', value = 'http://www.example.com', dataType = 'string',paramType = 'form'),
      @ApiImplicitParam(name = 'name', value = '', dataType = 'string',paramType = 'form'),
      @ApiImplicitParam(name = 'lastName', value = '', dataType = 'string',paramType = 'form'),
      @ApiImplicitParam(name = 'motherLastName', value = '', dataType = 'string',paramType = 'form'),
      @ApiImplicitParam(name = 'numeroEmpleado', value = '', dataType = 'string',paramType = 'form'),
      @ApiImplicitParam(name = 'type', value = 'FISICA', dataType = 'string',paramType = 'form'),
      @ApiImplicitParam(name = 'clientProviderType', value = 'EMPLEADO', dataType = 'string',paramType = 'form'),
      @ApiImplicitParam(name = 'companyId', value = '', dataType = 'number',paramType = 'form'),
      @ApiImplicitParam(name = 'artemisaId', value = '', dataType = 'number',paramType = 'form')
      ])
  def saveEmployee() {
    BusinessEntity businessEntity = new BusinessEntity()
    businessEntity.rfc = params.rfc
    businessEntity.website = params.website
    businessEntity.type = BusinessEntityType."${params.type.toUpperCase()}"
    businessEntity.artemisaId = params.artemisaId
    if (params.clientProviderType.equals("EMPLEADO")) {
      businessEntity.website="http://www.employee.com"
      businessEntity.type = BusinessEntityType.FISICA
      params.persona = 'fisica'
    }
    Company company = Company.findById(params.companyId)
    businessEntityService.generatedBussinessEntityProperties(businessEntity, params, company)
    respond businessEntity, status: 201, formats: ['json']
  }

  @ApiOperation(value='Get all emplooyees for company', response = BusinessEntity, responseContainer = 'list')
  @ApiImplicitParams ([
    @ApiImplicitParam(name = 'corporateId', value = '', dataType = 'number',paramType = 'query')
  ])
  def getAllActiveEmployeesForCompany() {
    def company = Company.get(params.corporateId)
    if(company){
      List<BusinessEntity> allEmployees = businessEntityService.getAllActiveEmployeesForCompany(company)
      respond allEmployees,status: 200, formats: ['json']
    }
    else{
      response.sendError(404,"Company not found" )
    }
  }

}
