package com.modulus.uno.paysheet

import com.modulus.uno.invoice.paysheetReceipt.*
import com.modulus.uno.invoice.*

import com.modulus.uno.PaysheetReceiptCommand
import com.modulus.uno.Company
import com.modulus.uno.DataImssEmployee
import com.modulus.uno.EmployeeLink
import com.modulus.uno.Address
import com.modulus.uno.AddressType
import com.modulus.uno.RestException

import com.modulus.uno.DataImssEmployeeService
import com.modulus.uno.CommissionTransactionService
import com.modulus.uno.RestService

import grails.util.Environment
import groovy.json.JsonSlurper
import grails.transaction.Transactional
import org.springframework.transaction.annotation.Propagation

class PaysheetReceiptService {

  PaysheetProjectService paysheetProjectService
  DataImssEmployeeService dataImssEmployeeService
  CommissionTransactionService commissionTransactionService
  RestService restService
  def grailsApplication

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  String generatePaysheetReceiptForEmployeeAndSchema(PaysheetEmployee paysheetEmployee, PaymentSchema schema) {
    PaysheetReceiptCommand paysheetReceipt = createPaysheetReceiptFromPaysheetEmployeeForSchema(paysheetEmployee, schema)
    String stampUuid = stampPaysheetReceipt(paysheetReceipt)
    registerCommissionTransaction(paysheetEmployee, paysheetReceipt, schema)
    stampUuid
  }

  def generatePdfFromPaysheetReceiptForEmployeeAndSchema(PaysheetEmployee paysheetEmployee, PaymentSchema schema) {
    PaysheetReceiptCommand paysheetReceipt = createPaysheetReceiptFromPaysheetEmployeeForSchema(paysheetEmployee, schema)
    paysheetReceipt.datosDeFacturacion.uuid = schema == PaymentSchema.IMSS ? paysheetEmployee.paysheetReceiptUuidSA : paysheetEmployee.paysheetReceiptUuidIAS 
    sendToGeneratePdfFromPaysheetReceipt(paysheetReceipt)
  }

  PaysheetReceiptCommand createPaysheetReceiptFromPaysheetEmployeeForSchema(PaysheetEmployee paysheetEmployee, PaymentSchema schema) {
    PaysheetReceiptCommand paysheetReceipt = new PaysheetReceiptCommand (
      datosDeFacturacion: createInvoiceDataFromPaysheetEmployee(paysheetEmployee),
      emisor: createEmitterFromPaysheetEmployeeAndSchema(paysheetEmployee, schema),
      receptor: createReceiverFromPaysheetEmployeeAndSchema(paysheetEmployee, schema),
      nomina: createPaysheetDataFromPaysheetEmployeeAndSchema(paysheetEmployee, schema),
      esquema: schema.toString(),
      id: getCompanyIdSchemaPayer(paysheetEmployee, schema)
    )
    paysheetReceipt.concepto = createConceptForPaysheetEmployee(paysheetReceipt)
    log.info "Concepto: ${paysheetReceipt.concepto.dump()}"
    paysheetReceipt
  }

  DatosDeFacturacion createInvoiceDataFromPaysheetEmployee(PaysheetEmployee paysheetEmployee) {
    new DatosDeFacturacion(
      folio: paysheetEmployee.paysheet.paysheetContract.nextFolio,
      serie: paysheetEmployee.paysheet.paysheetContract.serie
    )
  }

  Contribuyente createEmitterFromPaysheetEmployeeAndSchema(paysheetEmployee, PaymentSchema schema) {
    PaysheetProject paysheetProject = paysheetProjectService.getPaysheetProjectByPaysheetContractAndName(paysheetEmployee.paysheet.paysheetContract, paysheetEmployee.paysheet.prePaysheet.paysheetProject)
    PayerPaysheetProject payer = paysheetProject.payers.find { payer -> payer.paymentSchema == schema }
    Address address = payer.company.addresses.find { address -> address.addressType == AddressType.FISCAL }
    new Contribuyente (
      registroPatronal: paysheetEmployee.paysheet.paysheetContract.employerRegistration,
      datosFiscales: new DatosFiscales (
        razonSocial: payer.company.bussinessName,
        rfc: (Environment.current == Environment.PRODUCTION) ? payer.company.rfc : "AAA010101AAA",
        calle: address.street,
        noExterior: address.streetNumber,
        noInterior: address.suite ?: "SN",
        ciudad: address.city,
        colonia: address.colony ?: address.neighboorhood,
        delegacion: address.town,
        codigoPostal: address.zipCode
      )
    )
  }

