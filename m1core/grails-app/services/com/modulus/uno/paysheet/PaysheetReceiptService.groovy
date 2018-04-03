package com.modulus.uno.paysheet

import com.modulus.uno.invoice.paysheetReceipt.*
import com.modulus.uno.invoice.*

import com.modulus.uno.DataImssEmployee
import com.modulus.uno.EmployeeLink
import com.modulus.uno.AddressType

import com.modulus.uno.DataImssEmployeeService

class PaysheetReceiptService {

  PaysheetProjectService paysheetProjectService
  DataImssEmployeeService dataImssEmployeeService

  PaysheetReceipt createPaysheetReceiptFromPaysheetEmployeeForSchema(PaysheetEmployee paysheetEmployee, PaymentSchema schema) {
    PaysheetReceipt paysheetReceipt = new PaysheetReceipt(
      datosDeFacturacion: createInvoiceDataFromPaysheetEmployee(paysheetEmployee),
      emisor: createEmitterFromPaysheetEmployeeAndSchema(paysheetEmployee, schema),
      receptor: createReceiverFromPaysheetEmployeeAndSchema(paysheetEmployee, schema),
      nomina: createPaysheetDataFromPaysheetEmployeeAndSchema(paysheetEmployee, schema),
      esquema: schema.toString(),
      id: paysheetEmployee.paysheet.paysheetContract.company
    )
    paysheetReceipt.concepto = createConceptForPaysheetEmployee(paysheetReceipt)
    paysheetReceipt
  }

  DatosDeFacturacion createInvoiceDataFromPaysheetEmployee(PaysheetEmployee paysheetEmployee) {
    new DatosDeFacturacion(
      folio: paysheetEmployee.paysheet.paysheetContract.nextFolio,
      serie: paysheetEmployee.paysheet.paysheetContract.serie
    )
  }

  Contribuyente createEmitterFromPaysheetEmployee(paysheetEmployee, PaymentSchema schema) {
    PaysheetProject paysheetProject = paysheetProjectService.getPaysheetProjectByPaysheetContractAndName(paysheetEmployee.paysheet.paysheetContract, paysheetEmployee.paysheet.prePaysheet.paysheetProject)
    PayerPaysheetProject payer = paysheetProject.payers.find { payer -> payer.paymentSchema == schema }
    new Contribuyente (
      registroPatronal: paysheetEmployee.paysheet.paysheetContract.employerRegistration,
      datosFiscales: new DatosFiscales (
        razonSocial: payer.company.bussinessName,
        rfc: payer.company.rfc,
        codigoPostal: payer.company.addresses.find { address -> address.addressType == AddressType.FISCAL }.zipCode
      )
    )
  }

  Empleado createReceiverFromPaysheetEmployeeAndSchema(PaysheetEmployee paysheetEmployee, PaymentSchema schema) {
    new Empleado(
      rfc: paysheetEmployee.prePaysheetEmployee.rfc,
      nombre: paysheetEmployee.prePaysheetEmployee.nameEmployee,
      curp: paysheetEmployee.prePaysheetEmployee.curp,
      datosBancarios: new DatosBancarios(banco:paysheetEmployee.prePaysheetEmployee.bank.bankingCode.substring(2,5), cuenta:paysheetEmployee.prePaysheetEmployee.account),
      datosLaborales: createJobDataFromPaysheetEmployeeAndSchema(paysheetEmployee, schema)
    )
  }

