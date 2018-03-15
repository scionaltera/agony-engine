FROM frolvlad/alpine-oraclejdk8:slim as build
MAINTAINER Peter Keeler <scion@agonyengine.com>
WORKDIR /opt/build
COPY . /opt/build/
RUN cd /opt/build \
&& apk update \
&& apk upgrade \
&& apk add --no-cache bash \
&& ./gradlew clean build -x check

FROM frolvlad/alpine-oraclejre8:slim
MAINTAINER Peter Keeler <scion@agonyengine.com>
EXPOSE 8080
COPY --from=build /opt/build/build/libs/agony-engine-*.jar /opt/mud/app.jar
CMD ["/usr/bin/java", "-jar", "/opt/mud/app.jar"]