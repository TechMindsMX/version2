databaseChangeLog = {

    changeSet(author: "makingdevs (generated)", id: "1500410536615-1") {
        createTable(tableName: "menu_operation") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "menu_operationPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "internal_url", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-2") {
        createTable(tableName: "paymentm1emitter") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "paymentm1emitterPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "amount", type: "DECIMAL(19, 2)") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "status", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-3") {
        addColumn(tableName: "sale_order_item") {
            column(name: "ieps", type: "decimal(5, 2)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-4") {
        dropForeignKeyConstraint(baseTableName: "deposit_order_s3asset", constraintName: "FK_78mxqus4c33f8agef1720orat")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-5") {
        dropForeignKeyConstraint(baseTableName: "deposit_order_s3asset", constraintName: "FK_fc4511gyg27b7fsbnh548xidq")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-6") {
        dropForeignKeyConstraint(baseTableName: "deposit_order_authorization", constraintName: "FK_gd2dhyoj1fmwk484482lnquso")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-7") {
        dropForeignKeyConstraint(baseTableName: "deposit_order_authorization", constraintName: "FK_lfljxhh8n5pik85wl02g2cuj4")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-8") {
        dropForeignKeyConstraint(baseTableName: "deposit_order", constraintName: "FK_qxtq9xmyhwqmc3b2o4ty4ge8g")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-9") {
        dropForeignKeyConstraint(baseTableName: "account_statement_bank_account", constraintName: "FKh15dp9ub23f53a0953t7adeeo")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-10") {
        dropForeignKeyConstraint(baseTableName: "account_statement_bank_account", constraintName: "FKmv7rlb2b4crwf2y1e7k34w1gd")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-11") {
        dropForeignKeyConstraint(baseTableName: "data_imss_employee", constraintName: "FKpdxcdux5fcfcek3bc12uh0869")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-12") {
        dropForeignKeyConstraint(baseTableName: "paysheet_project", constraintName: "FKsd5mydsmyv1a5nmem2vlxjhrk")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-13") {
        dropUniqueConstraint(constraintName: "UC_DATAIMSSEMPLOYEE_NSS", tableName: "data_imss_employee")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-14") {
        dropTable(tableName: "account_statement_bank_account")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-15") {
        dropTable(tableName: "data_imss_employee")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-16") {
        dropTable(tableName: "deposit_order")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-17") {
        dropTable(tableName: "deposit_order_authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-18") {
        dropTable(tableName: "deposit_order_s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-19") {
        dropTable(tableName: "paysheet_project")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-20") {
        dropTable(tableName: "status_order_stp")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-21") {
        dropColumn(columnName: "card_number", tableName: "bank_account")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-22") {
        dropColumn(columnName: "iva_retention", tableName: "sale_order_item")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-23") {
        dropColumn(columnName: "number", tableName: "employee_link")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-24") {
        dropColumn(columnName: "status", tableName: "business_entity")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-25") {
        dropColumn(columnName: "status", tableName: "payment_to_purchase")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-26") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "business_entity_addresses_id", tableName: "business_entity_address")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-27") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "business_entity_banks_accounts_id", tableName: "business_entity_bank_account")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-28") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "cash_out_order_authorizations_id", tableName: "cash_out_order_authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-29") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "company_addresses_id", tableName: "company_address")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-30") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "company_banks_accounts_id", tableName: "company_bank_account")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-31") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "company_business_entities_id", tableName: "company_business_entity")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-32") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "company_documents_id", tableName: "company_s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-33") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "company_telephones_id", tableName: "company_telephone")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-34") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "corporate_companies_id", tableName: "corporate_company")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-35") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "corporate_users_id", tableName: "corporate_user")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-36") {
        addNotNullConstraint(columnDataType: "datetime", columnName: "date_created", tableName: "fees_receipt")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-37") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "fees_receipt_authorizations_id", tableName: "fees_receipt_authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-38") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "fees_receipt_documents_id", tableName: "fees_receipt_s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-39") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "group_notification_users_id", tableName: "group_notification_user")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-40") {
        addNotNullConstraint(columnDataType: "datetime", columnName: "last_updated", tableName: "fees_receipt")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-41") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "loan_order_authorizations_id", tableName: "loan_order_authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-42") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "loan_order_documents_id", tableName: "loan_order_s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-43") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "loan_payment_order_authorizations_id", tableName: "loan_payment_order_authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-44") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "profile_documents_id", tableName: "profile_s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-45") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "profile_telephones_id", tableName: "profile_telephone")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-46") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "purchase_order_authorizations_id", tableName: "purchase_order_authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-47") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "purchase_order_documents_id", tableName: "purchase_order_s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-48") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "purchase_order_payments_id", tableName: "purchase_order_payment_to_purchase")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-49") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "sale_order_addresses_id", tableName: "sale_order_address")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-50") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "sale_order_authorizations_id", tableName: "sale_order_authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-51") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "sale_order_documents_id", tableName: "sale_order_s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-52") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "transition_id", tableName: "transition_actions")
    }

    changeSet(author: "makingdevs (generated)", id: "1500410536615-53") {
        addNotNullConstraint(columnDataType: "bigint", columnName: "user_role_company_roles_id", tableName: "user_role_company_role")
    }
}