  Empleado createReceiverFromPaysheetEmployeeAndSchema(PaysheetEmployee paysheetEmployee, PaymentSchema schema) {
    new Empleado(
      rfc: (Environment.current == Environment.PRODUCTION) ? paysheetEmployee.prePaysheetEmployee.rfc : "BUPA690824IRA",
      nombre: paysheetEmployee.prePaysheetEmployee.nameEmployee,
      curp: paysheetEmployee.prePaysheetEmployee.curp,
      datosBancarios: buildDataBankForEmployee(paysheetEmployee),
      datosLaborales: createJobDataFromPaysheetEmployeeAndSchema(paysheetEmployee, schema)
    )
  }

  DatosBancarios buildDataBankForEmployee(PaysheetEmployee paysheetEmployee) {
    String bank = paysheetEmployee.prePaysheetEmployee.account || paysheetEmployee.prePaysheetEmployee.cardNumber ? paysheetEmployee.prePaysheetEmployee.bank.bankingCode.substring(2,5) : ""
    String account = paysheetEmployee.prePaysheetEmployee.account ?: (paysheetEmployee.prePaysheetEmployee.cardNumber ?: (paysheetEmployee.prePaysheetEmployee.clabe ?: ""))
    new DatosBancarios(banco:bank, cuenta:account)
  }

  DatosLaborales createJobDataFromPaysheetEmployeeAndSchema(PaysheetEmployee paysheetEmployee, PaymentSchema paymentSchema) {
    PaysheetProject paysheetProject = paysheetProjectService.getPaysheetProjectByPaysheetContractAndName(paysheetEmployee.paysheet.paysheetContract, paysheetEmployee.paysheet.prePaysheet.paysheetProject)
    EmployeeLink employeeLink = EmployeeLink.findByCompanyAndEmployeeRef(paysheetEmployee.paysheet.paysheetContract.company, paysheetEmployee.prePaysheetEmployee.rfc)
    DataImssEmployee dataImssEmployee = DataImssEmployee.findByEmployee(employeeLink)
    new DatosLaborales (
      entidad: paysheetProject.federalEntity.name(),
      noEmpleado: paysheetEmployee.prePaysheetEmployee.numberEmployee,
      tipoContrato: paymentSchema == PaymentSchema.IMSS ? dataImssEmployee.contractType.key : ContractType.WORK_WITHOUT_RELATION.key,
      periodoPago: dataImssEmployee.paymentPeriod.key,
      tipoRegimen: paymentSchema == PaymentSchema.IMSS ? dataImssEmployee.regimeType.key : RegimeType.FEES_ASSIMILATED.key,
      tipoJornada: dataImssEmployee.workDayType.key,
      datosImss: new DatosImss (
        antiguedad: dataImssEmployeeService.calculateLaborOldInSATFormat(dataImssEmployee, paysheetEmployee.paysheet.prePaysheet.endPeriod),
        departamento: dataImssEmployee.department,
        fechaAlta: dataImssEmployee.registrationDate.format("yyyy-MM-dd"),
        nss: dataImssEmployee.nss,
        puesto: dataImssEmployee.job,
        riesgo: dataImssEmployee.jobRisk.key,
        salarioBaseCotizacion: paymentSchema == PaymentSchema.IMSS ? paysheetEmployee.breakdownPayment.baseQuotation : new BigDecimal(0),
        salarioDiarioIntegrado: paymentSchema == PaymentSchema.IMSS ? paysheetEmployee.breakdownPayment.integratedDailySalary : new BigDecimal(0)
      )
    )
  }

