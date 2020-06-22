mvn clean package
scp -P 22556 -i ~/key ~/IdeaProjects/Twitch-o-matic/target/twitch-o-matic-0.0.1-SNAPSHOT.jar superuser@80.66.80.208:~/webapp.jar