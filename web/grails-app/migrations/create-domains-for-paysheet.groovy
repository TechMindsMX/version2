databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1502143261147-1") {
        createTable(tableName: "breakdown_payment_employee") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "breakdown_payment_employeePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "base_quotation", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "disability_and_life", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "disability_and_life_employer", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "disease_and_maternity", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "disease_and_maternity_base", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "disease_and_maternity_employer", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "fixed_fee", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "infonavit", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "integrated_daily_salary", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "kindergarten", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "loan", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "loan_employer", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "occupational_risk", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "pension", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "pension_employer", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "retirement_saving", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "unemployment_and_eld", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "unemployment_and_eld_employer", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tim (generated)", id: "1502143261147-2") {
        createTable(tableName: "paysheet") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "paysheetPK")
            }

            column(name: "version", type: "BIGINT") {
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

            column(name: "pre_paysheet_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "status", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tim (generated)", id: "1502143261147-3") {
        createTable(tableName: "paysheet_employee") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "paysheet_employeePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "breakdown_payment_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "commission", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "income_tax", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "iva_rate", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "paysheet_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "paysheet_tax", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "pre_paysheet_employee_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "salary_assimilable", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "salary_imss", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "social_quota", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "social_quota_employer", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "status", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "subsidy_salary", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "tim (generated)", id: "1502143261147-4") {
        addForeignKeyConstraint(baseColumnNames: "pre_paysheet_id", baseTableName: "paysheet", constraintName: "FK6xaw16f3m6mk55jsjgrpfsrnb", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "pre_paysheet")
    }

    changeSet(author: "tim (generated)", id: "1502143261147-5") {
        addForeignKeyConstraint(baseColumnNames: "paysheet_id", baseTableName: "paysheet_employee", constraintName: "FK7u4hsrsm71kt3mykmyt6wurfy", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "paysheet")
    }

    changeSet(author: "tim (generated)", id: "1502143261147-6") {
        addForeignKeyConstraint(baseColumnNames: "pre_paysheet_employee_id", baseTableName: "paysheet_employee", constraintName: "FKbh3t5ism7g2p76f4hde7mb7k9", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "pre_paysheet_employee")
    }

    changeSet(author: "tim (generated)", id: "1502143261147-7") {
        addForeignKeyConstraint(baseColumnNames: "company_id", baseTableName: "paysheet", constraintName: "FKedbi45b914pvpib0q2fyo7iu", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "company")
    }

    changeSet(author: "tim (generated)", id: "1502143261147-8") {
        addForeignKeyConstraint(baseColumnNames: "breakdown_payment_id", baseTableName: "paysheet_employee", constraintName: "FKly21d8fapv0c6g843h9d6db08", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "breakdown_payment_employee")
    }

}
