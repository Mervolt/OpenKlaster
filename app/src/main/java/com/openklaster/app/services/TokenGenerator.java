package com.openklaster.app.services;

import org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TokenGenerator {

    private final SecureRandom secureRandom;

    public TokenGenerator() {
        this.secureRandom = new SecureRandom();
    }

    private static final int asciiZero = 48;
    private static final int asciiNine = 57;
    private static final int asciiFirstSpecialChar = 33;
    private static final int asciiLastSpecialChar = 47;

    private String upperCaseLetters(int count) {
        return chars(count, 'A', 'Z');
    }

    private String lowerCaseLetters(int count) {
        return chars(count, 'a', 'z');
    }

    private String digits(int count) {
        return RandomStringUtils.random(count, asciiZero, asciiNine, false, true, null, secureRandom);
    }
    private String specialChars(int count) {
        return RandomStringUtils.random(count, asciiFirstSpecialChar, asciiLastSpecialChar, false, false, null, secureRandom);
    }

    private String chars(int count, int start, int stop) {
        return RandomStringUtils.random(count, start, stop, true, true, null, secureRandom);
    }

    private String accumulateChars(int uppperCount, int lowerCount, int digitsCount) {
        return upperCaseLetters(uppperCount)
                .concat(lowerCaseLetters(lowerCount))
                .concat(digits(digitsCount));
    }

    private List<Character> getCharactersList(int uppperCount, int lowerCount, int digitsCount) {
        return accumulateChars(uppperCount, lowerCount, digitsCount)
                .chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
    }


    public String generateToken(int count) {
        return generateToken(count, count, count);
    }


    public String generateToken(int uppperCount, int lowerCount, int digitsCount) {
        List<Character> characterList = getCharactersList(uppperCount, lowerCount, digitsCount);
        Collections.shuffle(characterList);
        return characterList.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
}