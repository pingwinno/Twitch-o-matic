package net.streamarchive.infrastructure.configuration;


import net.streamarchive.infrastructure.SettingsProvider;
import net.streamarchive.infrastructure.handlers.db.ArchiveDBHandler;
import net.streamarchive.infrastructure.handlers.db.EnabledDBHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@EnableScheduling
@RestController
@Configuration
@EnableWebSecurity
@Profile("production")
public class WebConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private SettingsProvider settingsProperties;

    @Value("${net.streamarchive.auth.user}")
    private String user;

    @Value("${net.streamarchive.auth.password}")
    private String password;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/login**", "/streams/**", "/handler/**", "/callback/", "/webjars/**", "/error**", "/status**", "/twitch/oauth")
                .permitAll()
                .anyRequest()
                .authenticated().and().formLogin().and()
                .logout().logoutSuccessUrl("/").clearAuthentication(true).and().csrf().disable().httpBasic();
        ;
    }

    @RequestMapping("/unauthenticated")
    public String unauthenticated() {
        return "redirect:/?error=true";
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        if (settingsProperties.isInitialized()) {
            user = settingsProperties.getUser();
            password = settingsProperties.getPassword();
        }
        auth.inMemoryAuthentication()
                .withUser(user).password(passwordEncoder().encode(password)).authorities("ROLE_USER");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    RestTemplate restTemplateWithCredentials(RestTemplateBuilder restTemplateBuilder) {
        if (settingsProperties.isInitialized()) {
            return restTemplateBuilder.basicAuthentication(settingsProperties.getDbUsername(), settingsProperties.getDbPassword()).build();
        } else {
            return restTemplateBuilder.build();
        }
    }

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    @Bean
    ArchiveDBHandler archiveDBHandler() {
        return new EnabledDBHandler();
    }
}
