databaseChangeLog = {

    changeSet(author: "tim (generated)", id: "1497361847513-1") {
        dropForeignKeyConstraint(baseTableName: "conciliation_commissions_invoice", constraintName: "FKeb2h1efutfyjbx53bus7gneu4")
    }

    changeSet(author: "tim (generated)", id: "1497361847513-2") {
        dropForeignKeyConstraint(baseTableName: "commissions_invoice_payment", constraintName: "FKj4xktc5wlhtu2bq4c88nklrnc")
    }

    changeSet(author: "tim (generated)", id: "1497361847513-3") {
        dropForeignKeyConstraint(baseTableName: "commissions_invoice", constraintName: "FKkcn2gvcuxjcmajb63nm4d99m7")
    }

    changeSet(author: "tim (generated)", id: "1497361847513-4") {
        dropForeignKeyConstraint(baseTableName: "conciliation_commissions_invoice", constraintName: "FKnrxpxyue0mb1etgc1vc5g8mr2")
    }

    changeSet(author: "tim (generated)", id: "1497361847513-5") {
        dropTable(tableName: "commissions_invoice")
    }

    changeSet(author: "tim (generated)", id: "1497361847513-6") {
        dropTable(tableName: "commissions_invoice_payment")
    }

    changeSet(author: "tim (generated)", id: "1497361847513-7") {
        dropTable(tableName: "conciliation_commissions_invoice")
    }

}
