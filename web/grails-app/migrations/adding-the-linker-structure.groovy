databaseChangeLog = {

  changeSet(author: "makingdevs (generated)", id: "1490900691243-1") {
    createTable(tableName: "combination") {
      column(autoIncrement: "true", name: "id", type: "BIGINT") {
        constraints(primaryKey: "true", primaryKeyName: "combinationPK")
      }

      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "class_left", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }

      column(name: "class_right", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }

      column(name: "date_created", type: "datetime") {
        constraints(nullable: "false")
      }

      column(name: "last_updated", type: "datetime") {
        constraints(nullable: "false")
      }

      column(name: "right_instance_id", type: "BIGINT") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "makingdevs (generated)", id: "1490900691243-2") {
    createTable(tableName: "combination_link") {
      column(autoIncrement: "true", name: "id", type: "BIGINT") {
        constraints(primaryKey: "true", primaryKeyName: "combination_linkPK")
      }

      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "combination_id", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "instance_ref", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "type", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "makingdevs (generated)", id: "1490900691243-3") {
    addForeignKeyConstraint(baseColumnNames: "combination_id", baseTableName: "combination_link", constraintName: "FKrryphlpdlf6swyganswjtfhvs", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "combination")
  }

  changeSet(author: "makingdevs (generated)", id: "1490900691243-4") {
    addNotNullConstraint(columnDataType: "bigint", columnName: "business_entity_addresses_id", tableName: "business_entity_address")
  }

  changeSet(author: "makingdevs (generated)", id: "1490900691243-5") {
    addNotNullConstraint(columnDataType: "bigint", columnName: "business_entity_banks_accounts_id", tableName: "business_entity_bank_account")
  }

  changeSet(author: "makingdevs (generated)", id: "1490900691243-6") {
    addNotNullConstraint(columnDataType: "bigint", columnName: "cash_out_order_authorizations_id", tableName: "cash_out_order_authorization")
  }

  changeSet(author: "makingdevs (generated)", id: "1490900691243-7") {
    addNotNullConstraint(columnDataType: "bigint", columnName: "company_addresses_id", tableName: "company_address")
  }

  changeSet(author: "makingdevs (generated)", id: "1490900691243-8") {
    addNotNullConstraint(columnDataType: "bigint", columnName: "company_banks_accounts_id", tableName: "company_bank_account")
  }

  changeSet(author: "makingdevs (generated)", id: "1490900691243-9") {
    addNotNullConstraint(columnDataType: "bigint", columnName: "company_business_entities_id", tableName: "company_business_entity")
  }

  changeSet(author: "makingdevs (generated)", id: "1490900691243-10") {
    addNotNullConstraint(columnDataType: "bigint", columnName: "company_documents_id", tableName: "company_s3asset")
  }

  changeSet(author: "makingdevs (generated)", id: "1490900691243-11") {
    addNotNullConstraint(columnDataType: "bigint", columnName: "company_telephones_id", tableName: "company_telephone")
  }

  changeSet(author: "makingdevs (generated)", id: "1490900691243-12") {
    addNotNullConstraint(columnDataType: "bigint", columnName: "corporate_companies_id", tableName: "corporate_company")
  }

  changeSet(author: "makingdevs (generated)", id: "1490900691243-13") {
    addNotNullConstraint(columnDataType: "bigint", columnName: "corporate_users_id", tableName: "corporate_user")
  }

  changeSet(author: "makingdevs (generated)", id: "1490900691243-14") {
    addNotNullConstraint(columnDataType: "bigint", columnName: "deposit_order_authorizations_id", tableName: "deposit_order_authorization")
  }

  changeSet(author: "makingdevs (generated)", id: "1490900691243-15") {
    addNotNullConstraint(columnDataType: "bigint", columnName: "deposit_order_documents_id", tableName: "deposit_order_s3asset")
  }

  changeSet(author: "makingdevs (generated)", id: "1490900691243-16") {
    addNotNullConstraint(columnDataType: "bigint", columnName: "fees_receipt_authorizations_id", tableName: "fees_receipt_authorization")
  }

  changeSet(author: "makingdevs (generated)", id: "1490900691243-17") {
    addNotNullConstraint(columnDataType: "bigint", columnName: "fees_receipt_documents_id", tableName: "fees_receipt_s3asset")
  }

  changeSet(author: "makingdevs (generated)", id: "1490900691243-18") {
    addNotNullConstraint(columnDataType: "bigint", columnName: "group_notification_users_id", tableName: "group_notification_user")
  }

  changeSet(author: "makingdevs (generated)", id: "1490900691243-19") {
    addNotNullConstraint(columnDataType: "bigint", columnName: "loan_order_authorizations_id", tableName: "loan_order_authorization")
  }

  changeSet(author: "makingdevs (generated)", id: "1490900691243-20") {
    addNotNullConstraint(columnDataType: "bigint", columnName: "loan_order_documents_id", tableName: "loan_order_s3asset")
  }

  changeSet(author: "makingdevs (generated)", id: "1490900691243-21") {
    addNotNullConstraint(columnDataType: "bigint", columnName: "loan_payment_order_authorizations_id", tableName: "loan_payment_order_authorization")
  }

  changeSet(author: "makingdevs (generated)", id: "1490900691243-22") {
    addNotNullConstraint(columnDataType: "bigint", columnName: "profile_documents_id", tableName: "profile_s3asset")
  }

  changeSet(author: "makingdevs (generated)", id: "1490900691243-23") {
    addNotNullConstraint(columnDataType: "bigint", columnName: "profile_telephones_id", tableName: "profile_telephone")
  }

  changeSet(author: "makingdevs (generated)", id: "1490900691243-24") {
    addNotNullConstraint(columnDataType: "bigint", columnName: "purchase_order_authorizations_id", tableName: "purchase_order_authorization")
  }

  changeSet(author: "makingdevs (generated)", id: "1490900691243-25") {
    addNotNullConstraint(columnDataType: "bigint", columnName: "purchase_order_documents_id", tableName: "purchase_order_s3asset")
  }

  changeSet(author: "makingdevs (generated)", id: "1490900691243-26") {
    addNotNullConstraint(columnDataType: "bigint", columnName: "purchase_order_payments_id", tableName: "purchase_order_payment_to_purchase")
  }

  changeSet(author: "makingdevs (generated)", id: "1490900691243-27") {
    addNotNullConstraint(columnDataType: "bigint", columnName: "sale_order_addresses_id", tableName: "sale_order_address")
  }

  changeSet(author: "makingdevs (generated)", id: "1490900691243-28") {
    addNotNullConstraint(columnDataType: "bigint", columnName: "sale_order_authorizations_id", tableName: "sale_order_authorization")
  }

  changeSet(author: "makingdevs (generated)", id: "1490900691243-29") {
    addNotNullConstraint(columnDataType: "bigint", columnName: "sale_order_documents_id", tableName: "sale_order_s3asset")
  }

  changeSet(author: "makingdevs (generated)", id: "1490900691243-30") {
    addNotNullConstraint(columnDataType: "bigint", columnName: "transition_id", tableName: "transition_actions")
  }

  changeSet(author: "makingdevs (generated)", id: "1490900691243-31") {
    addNotNullConstraint(columnDataType: "bigint", columnName: "user_role_company_roles_id", tableName: "user_role_company_role")
  }
}
