import grails.util.Environment

databaseChangeLog = {

  if(Environment.current == Environment.PRODUCTION){
    changeSet(author: "says33 (manual)",id: "create-user-admin-production") {
      sql(""" INSERT INTO profile(version, email, last_name, mother_last_name, name) values (0,'alexis.brunel@techminds.com.mx', 'Peralta', 'Brunel', 'Alexis') """)
      sql(""" INSERT INTO user(enabled, password, profile_id, username, uuid, version, account_expired, account_locked, password_expired) values (true,'\$2a\$10\$/yexq4HcTmBLShsEjCpD/uRQ8SeE2dC3XNdKUWt67AWg7XfiwdCOm',1,'Atites', '21f8ca3ba2384f749cc076fe4ebee482',0, false, false, false) """)
      sql(""" INSERT INTO user_role(user_id, role_id) values (1,1) """)
    }
  }else{
    //La contraseña de user_m1 es 1234567890Abc
    changeSet(author: "cggg88jorge (manual)",id: "create-user-admin-development") {
      sql(""" INSERT INTO profile(version, email, last_name, mother_last_name, name) values (0,'user_m1@email.com', 'user_m1', 'user_m1', 'user_m1') """)
      sql(""" INSERT INTO user(enabled, password, profile_id, username, uuid, version, account_expired, account_locked, password_expired) values (true,'\$2a\$10\$2Lax9RQsmG7bwSH429QgQu9z1v9cu4IuyDy3sW3krzHyXJ33XD2sS',1,'user_m1', '21f8ca3ba2384f749cc076fe4ebee482',0, false, false, false) """)
      sql(""" INSERT INTO user_role(user_id, role_id) values (1,1) """)
    }
  }
}