FROM frolvlad/alpine-oraclejre8:slim
MAINTAINER Peter Keeler <scion@agonyengine.com>
EXPOSE 8080
COPY agony-engine-*.jar /opt/mud/app.jar
CMD ["/usr/bin/java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005", "-jar", "/opt/mud/app.jar"]