package com.springboot.aldiabackjava.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class UtilsServices {

    @Autowired
    private Tesseract tesseract;


    public ResponseEntity<Map<String, String>> recognizedText(Map<String,String> img) throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(img.get("img"));
        BufferedImage jpgImage = null;
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedBytes)) {
            BufferedImage image = ImageIO.read(inputStream);
            jpgImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            jpgImage.createGraphics().drawImage(image, 0, 0, null);
        }

        Map<String,String> list = new HashMap<>();
        try {
            String text = tesseract.doOCR(jpgImage);
            // Define regex pattern to find numbers with optional decimals and currency symbols
            Pattern pattern = Pattern.compile("\\d*[.,]\\d+");

            Matcher matcher = pattern.matcher(text);
            List<String> numbers = new ArrayList<>();
            while (matcher.find()) {
                String temporalStr = matcher.group().replaceAll(",.*", "");
                temporalStr = temporalStr.replace(".", "");
                numbers.add(temporalStr);
            }
            List <Integer> cleanNumbers = new ArrayList<>();
            for (String number : numbers) {
                if (!number.isEmpty() && number != null && number!=null){
                    Integer temporalNumber = Integer.valueOf(number);
                    cleanNumbers.add(temporalNumber);
                }

            }

            // Find the maximum value
            Integer maxNumber = cleanNumbers.stream().max(Integer::compare).orElse(null);
            String result = null;
            if (maxNumber!=null){
                result =  maxNumber.toString();
            }
            list.put("total",result);
            return ResponseEntity.ok().body(list);
        }catch (TesseractException e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body(list);
    }
}
