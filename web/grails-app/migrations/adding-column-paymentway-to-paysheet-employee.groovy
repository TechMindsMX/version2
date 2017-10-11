
databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1507310079995-1") {
        addColumn(tableName: "paysheet_employee") {
            column(name: "payment_way", type: "varchar(255)") {
                constraints(nullable: "false")
            }
        }
    }

		changeSet(author: "temoc (manual)", id: "initialize-payment-way-into-paysheet-employee") {
			grailsChange {
				change {
					sql.execute("update paysheet_employee set payment_way='CASH' where pre_paysheet_employee_id in (select id from pre_paysheet_employee where bank_id is null)")
					sql.execute("update paysheet_employee set payment_way='BANKING' where pre_paysheet_employee_id in (select id from pre_paysheet_employee where bank_id is not null)")
				}
			}
		}

}
