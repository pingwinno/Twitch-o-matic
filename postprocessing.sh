#!/bin/bash

previewNumber=0
startTime=0
echo "$1"
streamPath="${1}/index-dvr.m3u8"
echo "stream path is $streamPath"
durationFloat=$(ffprobe "$streamPath" -show_format 2>&1 | sed -n 's/duration=//p')
duration=${durationFloat%.*}
offset=120
echo "start time is $durationFloat"
echo "formatted start time is $duration"
mkdir -p timeline_preview
while (( duration > startTime ))
do
echo "$startTime"
ffmpeg  -ss $startTime -i "$streamPath" -s 256x144 -vframes 1 timeline_preview/preview$previewNumber.jpg -y
startTime=$(( startTime + offset ))
previewNumber=$(( previewNumber+1 ))
done


previewNumber=0
startTime=0
offset=$(( duration/9))
mkdir -p animated_preview
while (( duration > startTime ))
do
echo "$startTime"
ffmpeg  -ss $startTime -i "$streamPath" -s 640x360 -vframes 1 animated_preview/preview$previewNumber.jpg -y
startTime=$(( startTime+offset ))
previewNumber=$(( previewNumber+1 ))
done
