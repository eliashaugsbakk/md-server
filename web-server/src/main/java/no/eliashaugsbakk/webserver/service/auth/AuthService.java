package no.eliashaugsbakk.webserver.service.auth;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final String VALID_TOKEN = "secret-token";

  public boolean isValid(String token) {
    if (token == null || token.isEmpty()) {
      return false;
    }
    String cleanToken = token.replace("Bearer ", "");
    return VALID_TOKEN.equals(cleanToken);
  }
}
