package com.modulus.uno.quotation

import com.modulus.uno.Company

class QuotationContractController {

    def show(){
    	Company company = Company.get(session.company)
    	respond new QuotationContract(), model:[company:company]
    }

    
}
