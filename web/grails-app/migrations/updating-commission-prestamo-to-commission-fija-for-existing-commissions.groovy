 import liquibase.statement.core.*
import com.modulus.uno.Commission

databaseChangeLog = {
  changeSet(author: "temoc (manual)", id: "updating-commission-prestamo-to-commission-fija") {
    grailsChange {
      change {
        sql.execute("update commission set type='FIJA' where type='PRESTAMO'")
      }
    }
  }

}

