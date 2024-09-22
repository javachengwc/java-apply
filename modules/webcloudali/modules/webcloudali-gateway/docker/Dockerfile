FROM openjdk:11-jdk-alpine
COPY webcloudali-gateway-1.0.0-SNAPSHOT.jar /app/
WORKDIR /app/
#修改docker时区为东八区，规避应用程序和北京时间相差8小时问题
ENV TZ=Asia/Shanghai
ENTRYPOINT [ "sh" ,"-c", "java ${JAVA_OPTS} -jar /app/webcloudali-gateway-1.0.0-SNAPSHOT.jar" ]