  Nomina createPaysheetDataFromPaysheetEmployeeAndSchema(PaysheetEmployee paysheetEmployee, PaymentSchema schema) {
    new Nomina (
      fechaInicio: paysheetEmployee.paysheet.prePaysheet.initPeriod.format("yyyy-MM-dd"),
      fechaFin: paysheetEmployee.paysheet.prePaysheet.endPeriod.format("yyyy-MM-dd"),
      fechaPago: paysheetEmployee.paysheet.prePaysheet.endPeriod.format("yyyy-MM-dd"),
      diasPagados: paysheetEmployee.paysheet.prePaysheet.endPeriod - paysheetEmployee.paysheet.prePaysheet.initPeriod + 1,
      percepciones: createPerceptionsFromPaysheetEmployeeAndSchema(paysheetEmployee, schema),
      deducciones: createDeductionsFromPaysheetEmployeeAndSchema(paysheetEmployee, schema),
      otrosPagos: createOtherPerceptionsFromPaysheetEmployeeAndSchema(paysheetEmployee, schema)
    )
  }

  Percepciones createPerceptionsFromPaysheetEmployeeAndSchema(PaysheetEmployee paysheetEmployee, PaymentSchema schema) {
    Percepciones percepciones = new Percepciones(detalles:[])
    percepciones.detalles.add("createSalaryDetailForSchema${schema.name()}"(paysheetEmployee))
    percepciones.detalles.addAll(addPerceptionIncidenceForSchema(paysheetEmployee, schema))
    percepciones
  }

  DetalleNomina createSalaryDetailForSchemaIMSS(PaysheetEmployee paysheetEmployee) {
    def incidences = paysheetEmployee.prePaysheetEmployee.incidences.findAll { incidence -> incidence.type == IncidenceType.PERCEPTION && incidence.keyType == PerceptionType.P001.key && incidence.paymentSchema == PaymentSchema.IMSS }
    BigDecimal totalSalary = paysheetEmployee.salaryImss
    if (incidences) {
      totalSalary += incidences.exemptAmount.sum() + incidences.taxedAmount.sum()
    }
    new DetalleNomina(clave: PerceptionType.P001.name(), descripcion: PerceptionType.P001.description, tipo: PerceptionType.P001.key, importeExento: new BigDecimal(0), importeGravado: totalSalary)
  }

  DetalleNomina createSalaryDetailForSchemaASSIMILABLE(PaysheetEmployee paysheetEmployee) {
    def incidences = paysheetEmployee.prePaysheetEmployee.incidences.findAll { incidence -> incidence.type == IncidenceType.PERCEPTION && incidence.keyType == PerceptionType.P046.key && incidence.paymentSchema == PaymentSchema.ASSIMILABLE }
    BigDecimal totalSalary = paysheetEmployee.crudeAssimilable
    if (incidences) {
      totalSalary += incidences.exemptAmount.sum() + incidences.taxedAmount.sum()
    }
    new DetalleNomina(clave: PerceptionType.P046.name(), descripcion: PerceptionType.P046.description, tipo: PerceptionType.P046.key, importeExento: new BigDecimal(0), importeGravado: totalSalary)
  }

  List<DetalleNomina> addPerceptionIncidenceForSchema(PaysheetEmployee paysheetEmployee, PaymentSchema schema) {
    List<DetalleNomina> perceptionIncidences = []
    if (schema == PaymentSchema.IMSS) {
      def incidences = paysheetEmployee.prePaysheetEmployee.incidences.findAll { incidence -> incidence.type == IncidenceType.PERCEPTION && incidence.keyType != PerceptionType.P001.key && incidence.keyType != PerceptionType.P046.key && incidence.paymentSchema == schema }
      incidences.each { incidence -> 
        DetalleNomina detalle = new DetalleNomina(clave: incidence.internalKey, descripcion: incidence.description, tipo: incidence.keyType, importeExento: incidence.exemptAmount, importeGravado: incidence.taxedAmount)
        if (incidence.extraHourIncidence) {
          detalle.diasHrsExtra = incidence.extraHourIncidence.days
          detalle.tipoHrsExtra = incidence.extraHourIncidence.type
          detalle.totalHrsExtra = incidence.extraHourIncidence.quantity
          detalle.importeHrsExtra = incidence.extraHourIncidence.amount
        }
        perceptionIncidences.add(detalle)
      }
    }
    perceptionIncidences 
  }

