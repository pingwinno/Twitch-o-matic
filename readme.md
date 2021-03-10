[![Build Status](https://travis-ci.org/pingwinno/Twitch-o-matic.svg?branch=test)](https://travis-ci.org/pingwinno/Twitch-o-matic)

//TODO update before realese

It is an application for the Twitch streams automatic recording. The estimated working time is 24/7. Streams are tracked via the Twitch webhook API.

ToM uses Twitch HLS API for downloading chunks with m3u8 playlist and preview. Unfortunately, it doesn't work (yet) if streamer has disabled VoD saving. ToM stores streams in raw HLS. You can compile it into one file using ffmpeg. 

ToM has management page using this adress template: '''SERVERIP:MANAGEMENT_PORT'''.

Requirements:
* Static IP or DDNS(not checked)
* ~3 GB storage per hour stream on 1080p
* Docker
Twitch-o-Matic is able to recover after an unexpected host reboot, strongly recommended to use several independent hosts at mission-critical use cases.

Installation:
1. Get docker-compose.yml from this repo.
2. Substitute your paths to it
3. Run it.
4. ?????
5. Profit!

Q: Can I setup an auto-upload to the cloud?
А: No, but you can execute script/command with cron.
For example - rcloud (https://rclone.org/)

Q: How to record streams from multiple streamers?
А: You can simply add subscription to streamer on management page.

Q: What type of streams does this app record automatically?
A: Live only. Rehost, retry, and other types will be filtered.

Q: Can I run it on Windows/Mac?
A: ToM is developed only for Linux. You can try, but don't report issue. It will not be fixed.
