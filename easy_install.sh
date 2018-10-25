#!/bin/bash
cd /tmp

### installing dependencies
echo -e "\033[36m Installing dependencies.\033[0m"
apt install -y maven git
### installing Twitch-o-matic
git clone https://github.com/pingwinno/Twitch-o-matic.git
cd ./Twitch-o-matic
mvn package
mkdir /usr/local/Twitch-o-matic/
mkdir  $HOME/.config/
mkdir  $HOME/db/
mv config.prop $HOME/.config/
mv ./target/Twitch-o-matic.jar /usr/local/Twitch-o-matic/
mv Twitch-o-matic.sh /usr/local/bin/
chmod +x /usr/local/bin/Twitch-o-matic.sh


cp Twitch-o-matic.service /etc/systemd/system/
cd ../../
rm -rf Twitch-o-matic
chmod 644 /etc/systemd/system/Twitch-o-matic.service
systemctl daemon-reload
systemctl enable Twitch-o-matic.service

echo -e "\033[32m Installation completed.\033[0m"
