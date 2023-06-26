package za.co.invoke.solutions.naturalgasprices.util;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import za.co.invoke.solutions.naturalgasprices.dto.DecodedToken;
import za.co.invoke.solutions.naturalgasprices.exceptions.GasPriceException;

import java.nio.charset.StandardCharsets;

@Slf4j
public class TokenDecoder {

    private TokenDecoder() {
    }

    public static DecodedToken getDecodedToken(String rawJwtToken) throws GasPriceException {
        final String[] pieces = rawJwtToken.split("\\.");
        if (pieces.length == 3) {
            final String b64payload = pieces[1];
            String jsonString = new String(Base64.decodeBase64(b64payload), StandardCharsets.UTF_8);

            final DecodedToken decodedToken = new Gson().fromJson(jsonString, DecodedToken.class);
            log.info("decodedToken for user with id: {}", decodedToken.getUid());
            return decodedToken;
        } else {
            throw new GasPriceException("An error has occurred while decoding token - Invalid Token");
        }
    }
}
