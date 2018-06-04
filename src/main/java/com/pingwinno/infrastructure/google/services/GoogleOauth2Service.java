package com.pingwinno.application;

    import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Tokeninfo;
import com.pingwinno.infrastructure.GoogleServiceCore;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;



public class GoogleOauth2 {




        private static final List<String> SCOPES = Arrays.asList(
                "https://www.googleapis.com/auth/drive");
        private static Oauth2 oauth2;
        private static GoogleClientSecrets clientSecrets;
        private static Credential authenticatedCredentials;



        private GoogleOauth2 (){
            }

        public static void startAuthentication() throws Exception {
                Credential credential;
            // authorization
                credential = authorize();
                // set up global Oauth2 instance
                oauth2 = new Oauth2.Builder(GoogleServiceCore.HTTP_TRANSPORT, GoogleServiceCore.JSON_FACTORY, credential).setApplicationName(
                        GoogleServiceCore.APPLICATION_NAME).build();
                // run commands
                authenticatedCredentials = credential;
                tokenInfo(credential.getAccessToken());
            System.out.println("Credentials saved to " + GoogleServiceCore.DATA_STORE_DIR.getAbsolutePath());
                // success!
        }

        private static Credential authorize() throws Exception {
            // load client secrets
            clientSecrets = GoogleClientSecrets.load(GoogleServiceCore.JSON_FACTORY,
                    new InputStreamReader(GoogleOauth2.class.getResourceAsStream("/client_secrets.json")));
            // set up authorization code flow
            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    GoogleServiceCore.HTTP_TRANSPORT, GoogleServiceCore.JSON_FACTORY, clientSecrets, SCOPES).setDataStoreFactory(
                    GoogleServiceCore.DATA_STORE_FACTORY).build();
            // authorize
            return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        }
        public static String getToken() {
          return authenticatedCredentials.getAccessToken();
        }

        private static void tokenInfo(String accessToken) throws IOException {
            header("Validating a token");
            Tokeninfo tokeninfo = oauth2.tokeninfo().setAccessToken(accessToken).execute();
            System.out.println(tokeninfo.toPrettyString());
            if (!tokeninfo.getAudience().equals(clientSecrets.getDetails().getClientId())) {
                System.err.println("ERROR: audience does not match our client ID!");
            }
        }


        static void header(String name) {
            System.out.println();
            System.out.println("================== " + name + " ==================");
            System.out.println();
        }
    }

