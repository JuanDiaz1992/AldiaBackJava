package com.springboot.aldiabackjava.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class GetCodeNow {
    public  static String getCode(){
        Instant instant = Instant.now();
        ZoneId zonaHoraria = ZoneId.systemDefault();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return formato.format(instant.atZone(zonaHoraria));
    }
}
