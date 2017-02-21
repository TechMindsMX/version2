databaseChangeLog = {

    changeSet(author: "makingdevs (generated)", id: "1487714352840-1") {
        createTable(tableName: "action") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "actionPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "datetime") {
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

    changeSet(author: "makingdevs (generated)", id: "1487714352840-2") {
        createTable(tableName: "log_record") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "log_recordPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "current_state_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "tracking_log_link_id", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-3") {
        createTable(tableName: "machine") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "machinePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "initial_state_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "datetime") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-4") {
        createTable(tableName: "machinery_link") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "machinery_linkPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "machine_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "machinery_ref", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "type", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-5") {
        createTable(tableName: "state") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "statePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "final_state", type: "BIT") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "machine_id", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-6") {
        createTable(tableName: "tracking_log_link") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "tracking_log_linkPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "tracking_ref", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "type", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-7") {
        createTable(tableName: "transition") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "transitionPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "action_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "datetime") {
                constraints(nullable: "false")
            }

            column(name: "state_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "state_to_id", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-8") {
        addForeignKeyConstraint(baseColumnNames: "state_to_id", baseTableName: "transition", constraintName: "FK_1koe0yhvkysc02wsx7nx8f3y0", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "state")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-9") {
        addForeignKeyConstraint(baseColumnNames: "state_id", baseTableName: "transition", constraintName: "FK_5gxuh0fyamf2txaxrr76wdfyi", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "state")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-10") {
        addForeignKeyConstraint(baseColumnNames: "tracking_log_link_id", baseTableName: "log_record", constraintName: "FK_epvhqrdg99srwcq3si46re2ii", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "tracking_log_link")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-11") {
        addForeignKeyConstraint(baseColumnNames: "initial_state_id", baseTableName: "machine", constraintName: "FK_gq016d8dojn6jcek1sg7gopd4", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "state")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-12") {
        addForeignKeyConstraint(baseColumnNames: "machine_id", baseTableName: "state", constraintName: "FK_jvls2o1bhaoy2wemcue85pwqv", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "machine")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-13") {
        addForeignKeyConstraint(baseColumnNames: "machine_id", baseTableName: "machinery_link", constraintName: "FK_l2aauyt0wvy7w1vyccexmlyg3", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "machine")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-14") {
        addForeignKeyConstraint(baseColumnNames: "action_id", baseTableName: "transition", constraintName: "FK_mmuwp8m7nxxn8ya5je65yhslp", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "action")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-15") {
        addForeignKeyConstraint(baseColumnNames: "current_state_id", baseTableName: "log_record", constraintName: "FK_ppne1f2e71fj5in1mtoad9206", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "state")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-16") {
        dropIndex(indexName: "FK_14gx7m1jy60c780oe6uycmfug", tableName: "sale_order_s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-17") {
        dropIndex(indexName: "FK_18m22ybal0ga91hy3s5l66bul", tableName: "fees_receipt_s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-18") {
        dropIndex(indexName: "FK_1mcjtpxmwom9h9bf2q0k412e0", tableName: "user")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-19") {
        dropIndex(indexName: "FK_26aw28fqp8nv9kd2n9kug1ate", tableName: "payment")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-20") {
        dropIndex(indexName: "FK_29tfk85uscogyuyhnmogjstyq", tableName: "sale_order_authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-21") {
        dropIndex(indexName: "FK_2b7qu5poo9sv9mtyuw9xhkcl2", tableName: "payment")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-22") {
        dropIndex(indexName: "FK_2hoido30po6klvme7xsojbcib", tableName: "purchase_order_payment_to_purchase")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-23") {
        dropIndex(indexName: "FK_3f85cq7on905fe0rwqwbvinnn", tableName: "fees_receipt_authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-24") {
        dropIndex(indexName: "FK_3y480lpgswma3dlahkas0piv2", tableName: "employee_link")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-25") {
        dropIndex(indexName: "FK_5a9nmc0j5rhmo268sm9f08lef", tableName: "purchase_order_authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-26") {
        dropIndex(indexName: "FK_5b91qa29nyutp6sh2fo4x4tn6", tableName: "cash_out_order_authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-27") {
        dropIndex(indexName: "FK_5iomp5vtj1eh4ykkyf02cd808", tableName: "company_business_entity")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-28") {
        dropIndex(indexName: "FK_5jo2bsgk40m6yjjd587bdvp2k", tableName: "sale_order_item")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-29") {
        dropIndex(indexName: "FK_64uboislmwsbb72nj98pwjt06", tableName: "fees_receipt")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-30") {
        dropIndex(indexName: "FK_6aaj211p0i2g5rjxve1679j1c", tableName: "corporate_user")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-31") {
        dropIndex(indexName: "FK_78mxqus4c33f8agef1720orat", tableName: "deposit_order_s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-32") {
        dropIndex(indexName: "FK_7js7ru525ir9t0f0atxgr143u", tableName: "cash_out_order_authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-33") {
        dropIndex(indexName: "FK_7khskpewos3jrxg9umn6n6mj5", tableName: "user_role_company")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-34") {
        dropIndex(indexName: "FK_7s9iidg2nghqx5vm8ufivle2f", tableName: "loan_order_authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-35") {
        dropIndex(indexName: "FK_8vi2muat59yyggpgqvqpum19o", tableName: "loan_order_s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-36") {
        dropIndex(indexName: "FK_97miav6mf441scxegv4yeahxn", tableName: "corporate_company")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-37") {
        dropIndex(indexName: "FK_9jma5e7kyk19qyuircu3nnol0", tableName: "loan_payment_order_authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-38") {
        dropIndex(indexName: "FK_a64q6jugjh0qqbrmik3wc4m57", tableName: "commission")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-39") {
        dropIndex(indexName: "FK_ajahww2oyodgs8nkgxii8vcyc", tableName: "sale_order_address")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-40") {
        dropIndex(indexName: "FK_aoni3grb28trkd55k5qdvmmlp", tableName: "loan_order")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-41") {
        dropIndex(indexName: "FK_atewti54qfefm3c47tl56bc6q", tableName: "profile_telephone")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-42") {
        dropIndex(indexName: "FK_b9vi6vh4wjnnlhg3fiqhibbn0", tableName: "company_telephone")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-43") {
        dropIndex(indexName: "FK_bi96koa3fssjo9uxnnivj99hy", tableName: "business_entity_address")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-44") {
        dropIndex(indexName: "FK_br0e1cf387hqfyl8r6kwgda39", tableName: "company_bank_account")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-45") {
        dropIndex(indexName: "FK_cndhy0ioljqk55e72fq57caui", tableName: "company_business_entity")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-46") {
        dropIndex(indexName: "FK_cskx8lxe537k80wgvcaikhcl1", tableName: "loan_order_s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-47") {
        dropIndex(indexName: "FK_d1mewcfc7h3b94jtl0502o4og", tableName: "modulus_uno_account")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-48") {
        dropIndex(indexName: "FK_eoqg04g2l1a8q8igjc4rletj8", tableName: "sale_order_address")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-49") {
        dropIndex(indexName: "FK_fc4511gyg27b7fsbnh548xidq", tableName: "deposit_order_s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-50") {
        dropIndex(indexName: "FK_ffok8uw34n09jms16o7syeh3v", tableName: "sale_order")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-51") {
        dropIndex(indexName: "FK_fuicds1wwge4vwujato9u3cks", tableName: "company_address")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-52") {
        dropIndex(indexName: "FK_g9bvx5cb2ynsq2oxeq094w31p", tableName: "purchase_order_payment_to_purchase")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-53") {
        dropIndex(indexName: "FK_gd2dhyoj1fmwk484482lnquso", tableName: "deposit_order_authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-54") {
        dropIndex(indexName: "FK_gh6vd4lpw7rfi6udgpmefs9r7", tableName: "loan_order_authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-55") {
        dropIndex(indexName: "FK_hluxnc67592jgfpl98v7pnscx", tableName: "user_role_company_role")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-56") {
        dropIndex(indexName: "FK_i78rkp7yywh4tembtx5ihxm7n", tableName: "fees_receipt_s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-57") {
        dropIndex(indexName: "FK_i98w7amj0ducvqps32ebja25r", tableName: "fees_receipt_authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-58") {
        dropIndex(indexName: "FK_it77eq964jhfqtu54081ebtio", tableName: "user_role")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-59") {
        dropIndex(indexName: "FK_j4n82x30i201m3hur184i0sre", tableName: "loan_payment_order")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-60") {
        dropIndex(indexName: "FK_jfyrjp5jp8som68gi8i2xhpgf", tableName: "loan_payment_order")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-61") {
        dropIndex(indexName: "FK_jur33w6ryvek3oa3dngwt8lqw", tableName: "business_entity_address")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-62") {
        dropIndex(indexName: "FK_l0jp8830qdyelrll7bkd2w846", tableName: "purchase_order")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-63") {
        dropIndex(indexName: "FK_l8yr249frc4ndctg7d1ioc1fe", tableName: "fees_receipt")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-64") {
        dropIndex(indexName: "FK_lbbwhrvw4lp38agdl1nr1bi2", tableName: "user_role_company")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-65") {
        dropIndex(indexName: "FK_lfljxhh8n5pik85wl02g2cuj4", tableName: "deposit_order_authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-66") {
        dropIndex(indexName: "FK_mxvud6egd9nvaa0dtuo4rch8s", tableName: "first_access_to_legal_representatives")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-67") {
        dropIndex(indexName: "FK_ndjediucqay0cjd4o508k0yxf", tableName: "business_entity_bank_account")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-68") {
        dropIndex(indexName: "FK_nrso9xpub8w0ncciyafd6qq4d", tableName: "company_address")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-69") {
        dropIndex(indexName: "FK_ny4e4sb5gwjknkbkimh9ad12e", tableName: "business_entity_bank_account")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-70") {
        dropIndex(indexName: "FK_o3g0cb3kil96la70xt16siocu", tableName: "loan_payment_order_authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-71") {
        dropIndex(indexName: "FK_ojo4se5xxv78pa3dceihpe51l", tableName: "user_role_company_role")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-72") {
        dropIndex(indexName: "FK_owheu11vhnpfmqfpd4y01k1ct", tableName: "company_s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-73") {
        dropIndex(indexName: "FK_p03yka0x638p1lt4fu2f8cafh", tableName: "bank_account")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-74") {
        dropIndex(indexName: "FK_p0svctns3tvk3nsg2ofu95f6y", tableName: "authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-75") {
        dropIndex(indexName: "FK_p2n2ovcwouuhbu6e5j3nsmdwi", tableName: "cash_out_order")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-76") {
        dropIndex(indexName: "FK_plp60f9wa9rbix1e3wegi3o4t", tableName: "company_bank_account")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-77") {
        dropIndex(indexName: "FK_q6rmqkgm8gwg1qtpvu4eu25m5", tableName: "provider_link")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-78") {
        dropIndex(indexName: "FK_qatwcpuy6ur5hskwnmo8spjdw", tableName: "first_access_to_legal_representatives")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-79") {
        dropIndex(indexName: "FK_qmcq07p0niw1bxin654va8xxv", tableName: "company_s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-80") {
        dropIndex(indexName: "FK_qxtq9xmyhwqmc3b2o4ty4ge8g", tableName: "deposit_order")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-81") {
        dropIndex(indexName: "FK_qxug1aj0k4b5tbixbm2a8g9nj", tableName: "sale_order_authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-82") {
        dropIndex(indexName: "FK_qyodi27brvg62u48ffvniw5g4", tableName: "purchase_order_item")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-83") {
        dropIndex(indexName: "FK_r9qv2bxe3le52lt36ubnjramx", tableName: "purchase_order")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-84") {
        dropIndex(indexName: "FK_rco4kaeej2an07aasmqw2p538", tableName: "sale_order_s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-85") {
        dropIndex(indexName: "FK_ri9istm8vp3x6cop99vhxlhc5", tableName: "purchase_order_authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-86") {
        dropIndex(indexName: "FK_rim8kc6rbj535werdkg4wxqfm", tableName: "corporate_user")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-87") {
        dropIndex(indexName: "FK_rjfla2m6jjrq4relh4idhb9c7", tableName: "purchase_order_s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-88") {
        dropIndex(indexName: "FK_rjqgae44682dns471c7hweb8e", tableName: "compose_name")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-89") {
        dropIndex(indexName: "FK_s14mlklg07gxek8vc2qmg5yk4", tableName: "company_telephone")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-90") {
        dropIndex(indexName: "FK_s2tgg8i7gbg9fgnkb7j05vhh1", tableName: "client_link")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-91") {
        dropIndex(indexName: "FK_s6qu1iyo2xra9o42sj0vney", tableName: "profile_telephone")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-92") {
        dropIndex(indexName: "FK_sorpav5eibs6myntu9bm6oerw", tableName: "movimientos_bancarios")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-93") {
        dropIndex(indexName: "FK_te8x5ha1oi9xmpbummb33gonr", tableName: "corporate_company")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-94") {
        dropIndex(indexName: "FK_tf5ckj7ugmmbtd0nku9nm1hs5", tableName: "profile_s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-95") {
        dropIndex(indexName: "FK_thyldems4h03uogsgduropc0i", tableName: "purchase_order_s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-96") {
        dropIndex(indexName: "FK_tn08matxb1rdwy9879bbx2bsk", tableName: "cash_out_order")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-97") {
        dropIndex(indexName: "FK_vevapj0ikfqe1s21iy1d9yyf", tableName: "profile_s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-98") {
        dropIndex(indexName: "FK_wbaipjtxlax1ajhy32amn5pe", tableName: "loan_order")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-99") {
        dropForeignKeyConstraint(baseTableName: "sale_order_s3asset", constraintName: "FK_14gx7m1jy60c780oe6uycmfug")

        addForeignKeyConstraint(baseColumnNames: "s3asset_id", baseTableName: "sale_order_s3asset", constraintName: "FK_14gx7m1jy60c780oe6uycmfug", referencedColumnNames: "id", referencedTableName: "s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-100") {
        dropForeignKeyConstraint(baseTableName: "fees_receipt_s3asset", constraintName: "FK_18m22ybal0ga91hy3s5l66bul")

        addForeignKeyConstraint(baseColumnNames: "s3asset_id", baseTableName: "fees_receipt_s3asset", constraintName: "FK_18m22ybal0ga91hy3s5l66bul", referencedColumnNames: "id", referencedTableName: "s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-101") {
        dropForeignKeyConstraint(baseTableName: "user", constraintName: "FK_1mcjtpxmwom9h9bf2q0k412e0")

        addForeignKeyConstraint(baseColumnNames: "profile_id", baseTableName: "user", constraintName: "FK_1mcjtpxmwom9h9bf2q0k412e0", referencedColumnNames: "id", referencedTableName: "profile")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-102") {
        dropForeignKeyConstraint(baseTableName: "payment", constraintName: "FK_26aw28fqp8nv9kd2n9kug1ate")

        addForeignKeyConstraint(baseColumnNames: "company_id", baseTableName: "payment", constraintName: "FK_26aw28fqp8nv9kd2n9kug1ate", referencedColumnNames: "id", referencedTableName: "company")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-103") {
        dropForeignKeyConstraint(baseTableName: "sale_order_authorization", constraintName: "FK_29tfk85uscogyuyhnmogjstyq")

        addForeignKeyConstraint(baseColumnNames: "authorization_id", baseTableName: "sale_order_authorization", constraintName: "FK_29tfk85uscogyuyhnmogjstyq", referencedColumnNames: "id", referencedTableName: "authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-104") {
        dropForeignKeyConstraint(baseTableName: "payment", constraintName: "FK_2b7qu5poo9sv9mtyuw9xhkcl2")

        addForeignKeyConstraint(baseColumnNames: "sale_order_id", baseTableName: "payment", constraintName: "FK_2b7qu5poo9sv9mtyuw9xhkcl2", referencedColumnNames: "id", referencedTableName: "sale_order")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-105") {
        dropForeignKeyConstraint(baseTableName: "purchase_order_payment_to_purchase", constraintName: "FK_2hoido30po6klvme7xsojbcib")

        addForeignKeyConstraint(baseColumnNames: "payment_to_purchase_id", baseTableName: "purchase_order_payment_to_purchase", constraintName: "FK_2hoido30po6klvme7xsojbcib", referencedColumnNames: "id", referencedTableName: "payment_to_purchase")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-106") {
        dropForeignKeyConstraint(baseTableName: "fees_receipt_authorization", constraintName: "FK_3f85cq7on905fe0rwqwbvinnn")

        addForeignKeyConstraint(baseColumnNames: "authorization_id", baseTableName: "fees_receipt_authorization", constraintName: "FK_3f85cq7on905fe0rwqwbvinnn", referencedColumnNames: "id", referencedTableName: "authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-107") {
        dropForeignKeyConstraint(baseTableName: "employee_link", constraintName: "FK_3y480lpgswma3dlahkas0piv2")

        addForeignKeyConstraint(baseColumnNames: "company_id", baseTableName: "employee_link", constraintName: "FK_3y480lpgswma3dlahkas0piv2", referencedColumnNames: "id", referencedTableName: "company")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-108") {
        dropForeignKeyConstraint(baseTableName: "product", constraintName: "FK_45g3a4rexapplpirj3jor716p")

        addForeignKeyConstraint(baseColumnNames: "company_id", baseTableName: "product", constraintName: "FK_45g3a4rexapplpirj3jor716p", referencedColumnNames: "id", referencedTableName: "company")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-109") {
        dropForeignKeyConstraint(baseTableName: "purchase_order_authorization", constraintName: "FK_5a9nmc0j5rhmo268sm9f08lef")

        addForeignKeyConstraint(baseColumnNames: "authorization_id", baseTableName: "purchase_order_authorization", constraintName: "FK_5a9nmc0j5rhmo268sm9f08lef", referencedColumnNames: "id", referencedTableName: "authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-110") {
        dropForeignKeyConstraint(baseTableName: "cash_out_order_authorization", constraintName: "FK_5b91qa29nyutp6sh2fo4x4tn6")

        addForeignKeyConstraint(baseColumnNames: "cash_out_order_authorizations_id", baseTableName: "cash_out_order_authorization", constraintName: "FK_5b91qa29nyutp6sh2fo4x4tn6", referencedColumnNames: "id", referencedTableName: "cash_out_order")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-111") {
        dropForeignKeyConstraint(baseTableName: "company_business_entity", constraintName: "FK_5iomp5vtj1eh4ykkyf02cd808")

        addForeignKeyConstraint(baseColumnNames: "company_business_entities_id", baseTableName: "company_business_entity", constraintName: "FK_5iomp5vtj1eh4ykkyf02cd808", referencedColumnNames: "id", referencedTableName: "company")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-112") {
        dropForeignKeyConstraint(baseTableName: "sale_order_item", constraintName: "FK_5jo2bsgk40m6yjjd587bdvp2k")

        addForeignKeyConstraint(baseColumnNames: "sale_order_id", baseTableName: "sale_order_item", constraintName: "FK_5jo2bsgk40m6yjjd587bdvp2k", referencedColumnNames: "id", referencedTableName: "sale_order")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-113") {
        dropForeignKeyConstraint(baseTableName: "fees_receipt", constraintName: "FK_64uboislmwsbb72nj98pwjt06")

        addForeignKeyConstraint(baseColumnNames: "company_id", baseTableName: "fees_receipt", constraintName: "FK_64uboislmwsbb72nj98pwjt06", referencedColumnNames: "id", referencedTableName: "company")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-114") {
        dropForeignKeyConstraint(baseTableName: "corporate_user", constraintName: "FK_6aaj211p0i2g5rjxve1679j1c")

        addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "corporate_user", constraintName: "FK_6aaj211p0i2g5rjxve1679j1c", referencedColumnNames: "id", referencedTableName: "user")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-115") {
        dropForeignKeyConstraint(baseTableName: "deposit_order_s3asset", constraintName: "FK_78mxqus4c33f8agef1720orat")

        addForeignKeyConstraint(baseColumnNames: "deposit_order_documents_id", baseTableName: "deposit_order_s3asset", constraintName: "FK_78mxqus4c33f8agef1720orat", referencedColumnNames: "id", referencedTableName: "deposit_order")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-116") {
        dropForeignKeyConstraint(baseTableName: "cash_out_order_authorization", constraintName: "FK_7js7ru525ir9t0f0atxgr143u")

        addForeignKeyConstraint(baseColumnNames: "authorization_id", baseTableName: "cash_out_order_authorization", constraintName: "FK_7js7ru525ir9t0f0atxgr143u", referencedColumnNames: "id", referencedTableName: "authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-117") {
        dropForeignKeyConstraint(baseTableName: "user_role_company", constraintName: "FK_7khskpewos3jrxg9umn6n6mj5")

        addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "user_role_company", constraintName: "FK_7khskpewos3jrxg9umn6n6mj5", referencedColumnNames: "id", referencedTableName: "user")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-118") {
        dropForeignKeyConstraint(baseTableName: "loan_order_authorization", constraintName: "FK_7s9iidg2nghqx5vm8ufivle2f")

        addForeignKeyConstraint(baseColumnNames: "authorization_id", baseTableName: "loan_order_authorization", constraintName: "FK_7s9iidg2nghqx5vm8ufivle2f", referencedColumnNames: "id", referencedTableName: "authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-119") {
        dropForeignKeyConstraint(baseTableName: "loan_order_s3asset", constraintName: "FK_8vi2muat59yyggpgqvqpum19o")

        addForeignKeyConstraint(baseColumnNames: "loan_order_documents_id", baseTableName: "loan_order_s3asset", constraintName: "FK_8vi2muat59yyggpgqvqpum19o", referencedColumnNames: "id", referencedTableName: "loan_order")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-120") {
        dropForeignKeyConstraint(baseTableName: "corporate_company", constraintName: "FK_97miav6mf441scxegv4yeahxn")

        addForeignKeyConstraint(baseColumnNames: "company_id", baseTableName: "corporate_company", constraintName: "FK_97miav6mf441scxegv4yeahxn", referencedColumnNames: "id", referencedTableName: "company")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-121") {
        dropForeignKeyConstraint(baseTableName: "loan_payment_order_authorization", constraintName: "FK_9jma5e7kyk19qyuircu3nnol0")

        addForeignKeyConstraint(baseColumnNames: "loan_payment_order_authorizations_id", baseTableName: "loan_payment_order_authorization", constraintName: "FK_9jma5e7kyk19qyuircu3nnol0", referencedColumnNames: "id", referencedTableName: "loan_payment_order")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-122") {
        dropForeignKeyConstraint(baseTableName: "commission", constraintName: "FK_a64q6jugjh0qqbrmik3wc4m57")

        addForeignKeyConstraint(baseColumnNames: "company_id", baseTableName: "commission", constraintName: "FK_a64q6jugjh0qqbrmik3wc4m57", referencedColumnNames: "id", referencedTableName: "company")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-123") {
        dropForeignKeyConstraint(baseTableName: "sale_order_address", constraintName: "FK_ajahww2oyodgs8nkgxii8vcyc")

        addForeignKeyConstraint(baseColumnNames: "sale_order_addresses_id", baseTableName: "sale_order_address", constraintName: "FK_ajahww2oyodgs8nkgxii8vcyc", referencedColumnNames: "id", referencedTableName: "sale_order")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-124") {
        dropForeignKeyConstraint(baseTableName: "loan_order", constraintName: "FK_aoni3grb28trkd55k5qdvmmlp")

        addForeignKeyConstraint(baseColumnNames: "creditor_id", baseTableName: "loan_order", constraintName: "FK_aoni3grb28trkd55k5qdvmmlp", referencedColumnNames: "id", referencedTableName: "company")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-125") {
        dropForeignKeyConstraint(baseTableName: "user_role", constraintName: "FK_apcc8lxk2xnug8377fatvbn04")

        addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "user_role", constraintName: "FK_apcc8lxk2xnug8377fatvbn04", referencedColumnNames: "id", referencedTableName: "user")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-126") {
        dropForeignKeyConstraint(baseTableName: "profile_telephone", constraintName: "FK_atewti54qfefm3c47tl56bc6q")

        addForeignKeyConstraint(baseColumnNames: "profile_telephones_id", baseTableName: "profile_telephone", constraintName: "FK_atewti54qfefm3c47tl56bc6q", referencedColumnNames: "id", referencedTableName: "profile")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-127") {
        dropForeignKeyConstraint(baseTableName: "company_telephone", constraintName: "FK_b9vi6vh4wjnnlhg3fiqhibbn0")

        addForeignKeyConstraint(baseColumnNames: "telephone_id", baseTableName: "company_telephone", constraintName: "FK_b9vi6vh4wjnnlhg3fiqhibbn0", referencedColumnNames: "id", referencedTableName: "telephone")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-128") {
        dropForeignKeyConstraint(baseTableName: "business_entity_address", constraintName: "FK_bi96koa3fssjo9uxnnivj99hy")

        addForeignKeyConstraint(baseColumnNames: "business_entity_addresses_id", baseTableName: "business_entity_address", constraintName: "FK_bi96koa3fssjo9uxnnivj99hy", referencedColumnNames: "id", referencedTableName: "business_entity")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-129") {
        dropForeignKeyConstraint(baseTableName: "company_bank_account", constraintName: "FK_br0e1cf387hqfyl8r6kwgda39")

        addForeignKeyConstraint(baseColumnNames: "company_banks_accounts_id", baseTableName: "company_bank_account", constraintName: "FK_br0e1cf387hqfyl8r6kwgda39", referencedColumnNames: "id", referencedTableName: "company")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-130") {
        dropForeignKeyConstraint(baseTableName: "company_business_entity", constraintName: "FK_cndhy0ioljqk55e72fq57caui")

        addForeignKeyConstraint(baseColumnNames: "business_entity_id", baseTableName: "company_business_entity", constraintName: "FK_cndhy0ioljqk55e72fq57caui", referencedColumnNames: "id", referencedTableName: "business_entity")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-131") {
        dropForeignKeyConstraint(baseTableName: "loan_order_s3asset", constraintName: "FK_cskx8lxe537k80wgvcaikhcl1")

        addForeignKeyConstraint(baseColumnNames: "s3asset_id", baseTableName: "loan_order_s3asset", constraintName: "FK_cskx8lxe537k80wgvcaikhcl1", referencedColumnNames: "id", referencedTableName: "s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-132") {
        dropForeignKeyConstraint(baseTableName: "modulus_uno_account", constraintName: "FK_d1mewcfc7h3b94jtl0502o4og")

        addForeignKeyConstraint(baseColumnNames: "company_id", baseTableName: "modulus_uno_account", constraintName: "FK_d1mewcfc7h3b94jtl0502o4og", referencedColumnNames: "id", referencedTableName: "company")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-133") {
        dropForeignKeyConstraint(baseTableName: "sale_order_address", constraintName: "FK_eoqg04g2l1a8q8igjc4rletj8")

        addForeignKeyConstraint(baseColumnNames: "address_id", baseTableName: "sale_order_address", constraintName: "FK_eoqg04g2l1a8q8igjc4rletj8", referencedColumnNames: "id", referencedTableName: "address")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-134") {
        dropForeignKeyConstraint(baseTableName: "deposit_order_s3asset", constraintName: "FK_fc4511gyg27b7fsbnh548xidq")

        addForeignKeyConstraint(baseColumnNames: "s3asset_id", baseTableName: "deposit_order_s3asset", constraintName: "FK_fc4511gyg27b7fsbnh548xidq", referencedColumnNames: "id", referencedTableName: "s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-135") {
        dropForeignKeyConstraint(baseTableName: "sale_order", constraintName: "FK_ffok8uw34n09jms16o7syeh3v")

        addForeignKeyConstraint(baseColumnNames: "company_id", baseTableName: "sale_order", constraintName: "FK_ffok8uw34n09jms16o7syeh3v", referencedColumnNames: "id", referencedTableName: "company")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-136") {
        dropForeignKeyConstraint(baseTableName: "company_address", constraintName: "FK_fuicds1wwge4vwujato9u3cks")

        addForeignKeyConstraint(baseColumnNames: "address_id", baseTableName: "company_address", constraintName: "FK_fuicds1wwge4vwujato9u3cks", referencedColumnNames: "id", referencedTableName: "address")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-137") {
        dropForeignKeyConstraint(baseTableName: "purchase_order_payment_to_purchase", constraintName: "FK_g9bvx5cb2ynsq2oxeq094w31p")

        addForeignKeyConstraint(baseColumnNames: "purchase_order_payments_id", baseTableName: "purchase_order_payment_to_purchase", constraintName: "FK_g9bvx5cb2ynsq2oxeq094w31p", referencedColumnNames: "id", referencedTableName: "purchase_order")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-138") {
        dropForeignKeyConstraint(baseTableName: "deposit_order_authorization", constraintName: "FK_gd2dhyoj1fmwk484482lnquso")

        addForeignKeyConstraint(baseColumnNames: "deposit_order_authorizations_id", baseTableName: "deposit_order_authorization", constraintName: "FK_gd2dhyoj1fmwk484482lnquso", referencedColumnNames: "id", referencedTableName: "deposit_order")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-139") {
        dropForeignKeyConstraint(baseTableName: "loan_order_authorization", constraintName: "FK_gh6vd4lpw7rfi6udgpmefs9r7")

        addForeignKeyConstraint(baseColumnNames: "loan_order_authorizations_id", baseTableName: "loan_order_authorization", constraintName: "FK_gh6vd4lpw7rfi6udgpmefs9r7", referencedColumnNames: "id", referencedTableName: "loan_order")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-140") {
        dropForeignKeyConstraint(baseTableName: "user_role_company_role", constraintName: "FK_hluxnc67592jgfpl98v7pnscx")

        addForeignKeyConstraint(baseColumnNames: "role_id", baseTableName: "user_role_company_role", constraintName: "FK_hluxnc67592jgfpl98v7pnscx", referencedColumnNames: "id", referencedTableName: "role")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-141") {
        dropForeignKeyConstraint(baseTableName: "fees_receipt_s3asset", constraintName: "FK_i78rkp7yywh4tembtx5ihxm7n")

        addForeignKeyConstraint(baseColumnNames: "fees_receipt_documents_id", baseTableName: "fees_receipt_s3asset", constraintName: "FK_i78rkp7yywh4tembtx5ihxm7n", referencedColumnNames: "id", referencedTableName: "fees_receipt")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-142") {
        dropForeignKeyConstraint(baseTableName: "fees_receipt_authorization", constraintName: "FK_i98w7amj0ducvqps32ebja25r")

        addForeignKeyConstraint(baseColumnNames: "fees_receipt_authorizations_id", baseTableName: "fees_receipt_authorization", constraintName: "FK_i98w7amj0ducvqps32ebja25r", referencedColumnNames: "id", referencedTableName: "fees_receipt")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-143") {
        dropForeignKeyConstraint(baseTableName: "user_role", constraintName: "FK_it77eq964jhfqtu54081ebtio")

        addForeignKeyConstraint(baseColumnNames: "role_id", baseTableName: "user_role", constraintName: "FK_it77eq964jhfqtu54081ebtio", referencedColumnNames: "id", referencedTableName: "role")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-144") {
        dropForeignKeyConstraint(baseTableName: "loan_payment_order", constraintName: "FK_j4n82x30i201m3hur184i0sre")

        addForeignKeyConstraint(baseColumnNames: "company_id", baseTableName: "loan_payment_order", constraintName: "FK_j4n82x30i201m3hur184i0sre", referencedColumnNames: "id", referencedTableName: "company")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-145") {
        dropForeignKeyConstraint(baseTableName: "loan_payment_order", constraintName: "FK_jfyrjp5jp8som68gi8i2xhpgf")

        addForeignKeyConstraint(baseColumnNames: "loan_order_id", baseTableName: "loan_payment_order", constraintName: "FK_jfyrjp5jp8som68gi8i2xhpgf", referencedColumnNames: "id", referencedTableName: "loan_order")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-146") {
        dropForeignKeyConstraint(baseTableName: "business_entity_address", constraintName: "FK_jur33w6ryvek3oa3dngwt8lqw")

        addForeignKeyConstraint(baseColumnNames: "address_id", baseTableName: "business_entity_address", constraintName: "FK_jur33w6ryvek3oa3dngwt8lqw", referencedColumnNames: "id", referencedTableName: "address")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-147") {
        dropForeignKeyConstraint(baseTableName: "purchase_order", constraintName: "FK_l0jp8830qdyelrll7bkd2w846")

        addForeignKeyConstraint(baseColumnNames: "company_id", baseTableName: "purchase_order", constraintName: "FK_l0jp8830qdyelrll7bkd2w846", referencedColumnNames: "id", referencedTableName: "company")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-148") {
        dropForeignKeyConstraint(baseTableName: "fees_receipt", constraintName: "FK_l8yr249frc4ndctg7d1ioc1fe")

        addForeignKeyConstraint(baseColumnNames: "bank_account_id", baseTableName: "fees_receipt", constraintName: "FK_l8yr249frc4ndctg7d1ioc1fe", referencedColumnNames: "id", referencedTableName: "bank_account")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-149") {
        dropForeignKeyConstraint(baseTableName: "user_role_company", constraintName: "FK_lbbwhrvw4lp38agdl1nr1bi2")

        addForeignKeyConstraint(baseColumnNames: "company_id", baseTableName: "user_role_company", constraintName: "FK_lbbwhrvw4lp38agdl1nr1bi2", referencedColumnNames: "id", referencedTableName: "company")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-150") {
        dropForeignKeyConstraint(baseTableName: "deposit_order_authorization", constraintName: "FK_lfljxhh8n5pik85wl02g2cuj4")

        addForeignKeyConstraint(baseColumnNames: "authorization_id", baseTableName: "deposit_order_authorization", constraintName: "FK_lfljxhh8n5pik85wl02g2cuj4", referencedColumnNames: "id", referencedTableName: "authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-151") {
        dropForeignKeyConstraint(baseTableName: "first_access_to_legal_representatives", constraintName: "FK_mxvud6egd9nvaa0dtuo4rch8s")

        addForeignKeyConstraint(baseColumnNames: "company_id", baseTableName: "first_access_to_legal_representatives", constraintName: "FK_mxvud6egd9nvaa0dtuo4rch8s", referencedColumnNames: "id", referencedTableName: "company")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-152") {
        dropForeignKeyConstraint(baseTableName: "business_entity_bank_account", constraintName: "FK_ndjediucqay0cjd4o508k0yxf")

        addForeignKeyConstraint(baseColumnNames: "business_entity_banks_accounts_id", baseTableName: "business_entity_bank_account", constraintName: "FK_ndjediucqay0cjd4o508k0yxf", referencedColumnNames: "id", referencedTableName: "business_entity")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-153") {
        dropForeignKeyConstraint(baseTableName: "company_address", constraintName: "FK_nrso9xpub8w0ncciyafd6qq4d")

        addForeignKeyConstraint(baseColumnNames: "company_addresses_id", baseTableName: "company_address", constraintName: "FK_nrso9xpub8w0ncciyafd6qq4d", referencedColumnNames: "id", referencedTableName: "company")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-154") {
        dropForeignKeyConstraint(baseTableName: "business_entity_bank_account", constraintName: "FK_ny4e4sb5gwjknkbkimh9ad12e")

        addForeignKeyConstraint(baseColumnNames: "bank_account_id", baseTableName: "business_entity_bank_account", constraintName: "FK_ny4e4sb5gwjknkbkimh9ad12e", referencedColumnNames: "id", referencedTableName: "bank_account")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-155") {
        dropForeignKeyConstraint(baseTableName: "loan_payment_order_authorization", constraintName: "FK_o3g0cb3kil96la70xt16siocu")

        addForeignKeyConstraint(baseColumnNames: "authorization_id", baseTableName: "loan_payment_order_authorization", constraintName: "FK_o3g0cb3kil96la70xt16siocu", referencedColumnNames: "id", referencedTableName: "authorization")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-156") {
        dropForeignKeyConstraint(baseTableName: "user_role_company_role", constraintName: "FK_ojo4se5xxv78pa3dceihpe51l")

        addForeignKeyConstraint(baseColumnNames: "user_role_company_roles_id", baseTableName: "user_role_company_role", constraintName: "FK_ojo4se5xxv78pa3dceihpe51l", referencedColumnNames: "id", referencedTableName: "user_role_company")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-157") {
        dropForeignKeyConstraint(baseTableName: "company_s3asset", constraintName: "FK_owheu11vhnpfmqfpd4y01k1ct")

        addForeignKeyConstraint(baseColumnNames: "company_documents_id", baseTableName: "company_s3asset", constraintName: "FK_owheu11vhnpfmqfpd4y01k1ct", referencedColumnNames: "id", referencedTableName: "company")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-158") {
        dropForeignKeyConstraint(baseTableName: "bank_account", constraintName: "FK_p03yka0x638p1lt4fu2f8cafh")

        addForeignKeyConstraint(baseColumnNames: "banco_id", baseTableName: "bank_account", constraintName: "FK_p03yka0x638p1lt4fu2f8cafh", referencedColumnNames: "id", referencedTableName: "bank")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-159") {
        dropForeignKeyConstraint(baseTableName: "authorization", constraintName: "FK_p0svctns3tvk3nsg2ofu95f6y")

        addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "authorization", constraintName: "FK_p0svctns3tvk3nsg2ofu95f6y", referencedColumnNames: "id", referencedTableName: "user")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-160") {
        dropForeignKeyConstraint(baseTableName: "cash_out_order", constraintName: "FK_p2n2ovcwouuhbu6e5j3nsmdwi")

        addForeignKeyConstraint(baseColumnNames: "company_id", baseTableName: "cash_out_order", constraintName: "FK_p2n2ovcwouuhbu6e5j3nsmdwi", referencedColumnNames: "id", referencedTableName: "company")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-161") {
        dropForeignKeyConstraint(baseTableName: "company_bank_account", constraintName: "FK_plp60f9wa9rbix1e3wegi3o4t")

        addForeignKeyConstraint(baseColumnNames: "bank_account_id", baseTableName: "company_bank_account", constraintName: "FK_plp60f9wa9rbix1e3wegi3o4t", referencedColumnNames: "id", referencedTableName: "bank_account")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-162") {
        dropForeignKeyConstraint(baseTableName: "provider_link", constraintName: "FK_q6rmqkgm8gwg1qtpvu4eu25m5")

        addForeignKeyConstraint(baseColumnNames: "company_id", baseTableName: "provider_link", constraintName: "FK_q6rmqkgm8gwg1qtpvu4eu25m5", referencedColumnNames: "id", referencedTableName: "company")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-163") {
        dropForeignKeyConstraint(baseTableName: "first_access_to_legal_representatives", constraintName: "FK_qatwcpuy6ur5hskwnmo8spjdw")

        addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "first_access_to_legal_representatives", constraintName: "FK_qatwcpuy6ur5hskwnmo8spjdw", referencedColumnNames: "id", referencedTableName: "user")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-164") {
        dropForeignKeyConstraint(baseTableName: "company_s3asset", constraintName: "FK_qmcq07p0niw1bxin654va8xxv")

        addForeignKeyConstraint(baseColumnNames: "s3asset_id", baseTableName: "company_s3asset", constraintName: "FK_qmcq07p0niw1bxin654va8xxv", referencedColumnNames: "id", referencedTableName: "s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-165") {
        dropForeignKeyConstraint(baseTableName: "deposit_order", constraintName: "FK_qxtq9xmyhwqmc3b2o4ty4ge8g")

        addForeignKeyConstraint(baseColumnNames: "company_id", baseTableName: "deposit_order", constraintName: "FK_qxtq9xmyhwqmc3b2o4ty4ge8g", referencedColumnNames: "id", referencedTableName: "company")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-166") {
        dropForeignKeyConstraint(baseTableName: "sale_order_authorization", constraintName: "FK_qxug1aj0k4b5tbixbm2a8g9nj")

        addForeignKeyConstraint(baseColumnNames: "sale_order_authorizations_id", baseTableName: "sale_order_authorization", constraintName: "FK_qxug1aj0k4b5tbixbm2a8g9nj", referencedColumnNames: "id", referencedTableName: "sale_order")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-167") {
        dropForeignKeyConstraint(baseTableName: "purchase_order_item", constraintName: "FK_qyodi27brvg62u48ffvniw5g4")

        addForeignKeyConstraint(baseColumnNames: "purchase_order_id", baseTableName: "purchase_order_item", constraintName: "FK_qyodi27brvg62u48ffvniw5g4", referencedColumnNames: "id", referencedTableName: "purchase_order")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-168") {
        dropForeignKeyConstraint(baseTableName: "purchase_order", constraintName: "FK_r9qv2bxe3le52lt36ubnjramx")

        addForeignKeyConstraint(baseColumnNames: "bank_account_id", baseTableName: "purchase_order", constraintName: "FK_r9qv2bxe3le52lt36ubnjramx", referencedColumnNames: "id", referencedTableName: "bank_account")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-169") {
        dropForeignKeyConstraint(baseTableName: "sale_order_s3asset", constraintName: "FK_rco4kaeej2an07aasmqw2p538")

        addForeignKeyConstraint(baseColumnNames: "sale_order_documents_id", baseTableName: "sale_order_s3asset", constraintName: "FK_rco4kaeej2an07aasmqw2p538", referencedColumnNames: "id", referencedTableName: "sale_order")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-170") {
        dropForeignKeyConstraint(baseTableName: "purchase_order_authorization", constraintName: "FK_ri9istm8vp3x6cop99vhxlhc5")

        addForeignKeyConstraint(baseColumnNames: "purchase_order_authorizations_id", baseTableName: "purchase_order_authorization", constraintName: "FK_ri9istm8vp3x6cop99vhxlhc5", referencedColumnNames: "id", referencedTableName: "purchase_order")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-171") {
        dropForeignKeyConstraint(baseTableName: "corporate_user", constraintName: "FK_rim8kc6rbj535werdkg4wxqfm")

        addForeignKeyConstraint(baseColumnNames: "corporate_users_id", baseTableName: "corporate_user", constraintName: "FK_rim8kc6rbj535werdkg4wxqfm", referencedColumnNames: "id", referencedTableName: "corporate")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-172") {
        dropForeignKeyConstraint(baseTableName: "purchase_order_s3asset", constraintName: "FK_rjfla2m6jjrq4relh4idhb9c7")

        addForeignKeyConstraint(baseColumnNames: "s3asset_id", baseTableName: "purchase_order_s3asset", constraintName: "FK_rjfla2m6jjrq4relh4idhb9c7", referencedColumnNames: "id", referencedTableName: "s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-173") {
        dropForeignKeyConstraint(baseTableName: "compose_name", constraintName: "FK_rjqgae44682dns471c7hweb8e")

        addForeignKeyConstraint(baseColumnNames: "business_entity_id", baseTableName: "compose_name", constraintName: "FK_rjqgae44682dns471c7hweb8e", referencedColumnNames: "id", referencedTableName: "business_entity")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-174") {
        dropForeignKeyConstraint(baseTableName: "company_telephone", constraintName: "FK_s14mlklg07gxek8vc2qmg5yk4")

        addForeignKeyConstraint(baseColumnNames: "company_telephones_id", baseTableName: "company_telephone", constraintName: "FK_s14mlklg07gxek8vc2qmg5yk4", referencedColumnNames: "id", referencedTableName: "company")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-175") {
        dropForeignKeyConstraint(baseTableName: "client_link", constraintName: "FK_s2tgg8i7gbg9fgnkb7j05vhh1")

        addForeignKeyConstraint(baseColumnNames: "company_id", baseTableName: "client_link", constraintName: "FK_s2tgg8i7gbg9fgnkb7j05vhh1", referencedColumnNames: "id", referencedTableName: "company")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-176") {
        dropForeignKeyConstraint(baseTableName: "profile_telephone", constraintName: "FK_s6qu1iyo2xra9o42sj0vney")

        addForeignKeyConstraint(baseColumnNames: "telephone_id", baseTableName: "profile_telephone", constraintName: "FK_s6qu1iyo2xra9o42sj0vney", referencedColumnNames: "id", referencedTableName: "telephone")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-177") {
        dropForeignKeyConstraint(baseTableName: "movimientos_bancarios", constraintName: "FK_sorpav5eibs6myntu9bm6oerw")

        addForeignKeyConstraint(baseColumnNames: "cuenta_id", baseTableName: "movimientos_bancarios", constraintName: "FK_sorpav5eibs6myntu9bm6oerw", referencedColumnNames: "id", referencedTableName: "bank_account")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-178") {
        dropForeignKeyConstraint(baseTableName: "corporate_company", constraintName: "FK_te8x5ha1oi9xmpbummb33gonr")

        addForeignKeyConstraint(baseColumnNames: "corporate_companies_id", baseTableName: "corporate_company", constraintName: "FK_te8x5ha1oi9xmpbummb33gonr", referencedColumnNames: "id", referencedTableName: "corporate")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-179") {
        dropForeignKeyConstraint(baseTableName: "profile_s3asset", constraintName: "FK_tf5ckj7ugmmbtd0nku9nm1hs5")

        addForeignKeyConstraint(baseColumnNames: "profile_documents_id", baseTableName: "profile_s3asset", constraintName: "FK_tf5ckj7ugmmbtd0nku9nm1hs5", referencedColumnNames: "id", referencedTableName: "profile")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-180") {
        dropForeignKeyConstraint(baseTableName: "purchase_order_s3asset", constraintName: "FK_thyldems4h03uogsgduropc0i")

        addForeignKeyConstraint(baseColumnNames: "purchase_order_documents_id", baseTableName: "purchase_order_s3asset", constraintName: "FK_thyldems4h03uogsgduropc0i", referencedColumnNames: "id", referencedTableName: "purchase_order")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-181") {
        dropForeignKeyConstraint(baseTableName: "cash_out_order", constraintName: "FK_tn08matxb1rdwy9879bbx2bsk")

        addForeignKeyConstraint(baseColumnNames: "account_id", baseTableName: "cash_out_order", constraintName: "FK_tn08matxb1rdwy9879bbx2bsk", referencedColumnNames: "id", referencedTableName: "bank_account")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-182") {
        dropForeignKeyConstraint(baseTableName: "profile_s3asset", constraintName: "FK_vevapj0ikfqe1s21iy1d9yyf")

        addForeignKeyConstraint(baseColumnNames: "s3asset_id", baseTableName: "profile_s3asset", constraintName: "FK_vevapj0ikfqe1s21iy1d9yyf", referencedColumnNames: "id", referencedTableName: "s3asset")
    }

    changeSet(author: "makingdevs (generated)", id: "1487714352840-183") {
        dropForeignKeyConstraint(baseTableName: "loan_order", constraintName: "FK_wbaipjtxlax1ajhy32amn5pe")

        addForeignKeyConstraint(baseColumnNames: "company_id", baseTableName: "loan_order", constraintName: "FK_wbaipjtxlax1ajhy32amn5pe", referencedColumnNames: "id", referencedTableName: "company")
    }
}
