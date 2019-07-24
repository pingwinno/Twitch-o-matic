package net.streamarchive.infrastructure.configuration;

import net.streamarchive.infrastructure.GithubPrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableScheduling
@EnableOAuth2Sso
@RestController
@Configuration
@PropertySource("file:${user.home}/application.properties")
public class WebConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/login**", "/handler/**", "/callback/", "/webjars/**", "/error**", "/status**")
                .permitAll()
                .anyRequest()
                .authenticated().and().logout().logoutSuccessUrl("/").clearAuthentication(true).and().csrf().disable();
    }

    @RequestMapping("/unauthenticated")
    public String unauthenticated() {
        return "redirect:/?error=true";
    }

    @Bean
    public PrincipalExtractor githubPrincipalExtractor() {
        return new GithubPrincipalExtractor();
    }



}
