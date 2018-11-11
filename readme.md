It is an application for the automatic recording streams without mute on Twitch. The estimated working time is 24/7  mode. Streams are tracked via the Twitch webhook API.

ToM uses Twitch HLS API for downloading chunks with m3u8 playlist and preview. Unfortunately, it doesn't work (yet) if streamer has disabled VoD saving. App stores streams in raw HLS. You can compile it into one file using ffmpeg. 

ToM has page for management on address '''SERVERIP:MANAGEMENT_PORT'''. For now it not have autorization so be carefull. You can disable it by setting port to 0.

Requirements:
* Static IP or DDNS(not checked)
* Linux
* ~3 GB storage per hour stream on 1080p
* Oracle JRE 8 ( may not work with OpenJRE/JDK)

Even Twitch-o-Matic is able to recover after an unexpected host reboot, strongly recommended to use several independent hosts at mission-critical use cases,

Installation:
1. Remove OpenJRE/JDK or other JVM's
2. Install Oracle JRE 8. You can try to use other versions, but it's not tested.
3. Run ```sudo curl -s https://raw.githubusercontent.com/pingwinno/Twitch-o-matic/master/easy_install.sh | sudo bash /dev/stdin ```
4. Configure ToM via ```/etc/tom/config.prop```
4.1. (optional) if you behind NAT forvard TwitchServer port needed.
5. ```systemctl start twitch-o-matic```
6. ?????
7. Profit!

Q: Can I setup auto-upload to cloud?
А: No, but you can execute script/command after the end of the stream.
For example - rcloud (https://rclone.org/)

Q: How to record streams from multiple streamers?
А: Run app on multiple hosts, or container instance (LXD in exmpl). Another way is manually run multiple instances of the app. Each instance needs a dedicated port.

Q: What type of streams does this app rec?
A: Live only. Rehost, retry, and other types will be filtered.

Q: Can I run it on Windows/Mac?
A: ToM developed only for Linux. You can try, but don't report issue. It will not be fixed.
