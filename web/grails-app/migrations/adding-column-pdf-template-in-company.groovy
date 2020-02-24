databaseChangeLog = {
  changeSet(author: "karla (manual)", id: "adding-column-pdf-template-in-company") {
    addColumn(tableName: "company"){
      column(name: "pdf_template", type: "varchar(25)")
    }
  }
}