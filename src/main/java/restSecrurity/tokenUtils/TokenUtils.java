package restSecrurity.tokenUtils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.javalin.http.HttpStatus;
import restSecrurity.DTO.UserDTO;
import restSecrurity.exceptions.ApiException;
import restSecrurity.exceptions.NotAuthorizedException;

import java.io.NotActiveException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

public class TokenUtils {
    /*
    This class writes a token for a user with the attributes "username"
    and roles as its own class in a set.

     */

    private final static String KEY = "secretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecret";
    public static String createToken(UserDTO user) throws ApiException {
        String ISSUER;
        String TOKEN_EXPIRE_TIME;
        String SECRET_KEY;

        if(System.getenv("DEPLOYED") != null){
            ISSUER = System.getenv("ISSUER");
            TOKEN_EXPIRE_TIME = System.getenv("TOKEN_EXPIRE_TIME");
            SECRET_KEY = System.getenv("SECRET_KEY");
        }else {
            ISSUER = "Patrick";
            TOKEN_EXPIRE_TIME = "1800000";
            SECRET_KEY = KEY;
        }

        try{
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getUsername())
                    .issuer(ISSUER)
                    .claim("username",user.getUsername())
                    .claim("roles",user.getRoles().stream().reduce((s1,s2) -> s1 +","+s2).get())
                    .claim("email",user.getEmail())
                    .expirationTime(new Date(new Date().getTime()+Integer.parseInt(TOKEN_EXPIRE_TIME)))
                    .build();
            Payload payload = new Payload(claimsSet.toJSONObject());

            JWSSigner signer = new MACSigner(SECRET_KEY);
            JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
            JWSObject jwsObject = new JWSObject(jwsHeader,payload);
            jwsObject.sign(signer);
            return jwsObject.serialize();
        }catch (JOSEException e){
            e.printStackTrace();
            throw new ApiException(500,"Could not create token");
        }
    }

    public static UserDTO getUserFromValidToken(String token) throws ApiException {
        // inline if statement
        boolean IS_DEPLOYED = (System.getenv("DEPLOYED") != null);
        String SECRET = IS_DEPLOYED ? System.getenv("SECRET_KEY"): KEY;
        // TODO create below methods.
        return verifyToken(token,SECRET);
    }
    private static UserDTO verifyToken(String token,String SECRET) throws ApiException {
        try {
            if(tokenIsValid(token,SECRET) && tokenNotExpired(token)){
                return getUserWithRolesFromToken(token);
            }else {
                throw new NotAuthorizedException(403,"Token is not valid");
            }
        }catch (ParseException | JOSEException | NotAuthorizedException e){
            e.printStackTrace();
            throw new ApiException(HttpStatus.UNAUTHORIZED.getCode(), "Unauthorized! could not validate token");
        }
    }
    private static boolean tokenIsValid(String token,String secret) throws ParseException, JOSEException, NotAuthorizedException {
        SignedJWT jwt = SignedJWT.parse(token);
        if(jwt.verify(new MACVerifier(secret))){
            return true;
        }else {
            throw new NotAuthorizedException(403,"Token is not valid");
        }
    }
    private static boolean tokenNotExpired(String token) throws NotAuthorizedException, ParseException {
        if(timeToExpire(token) > 0){
            return true;
        }else {
            throw new NotAuthorizedException(403,"Token has expired");
        }
    }
    private static int timeToExpire(String token) throws ParseException {
        SignedJWT jwt = SignedJWT.parse(token);
        return (int) (jwt.getJWTClaimsSet().getExpirationTime().getTime()- new Date().getTime());
    }

    private static UserDTO getUserWithRolesFromToken(String token) throws ParseException {
        SignedJWT jwt = SignedJWT.parse(token);
        String roles = jwt.getJWTClaimsSet().getClaim("roles").toString();
        String username = jwt.getJWTClaimsSet().getClaim("username").toString();

        Set<String> roleSet = Arrays.stream(roles.split(","))
                .collect(Collectors.toSet());
        return new UserDTO(username,roleSet);
    }

}
