package za.co.invoke.solutions.naturalgasprices.helper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import za.co.invoke.solutions.naturalgasprices.enums.ExceptionConstantsEnum;
import za.co.invoke.solutions.naturalgasprices.constants.JwtClaimConstants;
import za.co.invoke.solutions.naturalgasprices.dto.JwtTokenClaims;
import za.co.invoke.solutions.naturalgasprices.dto.VerifyTokenResponse;
import za.co.invoke.solutions.naturalgasprices.entities.JwtToken;
import za.co.invoke.solutions.naturalgasprices.entities.User;

import java.io.Serializable;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Component
public class JwtTokenHelper implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;

    private final String privateKey;

    private final Long expiration;

    public JwtTokenHelper(@Value("${secret.private.key}") String privateKey,
                          @Value("${token.expiration.time.minutes}") Long expiration) {

        this.privateKey = privateKey;
        this.expiration = expiration;
    }

    public static String getUUIDString() {
        return UUID.randomUUID().toString();
    }

    /**
     * This method generates the token by putting the passed in claims and signs it digitally with the private key
     *
     * @param claimsMap  - JWT token claims map
     * @param privateKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public String digitallySignJWT(final String keyId, final Map<String, Object> claimsMap, final String privateKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        return Jwts.builder()
                .setHeaderParam(JwtClaimConstants.KID, keyId)
                .setHeaderParam(JwtClaimConstants.TOKEN_TYPE, JwtClaimConstants.TOKEN_TYPE_VALUE)
                .setClaims(claimsMap)
                .setIssuedAt((Date) claimsMap.get(JwtClaimConstants.CLAIM_ISSUED_AT))
                .setExpiration((Date) claimsMap.get(JwtClaimConstants.CLAIM_EXP))
                .signWith(SignatureAlgorithm.RS512, getPrivateKey(privateKey))
                .compact();
    }

    /**
     * This method generates the X509 specification public key in order to verify the digital signature the token
     *
     * @param encodedPublicKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private PublicKey getPublicKey(final String encodedPublicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] publicKeyByteArray = Base64.getDecoder().decode(encodedPublicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyByteArray);
        final KeyFactory factory = KeyFactory.getInstance(JwtClaimConstants.ALGORITHM);
        return factory.generatePublic(keySpec);
    }

    /**
     * This method returns the GenerateTokenResponse Claims Map
     *
     * @param jwtTokenClaims
     * @return
     */
    public Map<String, Object> getJWTTokenClaimsMap(final JwtTokenClaims jwtTokenClaims, Date issueDateTime, Date expDateTime) {
        final Map<String, Object> claimsMap = new LinkedHashMap<>();


        claimsMap.put(JwtClaimConstants.CLAIM_ISSUED_AT, issueDateTime);
        claimsMap.put(JwtClaimConstants.CLAIM_EXP, expDateTime);
        claimsMap.put(JwtClaimConstants.CLAIM_UNIQUE_ID, jwtTokenClaims.getUniqueId());
        claimsMap.put(JwtClaimConstants.CLAIM_USER_ROLE, jwtTokenClaims.getRoles());

        return claimsMap;
    }

    /**
     * This method generates the PKCS#8 specification private key in order to digitally sign the token
     *
     * @param privateKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private PrivateKey getPrivateKey(final String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] privateKeyByteArray = Base64.getDecoder().decode(privateKey);
        final PKCS8EncodedKeySpec pKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyByteArray);
        final KeyFactory keyFactory = KeyFactory.getInstance(JwtClaimConstants.ALGORITHM);
        return keyFactory.generatePrivate(pKCS8EncodedKeySpec);
    }

    public JwtToken createAuthToken(final String uniqueID, User user, final List<String> roles) throws InvalidKeySpecException, NoSuchAlgorithmException {

        final String keyId = JwtTokenHelper.getUUIDString();
        final JwtTokenClaims jwtTokenClaims = getJWTTokenClaims(uniqueID, roles);
        Date issueDateTime = new Date();
        Date expDateTime = new Date(issueDateTime.getTime() + expiration);

        final Map<String, Object> jwtTokenClaimsMap = getJWTTokenClaimsMap(jwtTokenClaims, issueDateTime, expDateTime);
        final String jwtToken = digitallySignJWT(keyId, jwtTokenClaimsMap, privateKey);

        return new JwtToken(
                issueDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                expDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                jwtToken,
                user);
    }

    /**
     * This method verifies the integrity of the token by digitally verifying the signature of the token with the public key
     *
     * @param token
     * @param publicKey
     * @param uniqueId
     * @return
     */
    public VerifyTokenResponse verifyTokenSignatureWithPublicKey(final String token, final String publicKey, final String uniqueId) {
        final VerifyTokenResponse verifyTokenResponse = new VerifyTokenResponse();
        verifyTokenResponse.setUniqueId(uniqueId);
        verifyTokenResponse.setToken(token);

        try {
            Jwts.parser().setSigningKey(getPublicKey(publicKey));
            final Claims claims = Jwts.parser().setSigningKey(getPublicKey(publicKey))
                    .parseClaimsJws(token).getBody();
            final Date expirationDate = claims.getExpiration();

            if (expirationDate.compareTo(new Date()) < 0) {
                log.info(ExceptionConstantsEnum.TOKEN_EXPIRED.getMessage() + " Token - " + token);
                verifyTokenResponse.setValidToken(false);
                verifyTokenResponse.setNotValidReason(ExceptionConstantsEnum.TOKEN_EXPIRED.getMessage());
            }

            verifyTokenResponse.setValidToken(true);

            return verifyTokenResponse;
        } catch (JwtException | IllegalArgumentException e) {
            verifyTokenResponse.setValidToken(false);
            verifyTokenResponse.setNotValidReason(e.getMessage());
            log.error(e.getMessage());
            return verifyTokenResponse;
        } catch (NoSuchAlgorithmException e) {
            verifyTokenResponse.setValidToken(false);
            verifyTokenResponse.setNotValidReason(e.getMessage());
            log.error("Exception while verifying the token signature using the the algorithm specified");
            return verifyTokenResponse;
        } catch (InvalidKeySpecException e) {
            verifyTokenResponse.setValidToken(false);
            verifyTokenResponse.setNotValidReason(e.getMessage());
            log.error("Exception getting the public key specification to verify token signature");
            return verifyTokenResponse;
        }
    }

    /**
     * This method is used to get the GenerateTokenResponse Claims that are needed to create the JWT token
     *
     * @param uniqueID
     * @param roles
     * @return
     */
    private JwtTokenClaims getJWTTokenClaims(final String uniqueID,
                                             final List<String> roles) {

        return new JwtTokenClaims(LocalDateTime.now(), LocalDateTime.now().plusMinutes(expiration), uniqueID, roles);

    }
}
