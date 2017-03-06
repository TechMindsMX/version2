package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class DocumentService {

  def grailsApplication
  def companyService
  def s3AssetService

  def uploadDocumentCompany(def document,def type,def companyId) {
    def asset = s3AssetService.createTempFilesOfMultipartsFiles(document,type)
    def company = Company.findById(companyId)
    def assetExisting = searchAssetByTypeIfExist(type,company.documents)
    if (assetExisting)
      deleteAssetOfRelationship(company,assetExisting.first())
    createRelationShipCompanyToAssets(asset,companyId)
  }

  def uploadDocumentForLegalRepresentative(def document,def type,def legalRepsentativeId,def companyId) {
    def asset = s3AssetService.createTempFilesOfMultipartsFiles(document,type)
    def user = User.findById(legalRepsentativeId)
    def assetExisting = searchAssetByTypeIfExist(type,user.profile.documents)
    if (assetExisting){
      def assetExistingByCompany = getDocumentsByCompanyForLegalRepresentative(assetExisting,companyId.toLong())
      if (assetExistingByCompany) {
        deleteAssetOfRelationship(user.profile,assetExistingByCompany.first())
        deleteRelationshipLegalRepresentativeWithCompany(assetExistingByCompany.first(),companyId.toLong())
      }
    }
    createRelationshipIntoLegalRepresentativeAndAsset(asset,legalRepsentativeId,companyId)
  }

  def uploadDocumentForOrder(def document,def type,def clazz) {
    def asset = s3AssetService.createTempFilesOfMultipartsFiles(document,type)
    clazz.addToDocuments(asset)
    clazz.save()
  }

  def getDocumentsByCompanyForLegalRepresentative(def listDocumentsOfLegal, def companyId) {
    if (listDocumentsOfLegal.isEmpty())
      return []
    def idListOfasset = LegalRepresentativesAssets.withCriteria {
      'in'("asset",listDocumentsOfLegal*.id)
      and {
        'eq' ('company', companyId.toLong())
      }
    }
    if (idListOfasset) {
      return S3Asset.findAllByIdInList(idListOfasset*.asset)
    } else {
      return []
    }
  }

  def deleteRelationshipLegalRepresentativeWithCompany(asset,companyId) {
    def legalRepresentativesAsset = LegalRepresentativesAssets.findByAssetAndCompany(asset.id,companyId)
    legalRepresentativesAsset.delete()
  }

  private def createRelationshipIntoLegalRepresentativeAndAsset(assetList, legalRepsentativeId, companyId) {
    def user = User.findById(legalRepsentativeId)
    assetList.each {asset ->
      def legal = new LegalRepresentativesAssets()
      legal.asset = asset.id
      legal.company = Company.findById(companyId).id
      legal.save()
      user.profile.addToDocuments(asset)
      user.profile.save()
    }
  }

  private def searchAssetByTypeIfExist(def type,def elementWithContent) {
    elementWithContent.findAll{ asset -> asset.type == type && asset.status == true}
  }

  private def deleteAssetOfRelationship(clazz,asset) {
    clazz.removeFromDocuments(asset)
    asset.delete()
  }

  private def createRelationShipCompanyToAssets(asset,companyId) {
    companyService.createS3AssetForCompany(asset, companyId)
  }

}
