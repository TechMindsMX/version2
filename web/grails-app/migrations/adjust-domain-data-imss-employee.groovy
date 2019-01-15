databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1547511925058-1") {
        addColumn(tableName: "data_imss_employee") {
            column(name: "monthly_net_assimilable_salary", type: "decimal(19, 2)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1547511925058-2") {
        addColumn(tableName: "data_imss_employee") {
            column(name: "paysheet_schema", type: "varchar(255)")
        }
    }

    changeSet(author: "temoc (generated)", id: "1547511925058-27") {
        dropNotNullConstraint(columnDataType: "varchar(255)", columnName: "department", tableName: "data_imss_employee")
    }

    changeSet(author: "temoc (generated)", id: "1547511925058-30") {
        dropNotNullConstraint(columnDataType: "varchar(255)", columnName: "job", tableName: "data_imss_employee")
    }

    changeSet(author: "temoc (generated)", id: "1547511925058-35") {
        dropNotNullConstraint(columnDataType: "varchar(255)", columnName: "work_day_type", tableName: "data_imss_employee")
    }
}
