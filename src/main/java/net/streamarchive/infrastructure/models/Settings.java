package net.streamarchive.infrastructure.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class Settings {
    @NonNull
    @NotBlank(message = "Should be not blank")
    private String user;
    @NonNull
    @NotBlank(message = "Should be not blank")
    private String userPass;
    @NonNull
    @URL(message = "Should be url")
    private String callbackAddress;
    @NonNull
    @NotBlank(message = "Should be not blank")
    private String recordStreamPath;
    @NonNull
    @Range(min = 80, max = 65555, message = "Should be in range min = 80, max = 65555")
    private int serverPort;
    @NonNull
    @URL(message = "Should be url")
    private String remoteDBAddress;
    @NonNull
    @NotBlank(message = "Should be not blank")
    private String dbUsername;
    @NonNull
    @NotBlank(message = "Should be not blank")
    private String dbPassword;
    @NonNull
    @NotBlank(message = "Should be not blank")
    private String clientID;
}
