package com.github.rinnn31.shoppydex.utils;

public class StringHelper {
    // Hàm loại bỏ dấu tiếng Việt
    public static String replaceDiacritics(String input) {
        String normalized = java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD);
        
        String noDiacritics =  normalized.replaceAll("\\p{M}", "");
        return noDiacritics.replaceAll("đ", "d").replaceAll("Đ", "D");
    }

    public static String normalizeString(String input) {
        String noDiacritics = replaceDiacritics(input);

        return noDiacritics.replaceAll("\\s+", "-")
                            .replaceAll("[^a-zA-Z0-9\\-]", "")
                            .toLowerCase();
    }

    public static String generateRandomHexString(int length) {
        StringBuilder sb = new StringBuilder();
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < length; i++) {
            int r = random.nextInt(16);
            sb.append(Integer.toHexString(r));
        }
        return sb.toString();
    }

    public static String encodeEmail(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex <= 1) {
            return email; 
        }
        StringBuilder sb = new StringBuilder();
        sb.append(email.charAt(0));
        for (int i = 1; i < atIndex - 1; i++) {
            sb.append('*'); // Thay thế các ký tự giữa bằng '*'
        }
        sb.append(email.charAt(atIndex - 1)); // Ký tự cuối cùng trước '@'
        sb.append(email.substring(atIndex)); // Phần còn lại của email
        return sb.toString();
    }
}
