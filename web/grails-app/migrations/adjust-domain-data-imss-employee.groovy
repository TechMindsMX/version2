databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1541785084905-1") {
        addColumn(tableName: "data_imss_employee") {
            column(name: "is_only_assimilable", type: "bit") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1541785084905-2") {
        addColumn(tableName: "data_imss_employee") {
            column(name: "is_variable_assimilable", type: "bit") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1541785084905-3") {
        addColumn(tableName: "data_imss_employee") {
            column(name: "monthly_net_assimilable_salary", type: "decimal(19, 2)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1541785084905-28") {
        dropNotNullConstraint(columnDataType: "int", columnName: "annual_bonus_days", tableName: "data_imss_employee")
    }

    changeSet(author: "temoc (generated)", id: "1541785084905-30") {
        dropNotNullConstraint(columnDataType: "decimal(5,2)", columnName: "holiday_bonus_rate", tableName: "data_imss_employee")
    }

    changeSet(author: "temoc (generated)", id: "1541785084905-32") {
        dropNotNullConstraint(columnDataType: "varchar(255)", columnName: "job_risk", tableName: "data_imss_employee")
    }

    changeSet(author: "temoc (generated)", id: "1541785084905-35") {
        dropNotNullConstraint(columnDataType: "varchar(255)", columnName: "nss", tableName: "data_imss_employee")
    }

    changeSet(author: "temoc (generated)", id: "1541785084905-38") {
        dropNotNullConstraint(columnDataType: "datetime", columnName: "registration_date", tableName: "data_imss_employee")
    }
}
