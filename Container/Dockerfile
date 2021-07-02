FROM maven:3.8.1-openjdk-15

EXPOSE 3330

COPY files/ /usr/src/mymaven

WORKDIR /usr/src/mymaven

RUN ./build.sh

ENTRYPOINT ["./entrypoint.sh"]
