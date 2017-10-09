databaseChangeLog = {

    changeSet(author: "makingdevs (generated)", id: "1507580451479-1") {
        createTable(tableName: "menu_menu") {
            column(name: "menu_menus_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "menu_id", type: "BIGINT")
        }
    }

    changeSet(author: "makingdevs (generated)", id: "1507580451479-2") {
        addForeignKeyConstraint(baseColumnNames: "menu_id", baseTableName: "menu_menu", constraintName: "FK3cg9sxpw8wctsrvb48eii23rv", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "menu")
    }

    changeSet(author: "makingdevs (generated)", id: "1507580451479-3") {
        addForeignKeyConstraint(baseColumnNames: "menu_menus_id", baseTableName: "menu_menu", constraintName: "FKb457ek6bmm3wqb8pxxsvtw3os", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "menu")
    }

    changeSet(author: "makingdevs (generated)", id: "1507580451479-4") {
        dropForeignKeyConstraint(baseTableName: "menu", constraintName: "FKht6h4dvumr09qxk95r1qcgjd8")
    }

    changeSet(author: "makingdevs (generated)", id: "1507580451479-5") {
        dropColumn(columnName: "parent_menu_id", tableName: "menu")
    }

    changeSet(author: "makingdevs (generated)", id: "1507580451479-6") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "business_entity_addresses_id", tableName: "business_entity_address")
    }

    changeSet(author: "makingdevs (generated)", id: "1507580451479-7") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "business_entity_banks_accounts_id", tableName: "business_entity_bank_account")
    }

    changeSet(author: "makingdevs (generated)", id: "1507580451479-8") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "cash_out_order_authorizations_id", tableName: "cash_out_order_authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1507580451479-9") {
        addNotNullConstraint(columnDataType: "decimal(19,2)", columnName: "commission", tableName: "paysheet_project")
    }

    changeSet(author: "makingdevs (generated)", id: "1507580451479-10") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "company_addresses_id", tableName: "company_address")
    }

    changeSet(author: "makingdevs (generated)", id: "1507580451479-11") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "company_banks_accounts_id", tableName: "company_bank_account")
    }

    changeSet(author: "makingdevs (generated)", id: "1507580451479-12") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "company_business_entities_id", tableName: "company_business_entity")
    }

    changeSet(author: "makingdevs (generated)", id: "1507580451479-13") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "company_documents_id", tableName: "company_s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1507580451479-14") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "company_telephones_id", tableName: "company_telephone")
    }

    changeSet(author: "makingdevs (generated)", id: "1507580451479-15") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "corporate_companies_id", tableName: "corporate_company")
    }

    changeSet(author: "makingdevs (generated)", id: "1507580451479-16") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "corporate_users_id", tableName: "corporate_user")
    }

    changeSet(author: "makingdevs (generated)", id: "1507580451479-17") {
        addNotNullConstraint(columnDataType: "datetime", columnName: "date_created", tableName: "fees_receipt")
    }

    changeSet(author: "makingdevs (generated)", id: "1507580451479-18") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "fees_receipt_authorizations_id", tableName: "fees_receipt_authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1507580451479-19") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "fees_receipt_documents_id", tableName: "fees_receipt_s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1507580451479-20") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "group_notification_users_id", tableName: "group_notification_user")
    }

    changeSet(author: "makingdevs (generated)", id: "1507580451479-21") {
        addNotNullConstraint(columnDataType: "datetime", columnName: "last_updated", tableName: "fees_receipt")
    }

    changeSet(author: "makingdevs (generated)", id: "1507580451479-22") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "loan_order_authorizations_id", tableName: "loan_order_authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1507580451479-23") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "loan_order_documents_id", tableName: "loan_order_s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1507580451479-24") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "loan_payment_order_authorizations_id", tableName: "loan_payment_order_authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1507580451479-25") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "profile_documents_id", tableName: "profile_s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1507580451479-26") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "profile_telephones_id", tableName: "profile_telephone")
    }

    changeSet(author: "makingdevs (generated)", id: "1507580451479-27") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "purchase_order_authorizations_id", tableName: "purchase_order_authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1507580451479-28") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "purchase_order_documents_id", tableName: "purchase_order_s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1507580451479-29") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "purchase_order_payments_id", tableName: "purchase_order_payment_to_purchase")
    }

    changeSet(author: "makingdevs (generated)", id: "1507580451479-30") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "sale_order_addresses_id", tableName: "sale_order_address")
    }

    changeSet(author: "makingdevs (generated)", id: "1507580451479-31") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "sale_order_authorizations_id", tableName: "sale_order_authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1507580451479-32") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "sale_order_documents_id", tableName: "sale_order_s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1507580451479-33") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "transition_id", tableName: "transition_actions")
    }

    changeSet(author: "makingdevs (generated)", id: "1507580451479-34") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "user_role_company_roles_id", tableName: "user_role_company_role")
    }
}
