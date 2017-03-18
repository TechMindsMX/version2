databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1489796434457-1") {
        createTable(tableName: "conciliation") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "conciliationPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "amount", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "change_type", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "comment", type: "VARCHAR(255)")

            column(name: "company_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "payment_id", type: "BIGINT")

            column(name: "sale_order_id", type: "BIGINT")

            column(name: "status", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "user_id", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tim (generated)", id: "1489796434457-3") {
        addForeignKeyConstraint(baseColumnNames: "company_id", baseTableName: "conciliation", constraintName: "FK6r7ncouvhn19acm966xkhve0v", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "company")
    }

    changeSet(author: "tim (generated)", id: "1489796434457-4") {
        addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "conciliation", constraintName: "FKalc2br1vp6dlxkoxav5ihrot1", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user")
    }

    changeSet(author: "tim (generated)", id: "1489796434457-5") {
        addForeignKeyConstraint(baseColumnNames: "payment_id", baseTableName: "conciliation", constraintName: "FKj3lmtf83iehup9xfj3eahu88m", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "payment")
    }

    changeSet(author: "tim (generated)", id: "1489796434457-6") {
        addForeignKeyConstraint(baseColumnNames: "sale_order_id", baseTableName: "conciliation", constraintName: "FKocpmj8v0mkrcjegkeinltjst9", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "sale_order")
    }

}
