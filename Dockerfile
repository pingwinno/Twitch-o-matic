FROM openjdk:11
RUN apt update && apt upgrade -y && apt install ffmpeg -y
COPY . /tmp
WORKDIR /tmp
RUN sed 's!~/status!/etc/streamarchive/status!' src/main/resources/application.properties > src/main/resources/application.properties.copy \
&& mv src/main/resources/application.properties.copy src/main/resources/application.properties \
&& ./mvnw package && rm -rf ~/.m2 \
&& mv target/twitch-o-matic-0.0.1-SNAPSHOT.jar /usr/sbin/streamarchive.jar && mv postprocessing.sh /usr/sbin/postprocessing.sh \
 && cd / && rm -rf /tmp/*
WORKDIR /usr/sbin/
RUN mkdir /etc/streamarchive/
CMD ["java", "-jar", "streamarchive.jar"]
EXPOSE 8080
