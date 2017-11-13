package com.modulus.uno.stp

import com.modulus.uno.ModulusUnoAccount
import com.modulus.uno.ClientLink
import com.modulus.uno.BusinessException

class StpClabeService {

  String generateSTPMainAccount(String payerAccount, Integer sizePrefix) {
    String stpClabe = ""
    Integer userAccountId = calculateSerialForNewMainAccountFromPayerAccount(payerAccount, sizePrefix)
    String zeros = "0".padLeft((sizePrefix==3?4:3),"0")
    stpClabe = "${payerAccount}${convertUserIdToSerial(userAccountId, sizePrefix)}${zeros}"
    Integer verifiedDigit = getVerifiedDigit(stpClabe)
    stpClabe = "${stpClabe}${verifiedDigit}"
    stpClabe
  }

  private Integer calculateSerialForNewMainAccountFromPayerAccount(String payerAccount, Integer sizePrefix) {
    List<ModulusUnoAccount> m1Accounts = ModulusUnoAccount.findAllByStpClabeLike("${payerAccount}%")
    if (m1Accounts) {
      String lastStpClabe = (m1Accounts.sort {it.stpClabe}).last().stpClabe
      String lastSerial = lastStpClabe.substring(10,(10+sizePrefix))
      Integer newSerial = Integer.parseInt(lastSerial)+1
    } else {
      new Integer(1)
    }
  }

  private String convertUserIdToSerial (Integer userId, Integer sizePrefix) {
    if (userId > (sizePrefix==3?999:9999)) {
      throw new BusinessException("El rango de clabes para empresas ha sido excedido")
    }

    userId.toString().padLeft(sizePrefix, "0")
  }

  String generateSTPSubAccount(String mainAccount, Integer sizePrefix) {
    String stpSubAccount = ""
    String prefix = mainAccount.substring(0,(10+(sizePrefix==3?4:3)))
    Integer subAccountId = calculateSerialForNewSubAccountAtMainAccount(prefix, sizePrefix)
    String serialAccount = convertSubAccountIdToSerial(subAccountId, sizePrefix)
    stpSubAccount = "${prefix}${serialAccount}"
    Integer verifiedDigit = getVerifiedDigit(stpSubAccount)
    stpSubAccount = "${stpSubAccount}${verifiedDigit}"
    stpSubAccount
  }

  private Integer calculateSerialForNewSubAccountAtMainAccount(String mainAccount, Integer sizePrefix) {
    List<ClientLink> clientsExisting = ClientLink.findAllByStpClabeLike("${mainAccount}%")
    if (clientsExisting) {
      String lastStpClabe = (clientsExisting.sort {it.stpClabe}).last().stpClabe
      String lastSerial = lastStpClabe.substring(10+(sizePrefix==3?4:3),17)
      Integer newSerial = Integer.parseInt(lastSerial)+1
    } else {
      new Integer(1)
    }
  }

  private String convertSubAccountIdToSerial(Integer subAccountId, Integer sizePrefix) {
    if (subAccountId > (sizePrefix==3?999:9999)) {
      throw new BusinessException("El rango de clabes para clientes de la empresa ha sido excedido")
    }

    subAccountId.toString().padLeft(sizePrefix, "0")
  }


  private Integer getVerifiedDigit(String clabe){
    int[] ponderacion = [3,7,1,3,7,1,3,7,1,3,7,1,3,7,1,3,7] as int[]

    int resultado = 0
    int modulo = 0
    int acomulador = 0
    for(int i=0; i<17; i++){
      resultado = ponderacion[i] * Character.getNumericValue(clabe.charAt(i))
      modulo = resultado % 10
      acomulador += modulo
    }
    int a = acomulador % 10
    int b = 10 - a
    int digito = b % 10
    digito
  }

}
