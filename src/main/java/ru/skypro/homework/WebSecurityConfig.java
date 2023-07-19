package ru.skypro.homework;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.skypro.homework.repository.UserProfileRepository;
import org.springframework.http.HttpMethod;
import ru.skypro.homework.security.JpaUserDetailsService;
import ru.skypro.homework.security.MyUserDetailsService;
import ru.skypro.homework.service.ImageService;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

  private final UserProfileRepository userProfileRepository;
  private final MyUserDetailsService myUserDetailsService;
  private final ImageService imageService;

  public WebSecurityConfig(UserProfileRepository userProfileRepository, MyUserDetailsService myUserDetailsService, ImageService imageService) {
    this.userProfileRepository = userProfileRepository;
    this.myUserDetailsService = myUserDetailsService;
    this.imageService = imageService;
  }

  private static final String[] AUTH_WHITELIST = {
          "/swagger-resources/**",
          "/swagger-ui.html",
          "/v3/api-docs",
          "/webjars/**",
          "/login",
          "/register"
  };

  @Bean
  public JpaUserDetailsService userDetailsManager(){
    return new  JpaUserDetailsService(userProfileRepository, myUserDetailsService, imageService);
  }


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers(HttpMethod.PATCH).hasAnyAuthority("ADMIN", "USER")
            .mvcMatchers(AUTH_WHITELIST).permitAll()
            .antMatchers(HttpMethod.GET, "/ads", "/ads/image/*", "/image/**", "/users/*/getAvatar").permitAll()
            .antMatchers("/ads/**", "/users/**").authenticated()
            .and()
            .cors().and()
            .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .httpBasic(Customizer.withDefaults());
    return http.build();

  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
