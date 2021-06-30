FROM maven

COPY files/ /usr/src/mymaven

WORKDIR /usr/src/mymaven

RUN ./build.sh

ENTRYPOINT ["./entrypoint.sh"]