  DatosLaborales createJobDataFromPaysheetEmployeeAndSchema(PaysheetEmployee paysheetEmployee, PaymentSchema paymentSchema) {
    PaysheetProject paysheetProject = paysheetProjectService.getPaysheetProjectByPaysheetContractAndName(paysheetEmployee.paysheet.paysheetContract, paysheetEmployee.paysheet.prePaysheet.paysheetProject)
    EmployeeLink employeeLink = EmployeeLink.findByCompanyAndEmployeeRef(paysheetEmployee.paysheet.paysheetContract.company, paysheetEmployee.prePaysheetEmployee.rfc)
    DataImssEmployee dataImssEmployee = DataImssEmployee.findByEmployee(employeeLink)
    new DatosLaborales (
      entidad: paysheetProject.federalEntity,
      noEmpleado: paysheetEmployee.prePaysheetEmployee.numberEmployee,
      tipoContrato: paymentSchema == PaymentSchema.IMSS ? dataImssEmployee.contractType.key : ContractType.WORK_WITHOUT_RELATION.key,
      periodoPago: dataImssEmployee.paymentPeriod.key,
      tipoRegimen: paymentSchema == PaymentSchema.IMSS ? dataImssEmployee.regimeType.key : RegimeType.FEES_ASSIMILATED.key,
      tipoJornada: dataImssEmployee.workDayType.key,
      datosImss: new DatosImss (
        antiguedad: dataImssEmployeeService.calculateLaborOldInSATFormat(dataImssEmployee),
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
    new DetalleNomina(clave: PerceptionType.P001.name(), descripcion: PerceptionType.P001.description, tipo: PerceptionType.P001.key, importeExento: new BigDecimal(0), importeGravado: paysheetEmployee.salaryImss)
  }

  DetalleNomina createSalaryDetailForSchemaASSIMILABLE(PaysheetEmployee paysheetEmployee) {
      new DetalleNomina(clave: PerceptionType.P046.name(), descripcion: PerceptionType.P046.description, tipo: PerceptionType.P046.key, importeExento: new BigDecimal(0), importeGravado: paysheetEmployee.crudeAssimilable)
  }

  List<DetalleNomina> addPerceptionIncidenceForSchema(PaysheetEmployee paysheetEmployee, PaymentSchema schema) {
    def incidences = paysheetEmployee.prePaysheetEmployee.incidences.find { incidence -> incidence.type == IncidenceType.PERCEPTION && incidence.keyType != PerceptionType.P001.key && incidence.keyType != PerceptionType.P046.key && paymentSchema == schema }
    List<DetalleNomina> perceptionIncidences = []
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
    perceptionIncidences    
  }

  Deducciones createDeductionsFromPaysheetEmployeeAndSchema(PaysheetEmployee paysheetEmployee, PaymentSchema schema) {
    Deducciones deducciones = new Percepciones(detalles:[])
    deducciones.detalles.add("createDeductionDetailForSchema${schema.name()}"(paysheetEmployee))
    deducciones.detalles.addAll(addDeductionIncidenceForSchema(paysheetEmployee, schema))
    deducciones
  }

  DetalleNomina createDeductionDetailForSchemaIMSS(PaysheetEmployee paysheetEmployee) {
    if ((paysheetEmployee.subsidySalary - paysheetEmployee.incomeTax) > 0) {
      new DetalleNomina(clave: DeductionType.D002.name(), descripcion: DeductionType.D002.description, tipo: DeductionType.D002.key, importeExento: new BigDecimal(0), importeGravado: paysheetEmployee.subsidySalary - paysheetEmployee.incomeTax)
    }
  }

  DetalleNomina createDeductionDetailForSchemaASSIMILABLE(PaysheetEmployee paysheetEmployee) {
      new DetalleNomina(clave: DeductionType.D002.name(), descripcion: DeductionType.D002.description, tipo: DeductionType.D002.key, importeExento: new BigDecimal(0), importeGravado: paysheetEmployee.incomeTaxAssimilable)
  }

  List<DetalleNomina> addDeductionIncidenceForSchema(PaysheetEmployee paysheetEmployee, PaymentSchema schema) {
    def incidences = paysheetEmployee.prePaysheetEmployee.incidences.find { incidence -> incidence.type == IncidenceType.DEDUCTION  && incidence.keyType != DeductionType.D002.key && paymentSchema == schema }
    List<DetalleNomina> deductionIncidences = []
    incidences.each { incidence -> 
      DetalleNomina detalle = new DetalleNomina(clave: incidence.internalKey, descripcion: incidence.description, tipo: incidence.keyType, importeExento: incidence.exemptAmount, importeGravado: incidence.taxedAmount)
      deductionIncidences.add(detalle)
    }
    deductionIncidences    
  }

  List<DetalleNomina> createOtherPerceptionsFromPaysheetEmployeeAndSchema(PaysheetEmployee paysheetEmployee, PaymentSchema schema) {
    def incidences = paysheetEmployee.prePaysheetEmployee.incidences.find { incidence -> incidence.type == IncidenceType.OTHER_PERCEPTION && paymentSchema == schema }
    List<DetalleNomina> otherPerceptionIncidences = []
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
    otherPerceptionIncidences    
  }


  Concepto createConceptForPaysheetEmployee(PaysheetReceipt paysheetReceipt) {
    new Concepto (
      valorUnitario: paysheetReceipt.nomina.percepciones.detalles*.importeExento.sum() + paysheetReceipt.nomina.percepciones.detalles*.importeGravado.sum() + paysheetReceipt.nomina.otrosPagos*.importeExento.sum() + paysheetReceipt.nomina.otrosPagos*.importeGravado.sum(),
      descuento: paysheetReceipt.nomina.deducciones.detalles*.importeExento.sum() + paysheetReceipt.nomina.deducciones.detalles*.importeGravado.sum()
    )
  }
}
