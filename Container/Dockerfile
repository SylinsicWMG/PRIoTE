FROM maven:3.8.1-openjdk-15

EXPOSE 3330

COPY files/ /usr/src/mymaven
COPY mvn /tmp/mvn
COPY strace /usr/bin
COPY libs /usr/lib/x86_64-linux-gnu/ 

WORKDIR /usr/src/mymaven

RUN ./build.sh

ENTRYPOINT ["./entrypoint.sh"]
