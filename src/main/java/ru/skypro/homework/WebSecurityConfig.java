package ru.skypro.homework;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.skypro.homework.repository.UserProfileRepository;
import org.springframework.http.HttpMethod;
import ru.skypro.homework.security.JpaUserDetailsService;
import ru.skypro.homework.security.MyUserDetailsService;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

  private final UserProfileRepository userProfileRepository;
  private final MyUserDetailsService myUserDetailsService;

  public WebSecurityConfig(UserProfileRepository userProfileRepository, MyUserDetailsService myUserDetailsService) {
    this.userProfileRepository = userProfileRepository;
    this.myUserDetailsService = myUserDetailsService;
  }

  private static final String[] AUTH_WHITELIST = {
          "/swagger-resources/**",
          "/swagger-ui.html",
          "/v3/api-docs",
          "/webjars/**",
          "/login",
          "/register",
          "/ads/image/*",
          "/ads/*",
          "/ads"

  };


//  @Bean
//  public InMemoryUserDetailsManager userDetailsService(){
//    List<UserProfile> users = new ArrayList<>(userProfileRepository.findAll());
//   // if (users.size() > 0) {
//      List<UserDetails> userDetailsList = new ArrayList<>();
//      users.stream()
//              .forEach(userProfile -> {
//                userDetailsList.add(
//                        User.withUsername(
//                                        userProfile.getEmail())
//                                .password(passwordEncoder().encode(userProfile.getPassword()))
//                                .roles(userProfile.getRole().toString()).build()
//                );
//              });
//
//
//      return new InMemoryUserDetailsManager(userDetailsList);
//
// //   }
//
//  }

  @Bean
  public JpaUserDetailsService userDetailsManager(){
    return new  JpaUserDetailsService(userProfileRepository, myUserDetailsService);
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
            .csrf().disable()
            .authorizeHttpRequests((auth) ->
                    auth
                            .mvcMatchers(AUTH_WHITELIST).permitAll()
                            .antMatchers(HttpMethod.GET,
                                    "/ads/*",
                                    "ads/image/*",
                                    "/users/image/*",
                                    "ads/*/comments",
                                    "users/*/getAvatar"
                            ).permitAll()
                            .antMatchers("/ads/**", "/users/**").authenticated()
            )
            .cors()
            .and()
            .httpBasic(Customizer.withDefaults());
    return http.build();
  }

//  @Bean
//  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//    http
//            .csrf().disable()
//            .httpBasic(Customizer.withDefaults())
//            .authorizeHttpRequests(matcherRegistry ->
//                matcherRegistry
//                    .antMatchers(AUTH_WHITELIST).permitAll()
//                    .antMatchers(HttpMethod.GET, "/ads/*", "ads/image/*", "/users/image/*").permitAll()
//                    .antMatchers("/ads/**", "/users/**").authenticated()
//            )
//            .cors().and();
//    return http.build();
//
//  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
