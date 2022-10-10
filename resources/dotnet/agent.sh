#!/bin/bash
cd /usr/share/jenkins
echo -e "java -jar agent.jar -jnlpUrl http://jenkins.k2so:${JENKINS_PORT}/computer/${JENKINS_AGENT_NAME}/jenkins-agent.jnlp -secret $JENKINS_SECRET -workDir /home/jenkins/agent"
java -jar agent.jar -jnlpUrl http://jenkins.k2so:${JENKINS_PORT}/computer/${JENKINS_AGENT_NAME}/jenkins-agent.jnlp -secret $JENKINS_SECRET -workDir "/home/jenkins/agent"
