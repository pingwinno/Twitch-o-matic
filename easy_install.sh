#!/bin/bash
cd /tmp


### installing Twitch-o-matic

FILE=/usr/local/twitch-o-matic/twitch-o-matic.jar
if [ -f $FILE ]; then
  echo "tom installed. Updating...."
  git clone https://github.com/pingwinno/Twitch-o-matic.git
  cd ./Twitch-o-matic
  mvn package
else
### installing dependencies
  echo -e "\033[36m Installing dependencies.\033[0m"
  apt update
  apt upgrade -y
  apt install -y maven git jsvc
  git clone https://github.com/pingwinno/twitch-o-matic.git
  cd ./Twitch-o-matic
  mvn package
  adduser --system --no-create-home --group tom-daemon
  mkdir /usr/local/twitch-o-matic/
  mkdir /usr/local/twitch-o-matic//db/
  chown tom-daemon /usr/local/twitch-o-matic//db/
  chmod u+w /usr/local/twitch-o-matic//db/
  mkdir /var/log/tom/
  chown tom-daemon /var/log/tom/
  chmod u+w /var/log/tom/
  mkdir /etc/tom/
  mv config.prop /etc/tom/
fi


mv ./target/twitch-o-matic.jar /usr/local/twitch-o-matic/
mv twitch-o-matic.sh /usr/local/bin/
chmod +x /usr/local/bin/twitch-o-matic.sh


cp twitch-o-matic.service /etc/systemd/system/twitch-o-matic.service
cd ../
rm -rf twitch-o-matic
chmod 644 /etc/systemd/system/twitch-o-matic.service
systemctl daemon-reload
systemctl enable twitch-o-matic.service

echo -e "\033[32m Installation completed.\033[0m"
