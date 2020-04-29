package net.streamarchive.infrastructure.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
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
    @URL(message = "Should have port if it isn't standart for http(80)/https(443) .\n" +
            "Example http://external.address:10001")
    private String callbackAddress;
    @NonNull
    @URL(message = "Should have / on the end like http://db.com/ ")
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
    @NonNull
    @NotBlank(message = "Should be not blank")
    private String clientSecret;

}
