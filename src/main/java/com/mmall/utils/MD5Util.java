package com.mmall.utils;

import org.springframework.stereotype.Component;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class MD5Util {

    public static String md5(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes());
            byte[] digest = md.digest();
            return toHexString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String md5(InputStream in) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buf = new byte[1024 * 2];
            int i = 0;
            while ((i = in.read(buf)) != -1) {
                if (i == 0) {
                    continue;
                }
                md.update(buf, 0, i);
            }
            byte[] digest = md.digest();
            return toHexString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String md5(File file) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            return md5(in);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // Do-Nothing
                }
            }
        }
    }

    public static String sha(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(text.getBytes());
            byte[] digest = md.digest();
            return toHexString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toHexString(byte[] bytes) {
        StringBuilder buf = new StringBuilder(50);
        int t;
        for (int i = 0; i < bytes.length; i++) {
            t = bytes[i];
            if (t < 0) {
                t += 256;
            }
            if (t < 16) {
                buf.append("0");
            }
            buf.append(Integer.toHexString(t));
        }
        return buf.toString();
    }

}
