package com.openklaster.common.authentication.tokens;

import org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class generate random strings based on java.security.SecureRandom.
 * Those strings are chosen from 4 groups - upperCase chars, lowerCase chars, digits and
 * special characters (ascii values from 33 to 47).
 */
public class BasicTokenGenerator implements TokenGenerator {

    private final SecureRandom secureRandom;

    public BasicTokenGenerator() {
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

    private String accumulateChars(int uppperCount, int lowerCount, int digitscount, int specialCount) {
        return upperCaseLetters(uppperCount)
                .concat(lowerCaseLetters(lowerCount))
                .concat(digits(digitscount))
                .concat(specialChars(specialCount));
    }

    private List<Character> getCharactersList(int uppperCount, int lowerCount, int digitscount, int specialCount) {
        return accumulateChars(uppperCount, lowerCount, digitscount, specialCount)
                .chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
    }

    /**
     * @param count how many chars taken from each of 4 groups.
     * @return random token with length of 4*count with each group having exactly {count} chars.
     */
    @Override
    public String generateToken(int count) {
        return generateToken(count, count, count, count);
    }

    /**
     *
     * @param uppperCount count of upper case chars in string
     * @param lowerCount count of lower  case chars in string
     * @param digitscount count of digits in string
     * @param specialCount count of special characters in string
     * @return random token with length of sum of params.
     */
    @Override
    public String generateToken(int uppperCount, int lowerCount, int digitscount, int specialCount) {
        List<Character> characterList = getCharactersList(uppperCount, lowerCount, digitscount, specialCount);
        Collections.shuffle(characterList);
        return characterList.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
}
