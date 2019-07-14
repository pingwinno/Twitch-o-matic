package net.streamarchive.domain;

import net.streamarchive.application.CommandLineExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
public class DashProcessing {
    @Autowired
    CommandLineExecutor commandLineExecutor;

    public void start(String path, List<String> qualities) {
        List<String> streams = new LinkedList<>();
        for (String quality : qualities) {
            String streamPath = String.join("/", path, quality, "index-dvr.m3u8");
            String destinationPath = path + "/" + quality + ".mp4";
            streams.add(destinationPath);
            commandLineExecutor.execute("ffmpeg", "-i", streamPath, "-c:v", "copy", "-c:a", "copy", destinationPath, "-y");
        }
        qualities.sort(Collections.reverseOrder());
        List<String> commandLine = new LinkedList<>();
        commandLine.add("ffmpeg");
        for (String stream : streams) {
            commandLine.add("-i");
            commandLine.add(stream);
        }
        commandLine.add("-c");
        commandLine.add("copy");
        commandLine.add("-map");
        commandLine.add("0");
        for (int i = 1; i < streams.size(); i++) {
            commandLine.add("-map");
            commandLine.add(i + ":v");
        }
        commandLine.add("-movflags");
        commandLine.add("empty_moov+default_base_moof+frag_keyframe");
        commandLine.add("-probesize");
        commandLine.add("200000");
        commandLine.add("-vtag");
        commandLine.add("avc1");
        commandLine.add("-atag");
        commandLine.add("mp4a");
        commandLine.add("-f");
        commandLine.add("dash");
        commandLine.add(path + "/index.mpd");
        commandLineExecutor.setPath(path);
        String[] commandArray = new String[commandLine.size()];
        commandLineExecutor.execute(commandLine.toArray(commandArray));
    }
}
