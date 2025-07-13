package org.example.rgybackend.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class AuthCodeManager {
    private static Map<String, Long> authCodeMap = new HashMap<>();

    private static Map<String, Long> timestampMap = new HashMap<>();

    public static Long addAuthCode(String id) {
        Long lastTimestamp = timestampMap.get(id);
        if(lastTimestamp != null) {
            Long diffSeconds = (TimeUtil.now() - lastTimestamp) / TimeUtil.SECOND;
            if(diffSeconds < 60) {
                return null;
            }
        }

        Random random = new Random();
        final long authCode = 100000 + random.nextInt(900000);
        authCodeMap.put(id, authCode);
        timestampMap.put(id, TimeUtil.now());

        return authCode;
    }

    public static boolean checkAuthCode(String id, Long authCode) {
        System.out.println(authCode);

        Long timestamp = timestampMap.get(id);
        if(timestamp == null || (TimeUtil.now() - timestamp) / TimeUtil.SECOND > 300) {
            return false;
        }
    
        Long correctCode = authCodeMap.get(id);
        System.out.println(correctCode);
        return correctCode != null && authCode.equals(correctCode);
    }
}
