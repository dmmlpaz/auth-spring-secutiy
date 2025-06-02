package smarttmtweb.libauth.authentication;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(customAuthenticationProvider)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/login")
                        .successHandler((request, response, authentication) -> {
                            // Puedes personalizar la respuesta de éxito
                            Usuadmin usuadmin = (Usuadmin) authentication.getPrincipal();
                            response.setContentType("application/json");
                            response.getWriter().write(String.format(
                                    "{\"status\": \"success\", \"usuario\": \"%s\"}",
                                    usuadmin.getUsaddoid()
                            ));
                        })
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}