FROM jenkins/inbound-agent:4.11.2-4 as jenkins-agent
WORKDIR /home/jenkins/agent
COPY agent.sh .
LABEL com.datassential.component="ds-dotnet6-jenkins-agent-container" \
      name="dotnet/dotnet6-jenkins-agent-4.11" \
      version="6.0" \
      architecture="x86_64" \
      release="2" \
      io.k8s.display-name="Jenkins Agent .NET 6" \
      io.k8s.description="The jenkins agent dotnet image has the dotnet tools on top of the jenkins agent base image." \
      io.aws.tags="env:operations,app:jenkins,service:eks"

RUN echo "Run cd $HOME && ./agent.sh"

FROM mcr.microsoft.com/dotnet/sdk:6.0 as dotnet-6
WORKDIR /home/jenkins
ARG user=jenkins
ARG group=jenkins
ARG uid=1000
ARG gid=1000
RUN groupadd -g ${gid} ${group} && useradd -c "Jenkins user" -d /home/${user} -u ${uid} -g ${gid} -m ${user}
ENV LANG C.UTF-8
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH "${JAVA_HOME}/bin:${PATH}"
COPY --from=jenkins-agent /home/jenkins/agent/agent.sh .
CMD [ "./agent.sh" ]



