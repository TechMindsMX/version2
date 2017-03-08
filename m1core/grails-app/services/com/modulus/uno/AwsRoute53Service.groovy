package com.modulus.uno

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.route53.AmazonRoute53Client
import com.amazonaws.services.route53.model.*
import grails.util.Holders as H

class AwsRoute53Service {

    def grailsApplication

    private final String AWS_ACCESS_KEY = H.grailsApplication.config.grails.plugin.awssdk.accessKey
    private final String AWS_SECRET_KEY = H.grailsApplication.config.grails.plugin.awssdk.secretKey
    private final String HOST_ZONE_ID = H.grailsApplication.config.grails.plugin.awssdk.host.zone.id
    private final String VALUE_HOST = H.grailsApplication.config.grails.plugin.awssdk.value.host.ip

   void createRecordSet(String domainName,String completeUrl, String valueHost) {
    AmazonRoute53Client route53Client = new AmazonRoute53Client(new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY));
    ResourceRecord resourceRecord = new ResourceRecord();
    resourceRecord.setValue(valueHost);

    List<ResourceRecord> resourceRecords = new ArrayList<ResourceRecord>();
    resourceRecords.add(resourceRecord);

    ResourceRecordSet recordSet = new ResourceRecordSet();
    recordSet.setName("${domainName}${completeUrl}");
    recordSet.setType("A");
    recordSet.setTTL(300L);
    recordSet.setResourceRecords(resourceRecords);

    Change change = new Change(ChangeAction.CREATE, recordSet);

    List<Change> changes = new ArrayList<Change>();
    changes.add(change);
    ChangeBatch changeBatch = new ChangeBatch(changes);

    ChangeResourceRecordSetsRequest request = new ChangeResourceRecordSetsRequest();
    request.setHostedZoneId(HOST_ZONE_ID);
    request.setChangeBatch(changeBatch);

    route53Client.changeResourceRecordSets(request);
  }

  HostedZone getHostedZoneById() {
    AmazonRoute53Client route53Client = new AmazonRoute53Client(new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY));
    GetHostedZoneRequest request = new GetHostedZoneRequest();
    request.setId(HOST_ZONE_ID);
    GetHostedZoneResult result = route53Client.getHostedZone(request);
    HostedZone hostedZone = result.getHostedZone();
    return hostedZone;
  }

  def getResourceRecordSet() {
    AmazonRoute53Client route53Client = new AmazonRoute53Client(new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY));
    ListResourceRecordSetsRequest request = new ListResourceRecordSetsRequest();
    request.setHostedZoneId(HOST_ZONE_ID);
    ListResourceRecordSetsResult result = route53Client.listResourceRecordSets(request);
    return result.getResourceRecordSets();
  }

}
