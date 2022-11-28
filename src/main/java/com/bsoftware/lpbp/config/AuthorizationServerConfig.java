package com.bsoftware.lpbp.config;

import com.bsoftware.lpbp.config.token.CustomTokenEnhancer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    public final AuthenticationManager authenticationManager;
    private final PasswordEncoder ps = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private final CorsFilter corsConfiguration;

    public AuthorizationServerConfig(AuthenticationManager authenticationManagerBean, CorsFilter corsConfiguration) {
        this.authenticationManager = authenticationManagerBean;
        this.corsConfiguration = corsConfiguration;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().withClient("angular")
                .secret(ps.encode("@ngul@r0"))
                .scopes("read", "write")
                .authorizedGrantTypes("password", "refresh_token")
                .accessTokenValiditySeconds(3600 * 24)
                .refreshTokenValiditySeconds(3600 * 24)
                .and()
                .withClient("mobile")
                .secret("M0b1l3")
                .scopes("read")
                .authorizedGrantTypes("password", "refresh_token")
                .accessTokenValiditySeconds(3600)
                .refreshTokenValiditySeconds(3600 * 24);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        TokenEnhancerChain chain = new TokenEnhancerChain();
        chain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
        endpoints
                .tokenStore(tokenStore())
                .tokenEnhancer(chain)
                .reuseRefreshTokens(false)
                .authenticationManager(authenticationManager);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.addTokenEndpointAuthenticationFilter(this.corsConfiguration);
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey("Octa12js34aUgUOct@");
        return jwtAccessTokenConverter;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }


}
