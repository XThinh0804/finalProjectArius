package vn.arius.finalProject.service;

public interface TokenRevokedService {
    void saveTokenLogout(String token);

    boolean existsToken(String token);
}
