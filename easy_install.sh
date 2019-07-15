#!/bin/bash
cd /tmp


### installing Twitch-o-matic

FILE=/usr/local/twitch-o-matic/twitch-o-matic.jar
if [[ -f ${FILE} ]]; then
  echo "tom installed. Updating...."
  git clone https://github.com/pingwinno/twitch-o-matic.git
  cd ./twitch-o-matic
  mvn package
else
### installing dependencies
  echo -e "\033[36m Installing dependencies.\033[0m"
  apt install ffmpeg
  apt install git
  apt install maven
  git clone https://github.com/pingwinno/twitch-o-matic.git
  cd ./twitch-o-matic
  mvn package
  adduser --system --group tom-daemon
  mkdir /usr/local/twitch-o-matic/
  mkdir /var/log/tom/
  chown tom-daemon /var/log/tom/
  chmod u+w /var/log/tom/
  mv application.properties /home/tom-daemon/
fi

cp twitch-o-matic.service /etc/systemd/system/twitch-o-matic.service
cd ../
rm -rf twitch-o-matic
chmod 644 /etc/systemd/system/twitch-o-matic.service
systemctl daemon-reload
systemctl enable twitch-o-matic.service

echo -e "\033[32m Installation completed.\033[0m"