  Deducciones createDeductionsFromPaysheetEmployeeAndSchema(PaysheetEmployee paysheetEmployee, PaymentSchema schema) {
    Deducciones deducciones = new Deducciones(detalles:[])
    deducciones.detalles.addAll("createDeductionDetailForSchema${schema.name()}"(paysheetEmployee))
    deducciones.detalles.addAll(addDeductionIncidenceForSchema(paysheetEmployee, schema))
    deducciones.detalles = deducciones.detalles - null
    deducciones
  }

  List<DetalleNomina> createDeductionDetailForSchemaIMSS(PaysheetEmployee paysheetEmployee) {
    List<DetalleNomina> deductions = []

    def incidencesSS = paysheetEmployee.prePaysheetEmployee.incidences.findAll { incidence -> incidence.type == IncidenceType.DEDUCTION  && incidence.keyType == DeductionType.D001.key && incidence.paymentSchema == PaymentSchema.IMSS }
    BigDecimal totalSS = paysheetEmployee.socialQuota
    if (incidencesSS) {
      totalSS += incidencesSS.exemptAmount.sum() + incidencesSS.taxedAmount.sum()
    }
    deductions.add(new DetalleNomina(clave: DeductionType.D001.name(), descripcion: DeductionType.D001.description, tipo: DeductionType.D001.key, importeExento: new BigDecimal(0), importeGravado: totalSS))

    if ((paysheetEmployee.subsidySalary - paysheetEmployee.incomeTax) < 0) {
      def incidencesISR = paysheetEmployee.prePaysheetEmployee.incidences.findAll { incidence -> incidence.type == IncidenceType.DEDUCTION  && incidence.keyType == DeductionType.D002.key && incidence.paymentSchema == PaymentSchema.IMSS }
      BigDecimal totalIsr = paysheetEmployee.incomeTax - paysheetEmployee.subsidySalary
      if (incidencesISR) {
        totalIsr += incidencesISR.exemptAmount.sum() + incidencesISR.taxedAmount.sum()
      }
      deductions.add(new DetalleNomina(clave: DeductionType.D002.name(), descripcion: DeductionType.D002.description, tipo: DeductionType.D002.key, importeExento: new BigDecimal(0), importeGravado: totalIsr))
    }
    deductions
  }

  List<DetalleNomina> createDeductionDetailForSchemaASSIMILABLE(PaysheetEmployee paysheetEmployee) {
    def incidencesISR = paysheetEmployee.prePaysheetEmployee.incidences.findAll { incidence -> incidence.type == IncidenceType.DEDUCTION  && incidence.keyType == DeductionType.D002.key && incidence.paymentSchema == PaymentSchema.ASSIMILABLE }
    BigDecimal totalIsr = paysheetEmployee.incomeTaxAssimilable
    if (incidencesISR) {
      totalIsr += incidencesISR.exemptAmount.sum() + incidencesISR.taxedAmount.sum()
    }
    [new DetalleNomina(clave: DeductionType.D002.name(), descripcion: DeductionType.D002.description, tipo: DeductionType.D002.key, importeExento: new BigDecimal(0), importeGravado: totalIsr)]
  }

  List<DetalleNomina> addDeductionIncidenceForSchema(PaysheetEmployee paysheetEmployee, PaymentSchema schema) {
    List<DetalleNomina> deductionIncidences = []
    if (schema == PaymentSchema.IMSS) {
      def incidences = paysheetEmployee.prePaysheetEmployee.incidences.findAll { incidence -> incidence.type == IncidenceType.DEDUCTION  && incidence.keyType != DeductionType.D002.key && incidence.paymentSchema == schema }
      incidences.each { incidence -> 
        DetalleNomina detalle = new DetalleNomina(clave: incidence.internalKey, descripcion: incidence.description, tipo: incidence.keyType, importeExento: incidence.exemptAmount, importeGravado: incidence.taxedAmount)
        deductionIncidences.add(detalle)
      }
    }
    deductionIncidences
  }

