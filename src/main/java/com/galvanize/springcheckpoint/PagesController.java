package com.galvanize.springcheckpoint;

import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
public class PagesController {

    @GetMapping("/camelize")
    public String camelizeText(@RequestParam String original, @RequestParam(required = false) boolean initialCap) {
        String result = "";

        String[] words = original.split("_");

        if (initialCap == true) {
            for (int i = 0; i < words.length; i++) {
                result += words[i].substring(0,1).toUpperCase() + words[i].substring(1);
            }
        } else {
            result += words[0];
            for (int i = 1; i < words.length; i++) {
                result += words[i].substring(0,1).toUpperCase() + words[i].substring(1);
            }
        }

        return result;
    }

    @GetMapping("/redact")
    public String redact(@RequestParam String original, @RequestParam MultiValueMap<String, String> params) {
        String result = "";

        String[] originalWords = original.split(" ");

        for (int i = 0; i < originalWords.length; i++) {
            if (params.get("badWord").contains(originalWords[i])) {
                String mask = "";
                for (int l = 0; l <originalWords[i].length(); l++) {
                    mask += "*";
                }
                result += mask + " ";
            } else {
                result += originalWords[i] + " ";
            }
        }

        return result.trim();
    }

    @PostMapping("/encode")
    public String encode(@RequestParam String message, @RequestParam String key) {
        String result = "";

        String alphabet = "abcdefghijklmnopqrstuvwxyz";

        String[] letters = message.split("");

        for (int i = 0; i < letters.length; i++) {
            if (letters[i].equals(" ")) {
                result += letters[i];
            } else {
                int index = alphabet.indexOf(letters[i]);
                result += key.charAt(index);
            }
        }

        return result;
    }

    @PostMapping("/s/{find}/{replacement}")
    public String findReplace(@PathVariable String find, @PathVariable String replacement, @RequestBody String body) {

        while (body.contains(find)) {
            int replaceStart = body.indexOf(find);
            int replaceEnd = replaceStart + find.length();
            body = body.substring(0, replaceStart) + replacement + body.substring(replaceEnd);
        }

        return body;
    }
}
