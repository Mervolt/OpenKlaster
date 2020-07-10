package com.openklaster.common.authentication;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenGeneratorTest {

    ITokenGenerator generator;

    @Before
    public void setup() {
        this.generator = new TokenGenerator();
    }

    @Test
    public void testTokenLengthWithSameCountPerType() {
        int charsPerType = 4;
        assertEquals(4 * charsPerType, generator.generateToken(charsPerType).length());
        charsPerType = 5;
        assertEquals(4 * charsPerType, generator.generateToken(charsPerType).length());
        charsPerType = 12;
        assertEquals(4 * charsPerType, generator.generateToken(charsPerType).length());
    }

    @Test
    public void testTokenForVariableCountPerType(){
        int expectedUpper = 3;
        int expectedLower = 5;
        int expectedSpecial = 7;
        int expectedDigits = 4;
        String token = generator.generateToken(expectedUpper,expectedLower,expectedDigits,expectedSpecial);
        assertEquals(expectedUpper+expectedLower+expectedSpecial+expectedDigits,token.length());

        int upper =0;
        int lower = 0;
        int special = 0;
        int digits = 0;

        for(char c: token.toCharArray()){
            if(Character.isUpperCase(c)){
                upper++;
            }else if(Character.isLowerCase(c)){
                lower++;
            }else if (Character.isDigit(c)){
                digits++;
            }else {
                special++;
            }
        }
        assertEquals(expectedDigits,digits);
        assertEquals(expectedLower,lower);
        assertEquals(expectedSpecial,special);
        assertEquals(expectedUpper,upper);
    }
}