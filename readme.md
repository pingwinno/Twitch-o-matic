[![Build Status](https://travis-ci.org/pingwinno/Twitch-o-matic.svg?branch=test)](https://travis-ci.org/pingwinno/Twitch-o-matic)

//TODO update before realese

It is an application for the automatic recording streams without mute on Twitch. The estimated working time is 24/7  mode. Streams are tracked via the Twitch webhook API.

ToM uses Twitch HLS API for downloading chunks with m3u8 playlist and preview. Unfortunately, it doesn't work (yet) if streamer has disabled VoD saving. App stores streams in raw HLS. You can compile it into one file using ffmpeg. 

ToM has page for management on address '''SERVERIP:MANAGEMENT_PORT'''. For now it not have autorization so be carefull. You can disable it by setting port to 0.

Requirements:
* Static IP or DDNS(not checked)
* ~3 GB storage per hour stream on 1080p
* Docker
Even Twitch-o-Matic is able to recover after an unexpected host reboot, strongly recommended to use several independent hosts at mission-critical use cases,

Installation:
2. Get docker-compose.yml from this repo.
3. Run it.
6. ?????
7. Profit!

Q: Can I setup auto-upload to cloud?
А: No, but you can execute script/command with cron.
For example - rcloud (https://rclone.org/)

Q: How to record streams from multiple streamers?
А: You can simply add subscription to streamer on management page.

Q: What type of streams does this app rec automaticly?
A: Live only. Rehost, retry, and other types will be filtered.

Q: Can I run it on Windows/Mac?
A: ToM developed only for Linux. You can try, but don't report issue. It will not be fixed.
