package com.modulus.uno

import com.modulus.uno.machine.MachineEventImplementer
import in.ashwanthkumar.slack.webhook.Slack
import in.ashwanthkumar.slack.webhook.SlackMessage

class EventSlackImplementerService implements MachineEventImplementer {

  void executeEvent(String className,Long instanceId){

    def webhookUrl = "https://hooks.slack.com/services/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"
      new Slack(webhookUrl)
        .icon(":envelope:")
        .sendToChannel("techminds")
        .displayName("Event Slack Implementer Service")
        .push(new SlackMessage("Cambio de Estado de ").bold("${className}, id: ${instanceId}"));
  }

}
