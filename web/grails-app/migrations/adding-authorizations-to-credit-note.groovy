databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1526076507321-1") {
        createTable(tableName: "credit_note_authorization") {
            column(name: "credit_note_authorizations_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "authorization_id", type: "BIGINT")
        }
    }

    changeSet(author: "temoc (generated)", id: "1526076507321-2") {
        addForeignKeyConstraint(baseColumnNames: "credit_note_authorizations_id", baseTableName: "credit_note_authorization", constraintName: "FK9l6b3whmla29iaubipabpphru", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "credit_note")
    }

    changeSet(author: "temoc (generated)", id: "1526076507321-3") {
        addForeignKeyConstraint(baseColumnNames: "authorization_id", baseTableName: "credit_note_authorization", constraintName: "FKnw7a5dqstxtyo34w0lu6j12ld", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "authorization")
    }
}
