FROM java:8-jre
EXPOSE 8081

RUN adduser runner
RUN echo runner:123456 | chpasswd
USER runner
RUN id -a

ADD *.jar /install/app.jar
WORKDIR /install

ENTRYPOINT ["java","-Djava.io.tmpdir=/tmp/app", "-jar","app.jar"]