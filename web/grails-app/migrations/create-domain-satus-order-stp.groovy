databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1498233619167-1") {
        createTable(tableName: "status_order_stp") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "status_order_stpPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "cause_refund", type: "INT") {
                constraints(nullable: "false")
            }

            column(name: "company", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "key_transaction", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "status", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "tracking_key", type: "VARCHAR(255)")
        }
    }

}
