package net.streamarchive.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TelegramServerPool {
    int index;
    private List<String> pool = new ArrayList<>();
    @Autowired
    private RestTemplate restTemplate;
    @Value("${net.streamarchive.telegram.instances}")
    private int instancesNumber;
    @Value("${net.streamarchive.telegram.address}")
    private String tgServer;

    @PostConstruct
    private void init() {
        for (int i = 0; i < instancesNumber; i++) {
            String address = tgServer + i;
            if (checkConnection(address)) {
                pool.add(address);
            } else {
                log.trace("Server {} is not accessible. Skip...", address);
            }
        }
    }

    public String write(byte[] data) {
        var tgServer = getAddress(true);
        HttpEntity<byte[]> requestEntity =
                new HttpEntity<>(data);
        ResponseEntity<String> response = restTemplate.exchange(tgServer, HttpMethod.POST,
                requestEntity,
                String.class);
        return response.getBody();
    }

    public InputStream read(long id) throws IOException {
        return new URL(getAddress(false) + "/" + id).openStream();
    }

    public synchronized String getAddress(boolean isUpload) {
        log.warn("pool index {}", index);

        if (index >= pool.size() || isUpload) {
            index = 0;
        }
        log.warn("pool index {}", index);
        String address = pool.get(index);
        if (checkConnection(address)) {
            index++;
            return address;
        } else {
            pool.remove(index);
            return getAddress(false);
        }
    }

    private boolean checkConnection(String address) {
        try {
            restTemplate.exchange(address + "/connection/test"
                    ,
                    HttpMethod.GET,
                    null,
                    String.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
