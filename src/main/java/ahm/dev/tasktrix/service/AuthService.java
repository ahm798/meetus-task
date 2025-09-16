package ahm.dev.tasktrix.service;

import ahm.dev.tasktrix.dto.AuthRequest;
import ahm.dev.tasktrix.dto.AuthResponse;

public interface AuthService {
    AuthResponse authenticate(AuthRequest authRequest);
    AuthResponse refresh(String refreshToken);
}
