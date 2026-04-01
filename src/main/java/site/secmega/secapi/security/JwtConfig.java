package site.secmega.secapi.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {

    private final RsaKeyProperties rsaKeyProperties;

    // ===== Access Token =====

    @Primary
    @Bean
    RSAKey rsaKey() {
        return new RSAKey.Builder(rsaKeyProperties.getAccessToken().getPublicKey())
                .privateKey(rsaKeyProperties.getAccessToken().getPrivateKey())
                .keyID("access-token-key")
                .build();
    }

    @Primary
    @Bean
    JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) {
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, ctx) -> jwkSelector.select(jwkSet);
    }

    @Primary
    @Bean
    JwtDecoder jwtDecoder(RSAKey rsaKey) throws JOSEException {
        return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
    }

    @Primary
    @Bean
    JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    // ===== Refresh Token =====

    @Bean("rsaKeyRefreshToken")
    RSAKey rsaKeyRefreshToken() {
        return new RSAKey.Builder(rsaKeyProperties.getRefreshToken().getPublicKey())
                .privateKey(rsaKeyProperties.getRefreshToken().getPrivateKey())
                .keyID("refresh-token-key")
                .build();
    }

    @Bean("jwkSourceRefreshToken")
    JWKSource<SecurityContext> jwkSourceRefreshToken(
            @Qualifier("rsaKeyRefreshToken") RSAKey rsaKey) {
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, ctx) -> jwkSelector.select(jwkSet);
    }

    @Bean("jwtDecoderRefreshToken")
    JwtDecoder jwtDecoderRefreshToken(
            @Qualifier("rsaKeyRefreshToken") RSAKey rsaKey) throws JOSEException {
        return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
    }

    @Bean("jwtEncoderRefreshToken")
    JwtEncoder jwtEncoderRefreshToken(
            @Qualifier("jwkSourceRefreshToken") JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

}
