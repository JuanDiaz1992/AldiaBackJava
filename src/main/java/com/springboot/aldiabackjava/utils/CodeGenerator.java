package com.springboot.aldiabackjava.utils;

import java.util.concurrent.ThreadLocalRandom;

public class CodeGenerator {
    public static String generateNumericCode(int length) {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int digit = ThreadLocalRandom.current().nextInt(0, 10);
            code.append(digit);
        }
        return code.toString();
    }
}
