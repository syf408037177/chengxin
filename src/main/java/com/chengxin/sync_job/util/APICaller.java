package com.chengxin.sync_job.util;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class APICaller {
    public static String call(String ip, String serviceName, String type, String json)
            throws IOException {
        Writer writer = null;
        Scanner scanner = null;
        try {
            URL url = new URL(ip + "/u8cloud/api/" + serviceName);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("content-type", "application/json");
            if (type != null) {
                connection.setRequestProperty("trantype", type);
            }
            connection.setRequestProperty("isEncrypt", "N");
            connection.setRequestProperty("needStackTrace", "Y");
            connection.setConnectTimeout(60);
            connection.setReadTimeout(60000);
            connection.setDoOutput(true);
            writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            writer.write(json);
            writer.flush();
            scanner = new Scanner(connection.getInputStream(), "UTF-8");

            StringBuffer returnJson = new StringBuffer();
            while (scanner.hasNextLine()) {
                String mess = scanner.nextLine();
                returnJson.append(mess);
            }

            return returnJson.toString();
        } finally {
            if (writer != null) {
                writer.close();
            }
            if (scanner != null)
                scanner.close();
        }
    }
}