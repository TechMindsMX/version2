package com.modulus.uno.paysheet

enum PaysheetSchema {
  SA_IAS("SA + IAS"),
  SA("SA"),
  IAS_FIJO("IAS Fijo"),
  IAS_VARIABLE("IAS Variable")

  private final String description

  PaysheetSchema(String description) {
    this.description = description
  }

  String toString() {
    this.description
  }

}
