ARG REPO=176275544602.dkr.ecr.ap-south-1.amazonaws.com
FROM ${REPO}/openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 9003
ADD target/*.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]