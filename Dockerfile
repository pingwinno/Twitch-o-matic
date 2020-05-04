FROM openjdk:11
COPY . /tmp
WORKDIR /tmp
RUN ls
RUN ./mvnw package && rm -rf ~/.m2 && mv target/twitch-o-matic-0.0.1-SNAPSHOT.jar /usr/sbin/streamarchive.jar && cd / && rm -rf /tmp/*
WORKDIR /usr/sbin/
RUN mkdir /etc/streamarchive/
CMD java -jar streamarchive.jar
EXPOSE 8080