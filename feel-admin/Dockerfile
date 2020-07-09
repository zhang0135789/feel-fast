FROM openjdk:8-jre-alpine

ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8
ENV  TIME_ZONE Asiz/Shanghai
RUN apk add --no-cache tzdata  \
##设置时区
&& echo "Asia/Shanghai" > /etc/timezone \
&& ln -sf /usr/share/zoneinfo/${TIME_ZONE} /etc/localtime


VOLUME /tmp

ADD ./target/feel-admin.jar /feel-admin.jar

EXPOSE 10080

ENTRYPOINT ["java","-XX:+UnlockExperimentalVMOptions","-XX:+UseCGroupMemoryLimitForHeap","-Djava.security.egd=file:/dev/./urandom","-jar","/feel-admin.jar"]
