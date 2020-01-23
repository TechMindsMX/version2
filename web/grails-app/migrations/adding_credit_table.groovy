databaseChangeLog = {

  changeSet(author: "sohjiro (generated)", id: "1579495679863-1") {
    createTable(tableName: "credit") {
      column(autoIncrement: "true", name: "id", type: "BIGINT") {
        constraints(primaryKey: "true", primaryKeyName: "creditPK")
      }

      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "company_id", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "credit_line_type", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }

      column(name: "dispersion_type", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }

      column(name: "enabled", type: "BIT") {
        constraints(nullable: "false")
      }

      column(name: "frequency_type", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }

      column(name: "name", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }

      column(name: "portfolio_management_type", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }

      column(name: "product_type", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "sohjiro (generated)", id: "1579495679863-2") {
    addForeignKeyConstraint(baseColumnNames: "company_id", baseTableName: "credit", constraintName: "FKc6dio2oa3wy7ptj4xykut6062", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "company")
  }
}
