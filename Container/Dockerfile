FROM maven:3.8.1-openjdk-15

EXPOSE 3330

WORKDIR /usr/src/mymaven

COPY files/ /usr/src/mymaven
RUN cp -r CustomJava/* MagmaCore/src/main/java && rm -rf CustomJava || :

ENV JAVA_HOME=/usr/java/openjdk-15
ENV JAVA_VERSION=15.0.2
ENV LANG=C.UTF-8
ENV MAVEN_HOME=/usr/share/maven
ENV MAVEN_CONFIG=/root/.m2
ENV PATH=/usr/java/openjdk-15/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

RUN ./build.sh

ENTRYPOINT ["./entrypoint.sh"]
