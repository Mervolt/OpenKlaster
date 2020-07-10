package com.openklaster.common.authentication;

public interface ITokenGenerator {
    String generateToken(int count);
    String generateToken(int uppperCount, int lowerCount, int digitscount, int specialCount);
}
