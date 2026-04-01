package site.secmega.secapi.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@ConfigurationProperties(prefix = "app.security")
public class RsaKeyProperties {

    private TokenKeys accessToken = new TokenKeys();
    private TokenKeys refreshToken = new TokenKeys();

    public TokenKeys getAccessToken() { return accessToken; }
    public void setAccessToken(TokenKeys accessToken) { this.accessToken = accessToken; }

    public TokenKeys getRefreshToken() { return refreshToken; }
    public void setRefreshToken(TokenKeys refreshToken) { this.refreshToken = refreshToken; }

    public static class TokenKeys {
        private RSAPrivateKey privateKey;
        private RSAPublicKey publicKey;

        public RSAPrivateKey getPrivateKey() { return privateKey; }
        public void setPrivateKey(RSAPrivateKey privateKey) { this.privateKey = privateKey; }

        public RSAPublicKey getPublicKey() { return publicKey; }
        public void setPublicKey(RSAPublicKey publicKey) { this.publicKey = publicKey; }
    }
}