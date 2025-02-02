package vn.arius.finalProject.service.impl;

import org.springframework.stereotype.Service;
import vn.arius.finalProject.entity.TokenRevoked;
import vn.arius.finalProject.repository.TokenRevokedRepository;
import vn.arius.finalProject.service.TokenRevokedService;

@Service
public class TokenRevokedServiceImpl implements TokenRevokedService {
    private final TokenRevokedRepository tokenRevokedRepository;

    public TokenRevokedServiceImpl(TokenRevokedRepository tokenRevokedRepository) {
        this.tokenRevokedRepository = tokenRevokedRepository;
    }

    @Override
    public void saveTokenLogout(String token) {
        TokenRevoked tokenRevoked = new TokenRevoked();
        tokenRevoked.setToken(token);
        this.tokenRevokedRepository.save(tokenRevoked);
    }

    @Override
    public boolean existsToken(String token) {
        return this.tokenRevokedRepository.existsByToken(token);
    }
}
