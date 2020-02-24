databaseChangeLog = {

  changeSet(author: "sohjiro (generated)", id: "1574753590220-1") {
    createTable(tableName: "company_contact_information") {
      column(name: "company_contacts_id", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "contact_information_id", type: "BIGINT")
    }
  }

  changeSet(author: "sohjiro (generated)", id: "1574753590220-2") {
    createTable(tableName: "contact_information") {
      column(autoIncrement: "true", name: "id", type: "BIGINT") {
        constraints(primaryKey: "true", primaryKeyName: "contact_informationPK")
      }

      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "department", type: "VARCHAR(200)") {
        constraints(nullable: "false")
      }

      column(name: "name", type: "VARCHAR(100)") {
        constraints(nullable: "false")
      }

      column(name: "position", type: "VARCHAR(200)") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "sohjiro (generated)", id: "1574753590220-3") {
    createTable(tableName: "contact_information_email") {
      column(name: "contact_information_emails_id", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "email_id", type: "BIGINT")
    }
  }

  changeSet(author: "sohjiro (generated)", id: "1574753590220-4") {
    createTable(tableName: "contact_information_telephone") {
      column(name: "contact_information_telephones_id", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "telephone_id", type: "BIGINT")
    }
  }

  changeSet(author: "sohjiro (generated)", id: "1574753590220-5") {
    createTable(tableName: "email") {
      column(autoIncrement: "true", name: "id", type: "BIGINT") {
        constraints(primaryKey: "true", primaryKeyName: "emailPK")
      }

      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "address", type: "VARCHAR(200)") {
        constraints(nullable: "false")
      }

      column(name: "type", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "sohjiro (generated)", id: "1574753590220-6") {
    addForeignKeyConstraint(baseColumnNames: "company_contacts_id", baseTableName: "company_contact_information", constraintName: "FK2n4arbyia4a8e3ats2v0cb6cj", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "company")
  }

  changeSet(author: "sohjiro (generated)", id: "1574753590220-7") {
    addForeignKeyConstraint(baseColumnNames: "contact_information_id", baseTableName: "company_contact_information", constraintName: "FK7vqmgod3o20053qhvqojgdfby", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "contact_information")
  }

  changeSet(author: "sohjiro (generated)", id: "1574753590220-8") {
    addForeignKeyConstraint(baseColumnNames: "email_id", baseTableName: "contact_information_email", constraintName: "FK8ycsmtmkr7h06ayciv9ro14k2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "email")
  }

  changeSet(author: "sohjiro (generated)", id: "1574753590220-9") {
    addForeignKeyConstraint(baseColumnNames: "contact_information_emails_id", baseTableName: "contact_information_email", constraintName: "FKbitxdxexkvtscvxd2qswxhw6w", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "contact_information")
  }

  changeSet(author: "sohjiro (generated)", id: "1574753590220-10") {
    addForeignKeyConstraint(baseColumnNames: "telephone_id", baseTableName: "contact_information_telephone", constraintName: "FKixulop7b7e5ej1ot3pfj1ra7h", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "telephone")
  }

  changeSet(author: "sohjiro (generated)", id: "1574753590220-11") {
    addForeignKeyConstraint(baseColumnNames: "contact_information_telephones_id", baseTableName: "contact_information_telephone", constraintName: "FKv5r28r5cje2wkxq0g20xkjwc", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "contact_information")
  }
}
