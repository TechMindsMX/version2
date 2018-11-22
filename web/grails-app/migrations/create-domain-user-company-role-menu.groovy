databaseChangeLog = {

    changeSet(author: "temoc (generated)", id: "1538415278409-1") {
        createTable(tableName: "user_role_company_menu") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "user_role_company_menuPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "company_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "role_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "user_id", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "temoc (generated)", id: "1538415278409-2") {
        createTable(tableName: "user_role_company_menu_menu") {
            column(name: "user_role_company_menu_menus_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "menu_id", type: "BIGINT")
        }
    }

    changeSet(author: "temoc (generated)", id: "1538415278409-3") {
        addForeignKeyConstraint(baseColumnNames: "role_id", baseTableName: "user_role_company_menu", constraintName: "FK3btxxrf7fy9f6k608kyqrs47j", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "role")
    }

    changeSet(author: "temoc (generated)", id: "1538415278409-4") {
        addForeignKeyConstraint(baseColumnNames: "menu_id", baseTableName: "user_role_company_menu_menu", constraintName: "FK5wjndvr92h3ohk7pb357l7gp", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "menu")
    }

    changeSet(author: "temoc (generated)", id: "1538415278409-5") {
        addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "user_role_company_menu", constraintName: "FK99nsq2u1fu7ch160sul4yt4sd", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user")
    }

    changeSet(author: "temoc (generated)", id: "1538415278409-6") {
        addForeignKeyConstraint(baseColumnNames: "company_id", baseTableName: "user_role_company_menu", constraintName: "FKaomf0faictsna07kwt78icfmv", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "company")
    }

    changeSet(author: "temoc (generated)", id: "1538415278409-7") {
        addForeignKeyConstraint(baseColumnNames: "user_role_company_menu_menus_id", baseTableName: "user_role_company_menu_menu", constraintName: "FKnx0u4nb65h4xcsjwe8ac618ec", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user_role_company_menu")
    }

}
