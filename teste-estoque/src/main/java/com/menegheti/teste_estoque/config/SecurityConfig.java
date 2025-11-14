package com.menegheti.teste_estoque.config;  // Ajuste pacote

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//import org.springframework.context.annotation.Bean;


@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity(prePostEnabled = true)  // Para @PreAuthorize nos controllers
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
       return new BCryptPasswordEncoder();
    }

    /*@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();  // Usa UsuarioService como UserDetailsService auto-detectado
    }*/
    


    /*
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // Desabilita CSRF para API REST (Postman/Frontend)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Sem sessions (stateless)
                .authorizeHttpRequests(authz -> authz
                        // Único Público: Login POST
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()  // Login público (sem auth)
                        
                        // Auth: Cadastro e Listar Usuários só ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/auth/cadastro").hasRole("ADMIN")  // POST cadastro só ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/auth/usuarios").hasRole("ADMIN")  // GET listar usuários só ADMIN
                        
                        // Perfil: Requer autenticação (qualquer logado), mas não role específica
                        .requestMatchers(HttpMethod.GET, "/api/auth/perfil").authenticated()  // GET perfil (com Basic Auth)
                        
                        // Usuários CRUD: Só ADMIN
                        .requestMatchers("/api/usuarios/**").hasRole("ADMIN")  // Todos os subpaths (GET/POST/PUT/DELETE)
                        
                        // Produtos: GET/POST/PUT/DELETE para ADMIN ou ESTOQUISTA
                        .requestMatchers("/api/produtos/**").hasAnyRole("ADMIN", "ESTOQUISTA")
                        
                        // Movimentações: 
                        // - GETs (listar/filtros): ADMIN ou ESTOQUISTA
                        // - POST/PUT/DELETE: ADMIN ou ESTOQUISTA
                        .requestMatchers(HttpMethod.GET, "/api/movimentacoes/**").hasAnyRole("ADMIN", "ESTOQUISTA")  // GETs protegidos agora
                        .requestMatchers(HttpMethod.POST, "/api/movimentacoes").hasAnyRole("ADMIN", "ESTOQUISTA")  // POST criar
                        .requestMatchers(HttpMethod.PUT, "/api/movimentacoes/**").hasAnyRole("ADMIN", "ESTOQUISTA")  // PUT atualizar (inclui /{id})
                        .requestMatchers(HttpMethod.DELETE, "/api/movimentacoes/**").hasAnyRole("ADMIN", "ESTOQUISTA")  // DELETE (inclui /{id})
                        
                        // Fornecedores: Só ADMIN (todos métodos)
                        .requestMatchers("/api/fornecedores/**").hasRole("ADMIN")
                        
                        // Relatórios/Dashboard: Só ADMIN (todos métodos)
                        .requestMatchers("/api/relatorios/**").hasRole("ADMIN")
                        .requestMatchers("/api/dashboard/**").hasRole("ADMIN")
                        
                        // Geral: Qualquer outro requer autenticação
                        .anyRequest().authenticated()  // Ex.: endpoints não listados precisam de login
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))  // 401 para não autenticado
                );  // Habilita Basic Auth para testes (email:senha no header)

        return http.build();
    }*/
}
