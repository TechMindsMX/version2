databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1525812810068-1") {
        createTable(tableName: "credit_note_item") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "credit_note_itemPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "credit_note_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "currency_type", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "discount", type: "DECIMAL(5, 2)") {
                constraints(nullable: "false")
            }

            column(name: "iva", type: "DECIMAL(5, 2)") {
                constraints(nullable: "false")
            }

            column(name: "iva_retention", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "name", type: "VARCHAR(300)") {
                constraints(nullable: "false")
            }

            column(name: "price", type: "DECIMAL(11, 2)") {
                constraints(nullable: "false")
            }

            column(name: "quantity", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "sat_key", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "sku", type: "VARCHAR(50)") {
                constraints(nullable: "false")
            }

            column(name: "unit_type", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1525812810068-2") {
        addForeignKeyConstraint(baseColumnNames: "credit_note_id", baseTableName: "credit_note_item", constraintName: "FKndm4383s2ffg3xdulf1ma8rha", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "credit_note")
    }
}
