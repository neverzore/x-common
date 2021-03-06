/*
 * Copyright (c) 2020 neverzore (https://github.com/neverzore).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.neverzore.common.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.apache.commons.lang3.StringUtils;
import tech.neverzore.common.security.jwt.support.JwtManager;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * JWT Token源
 *
 * @author zhouzb
 * @date 2019/5/28
 */
public class DefaultJwtManager implements JwtManager {
    private static final String BEARER = "Bearer";
    private static final String ISSUER = "neverzore";
    private static final String CHARSET = "UTF-8";
    private static final Long DURATION = -1L;

    private String secret;
    private SecretKey secretKey;
    private SignatureAlgorithm algorithm;
    private JwtParser parser;

    private String issuer;

    public DefaultJwtManager(String secret) {
        this(secret, SignatureAlgorithm.HS512.getValue());
    }

    public DefaultJwtManager(String secret, String algorithm) {
        this(secret, algorithm, StringUtils.EMPTY);
    }

    public DefaultJwtManager(String secret, String algorithm, String issuer) {
        this(secret, algorithm, issuer, CHARSET);
    }

    public DefaultJwtManager(String secret, String algorithm, String issuer, String charset) {
        this.secret = secret;
        this.algorithm = SignatureAlgorithm.forName(algorithm);
        this.issuer = issuer;

        try {
            this.secretKey = new SecretKeySpec(secret.getBytes(Charset.forName(charset)), this.algorithm.getJcaName());
        } catch (UnsupportedCharsetException e) {
            this.secretKey = new SecretKeySpec(secret.getBytes(), this.algorithm.getJcaName());
        }

        this.parser = Jwts.parserBuilder().requireIssuer(this.issuer).setSigningKey(this.secretKey).build();
    }

    @Override
    public String encode(String user, Long duration, Map<String, Object> claims) {
        JwtBuilder builder = Jwts.builder();

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        if (duration >= 0) {
            long expMillis = nowMillis + duration;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        builder
                .setId(UUID.randomUUID().toString())
                .setIssuer(this.issuer)
                .setAudience(user)
                .setIssuedAt(now).signWith(this.secretKey, this.algorithm);

        if (claims != null && !claims.isEmpty()) {
            builder.addClaims(claims);
        }

        return builder.compact();
    }

    @Override
    public Claims decode(String jwt) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        Jws<Claims> claimsJws = this.parser.parseClaimsJws(jwt);
        return claimsJws.getBody();
    }

    @Override
    public boolean isSigned(String jwt) {
        return this.parser.isSigned(jwt);
    }
}
