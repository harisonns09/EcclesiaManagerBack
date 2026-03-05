package org.ecclesiaManager.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Converter
public class CriptografiaConverter implements AttributeConverter<String, String> {

    private static final String ALGORITMO = "AES/ECB/PKCS5Padding";
    // 🔥 IMPORTANTE: Em produção, carregue esta chave de uma variável de ambiente!
    private static final String CHAVE_SECRETA = "MinhaChaveSuperSecretaDe32Bytes!";

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        try {
            SecretKeySpec sks = new SecretKeySpec(CHAVE_SECRETA.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.ENCRYPT_MODE, sks);
            return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar dado: " + e.getMessage());
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            SecretKeySpec sks = new SecretKeySpec(CHAVE_SECRETA.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.DECRYPT_MODE, sks);
            return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao descriptografar dado: " + e.getMessage());
        }
    }
}