FROM java:8-jre
EXPOSE 3000

ADD *.jar /install/app.jar
WORKDIR /install

ENTRYPOINT ["java","-jar","app.jar"]