package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class CombinationService {

  Combination createCombinationOfInstanceWithClass(def instance,String className){
    new Combination(classLeft:className,
                    classRight:instance.class.simpleName,
                    rightInstanceId:instance.id).save()
  }

}
