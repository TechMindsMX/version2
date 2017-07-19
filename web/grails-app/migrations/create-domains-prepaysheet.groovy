databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1500485667048-2") {
        createTable(tableName: "pre_paysheet") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "pre_paysheetPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "account_executive", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "company_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "payment_period", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "paysheet_project", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "status", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tim (generated)", id: "1500485667048-3") {
        createTable(tableName: "pre_paysheet_employee") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "pre_paysheet_employeePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "account", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "bank_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "card_number", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "clabe", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "name_employee", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "net_payment", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "note", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "number_employee", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "pre_pay_sheet_id", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tim (generated)", id: "1500485667048-5") {
        addForeignKeyConstraint(baseColumnNames: "company_id", baseTableName: "pre_paysheet", constraintName: "FKaged9hw8yh0hvb39rec7pb7p", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "company")
    }

    changeSet(author: "tim (generated)", id: "1500485667048-6") {
        addForeignKeyConstraint(baseColumnNames: "pre_pay_sheet_id", baseTableName: "pre_paysheet_employee", constraintName: "FKja9awgcnkpe0bm75786k3v1nw", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "pre_paysheet")
    }

    changeSet(author: "tim (generated)", id: "1500485667048-7") {
        addForeignKeyConstraint(baseColumnNames: "bank_id", baseTableName: "pre_paysheet_employee", constraintName: "FKsle784g1de00x4vpg20tk4ir3", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "bank")
    }

}
