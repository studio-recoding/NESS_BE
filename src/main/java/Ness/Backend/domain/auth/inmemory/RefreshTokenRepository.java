package Ness.Backend.domain.auth.inmemory;

import Ness.Backend.domain.auth.inmemory.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
}