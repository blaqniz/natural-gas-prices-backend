package za.co.invoke.solutions.naturalgasprices.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Collections;

import static za.co.invoke.solutions.naturalgasprices.constants.GasPriceConstants.STS;
import static za.co.invoke.solutions.naturalgasprices.constants.GasPriceConstants.MAX_AGE;
import static za.co.invoke.solutions.naturalgasprices.constants.GasPriceConstants.X_TOKEN;

public class GasPriceHelper {

    /**
     * Private constructor to hide implicit public constructor
     */
    private GasPriceHelper () {}

    public static HttpHeaders getXTokenAndSTSHeaders(String jwtToken) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(STS, "max-age=" + MAX_AGE + "; includeSubDomains");
        headers.set(X_TOKEN, jwtToken);
        return headers;
    }
}
