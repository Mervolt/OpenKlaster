package com.openklaster.common.authentication.tokens;

public interface TokenGenerator {
    String generateToken(int count);
    String generateToken(int uppperCount, int lowerCount, int digitscount, int specialCount);
}
