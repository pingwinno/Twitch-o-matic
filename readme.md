It is an application for the automatic recording streams without mute on Twitch. The estimated work time is  24 /7  mode. Streams are tracked via the Twitch webhook API.

It  can record in two modes:
Streamlink - run streamlink via command line with -o option and store in a specified folder. Skip around first 3 minutes of the stream because of webhook API delay.
VOD - use Twitch HLS API for downloading and compile chunks in one file without FFMPEG (like streamlink). This mode preferred, but not work if streamer has disabled VoD saving.

-----------------------

So far, Twitch-o-Matic is unable to recover after an unexpected host reboot. At mission-critical use cases, it is recommended to use several independent hosts.

-----------------------
Q: Can I setup auto-upload to cloud?
А: No, but you can execute script/command after the end of the stream.
For example - rcloud (https://rclone.org/)

Q: How record multiple streamers?
А: Run app on multiple host, or container instance (LXD in exmpl). Another way is manually run multiple instances of the app. Each instance needs a dedicated port.

Q: What type of streams this app rec?
A: Live only. Rehost, retry, and other types will be filtered.
