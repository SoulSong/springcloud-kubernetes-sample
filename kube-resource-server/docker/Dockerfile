FROM java:8-jre
EXPOSE 3001

ADD *.jar /install/app.jar
WORKDIR /install

ENTRYPOINT ["java","-jar","app.jar"]