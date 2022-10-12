package com.sparta.board.repopsitory;

import com.sparta.board.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepositroy extends JpaRepository<RefreshToken, String> {
}