  List<DetalleNomina> createOtherPerceptionsFromPaysheetEmployeeAndSchema(PaysheetEmployee paysheetEmployee, PaymentSchema schema) {
    List<DetalleNomina> otherPerceptionIncidences = []

    if (schema == PaymentSchema.IMSS) { 
      if ((paysheetEmployee.subsidySalary - paysheetEmployee.incomeTax) > 0) {
        otherPerceptionIncidences.add(new DetalleNomina(clave: OtherPerceptionType.O002.name(), descripcion: OtherPerceptionType.O002.description, tipo: OtherPerceptionType.O002.key, importeExento: new BigDecimal(0), importeGravado: paysheetEmployee.subsidySalary - paysheetEmployee.incomeTax, subsidio: paysheetEmployee.subsidySalary))
      }

      def incidences = paysheetEmployee.prePaysheetEmployee.incidences.findAll { incidence -> incidence.type == IncidenceType.OTHER_PERCEPTION && incidence.paymentSchema == schema }
      incidences.each { incidence -> 
        DetalleNomina detalle = new DetalleNomina(clave: incidence.internalKey, descripcion: incidence.description, tipo: incidence.keyType, importeExento: incidence.exemptAmount, importeGravado: incidence.taxedAmount)
        if (incidence.extraHourIncidence) {
          detalle.diasHrsExtra = incidence.extraHourIncidence.days
          detalle.tipoHrsExtra = incidence.extraHourIncidence.type
          detalle.totalHrsExtra = incidence.extraHourIncidence.quantity
          detalle.importeHrsExtra = incidence.extraHourIncidence.amount
        }
        otherPerceptionIncidences.add(detalle)
      }
    }
    otherPerceptionIncidences    
  }

  Concepto createConceptForPaysheetEmployee(PaysheetReceiptCommand paysheetReceipt) {
    new Concepto (
      valorUnitario: (paysheetReceipt.nomina.percepciones?.detalles*.importeExento.sum() ?: 0) + (paysheetReceipt.nomina.percepciones?.detalles*.importeGravado.sum() ?: 0) + (paysheetReceipt.nomina.otrosPagos ? paysheetReceipt.nomina.otrosPagos*.importeExento.sum() : 0) + (paysheetReceipt.nomina.otrosPagos ? paysheetReceipt.nomina.otrosPagos*.importeGravado.sum() : 0),
      descuento: paysheetReceipt.nomina.deducciones.detalles ? paysheetReceipt.nomina.deducciones.detalles*.importeExento.sum() + paysheetReceipt.nomina.deducciones.detalles*.importeGravado.sum() : 0
    )
  }

  String getCompanyIdSchemaPayer(PaysheetEmployee paysheetEmployee, PaymentSchema schema) {
    PaysheetProject paysheetProject = paysheetProjectService.getPaysheetProjectByPaysheetContractAndName(paysheetEmployee.paysheet.paysheetContract, paysheetEmployee.paysheet.prePaysheet.paysheetProject)
    PayerPaysheetProject payer = paysheetProject.payers.find { payer -> payer.paymentSchema == schema } 
    payer.company.id.toString()
  }

  String stampPaysheetReceipt(PaysheetReceiptCommand paysheetReceipt) {
    def result = restService.sendFacturaCommandWithAuth(paysheetReceipt, grailsApplication.config.modulus.paysheetReceiptCreate)
    if (!result) {
      throw new RestException("No se pudo generar el recibo de nómina") 
    }

    if (result.text.startsWith("Error")) {
      throw new RestException(result.text) 
    }

    result.text
  }

  def registerCommissionTransaction(PaysheetEmployee employee, PaysheetReceiptCommand paysheetReceipt, PaymentSchema schema) {
    BigDecimal commissionBaseAmount = schema == PaymentSchema.IMSS ? employee.imssSalaryNet : employee.netAssimilable
    Company company = Company.get(paysheetReceipt.id)
    commissionTransactionService.registerCommissionForPaysheetReceipt(commissionBaseAmount, company)
  }

  def sendToGeneratePdfFromPaysheetReceipt(PaysheetReceiptCommand paysheetReceipt) {
    def pdfFile = restService.sendFacturaCommandWithAuth(paysheetReceipt, grailsApplication.config.modulus.paysheetReceiptGeneratePdf)
    if (!pdfFile) {
      throw new RestException("No se pudo generar el PDF del recibo de nómina") 
    }

    pdfFile
  }

}
