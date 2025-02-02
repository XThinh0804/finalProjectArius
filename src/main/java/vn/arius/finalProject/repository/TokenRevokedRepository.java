package vn.arius.finalProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.arius.finalProject.entity.TokenRevoked;

@Repository
public interface TokenRevokedRepository extends JpaRepository<TokenRevoked, Long> {
    boolean existsByToken(String token);
}
