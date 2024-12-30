package com.signature.ws;


import com.signature.util.FileValidator;
import com.signature.util.TextUtils;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.*;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.text.ParseException;
import java.util.Locale;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class RequestInterceptor implements WriterInterceptor, ReaderInterceptor {

    @Inject
    private FileValidator fileValidator;

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
        log.info("Notice from ReaderInterceptor");
        InputStream is = context.getInputStream();
        String contentType = context.getHeaders().getFirst("Content-Type");
        if (contentType != null && contentType.startsWith("multipart/form-data;")) {
            int size = Integer.parseInt(context.getHeaders().getFirst("Content-Length"));
            InputStream[] inputStreams = duplicateStream(is);
            try (InputStream original = inputStreams[0]; InputStream duplicate = inputStreams[1]) {
                // TODO: continue validating file
                // fileValidator.validateFile(duplicate, size);
                context.setInputStream(original);
                return context.proceed();
            } catch (IOException e) {
                throw new IllegalStateException();
            }
        } else {
            validateJsonRequest(context, is);
        }
        return context.proceed();
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext context)
            throws IOException, WebApplicationException {
        log.info("Notice from WriterInterceptor");
        context.proceed();
    }

    private void validateJsonRequest(ReaderInterceptorContext context, InputStream is) throws IOException {
        final char[] buffer = new char[1024];
        final StringBuilder out = new StringBuilder();
        try (Reader in = new InputStreamReader(is, "UTF-8")) {
            for (; ; ) {
                int rsz = in.read(buffer, 0, buffer.length);
                if (rsz < 0)
                    break;
                out.append(buffer, 0, rsz);
            }
        }
        String json = out.toString();
        if (json.contains("<script>") || json.contains("<") || json.contains(">") || containsSqlInjection(json)) {
            Response response = Response.status(Response.Status.BAD_REQUEST).build();
            throw new WebApplicationException(response);
        }
        String formattedJson = TextUtils.sanitizeString(json);
        try {

            formattedJson = TextUtils.sanitizeNumber(formattedJson, Locale.US);
        } catch (ParseException e) {
            throw new IOException(e);
        }
        context.setInputStream(new ByteArrayInputStream(formattedJson.getBytes("UTF-8")));
    }

    private boolean containsSqlInjection(String str) {
        if (str == null || str.trim().length() == 0) return false;
        String[] words = str.toLowerCase().split("\\W+");
        if (words == null || words.length == 0) return false;
        return findInArray(words, "select") ||
                findInArray(words, "drop") ||
                findInArray(words, "alter") ||
                findInArray(words, "truncate") ||
                findInArray(words, "execute") ||
                findInArray(words, "exec") ||
                findInArray(words, "benchmark") ||
                findInArray(words, "sleep") ||
                findInArray(words, "update") ||
                findInArray(words, "create") ||
                findInArray(words, "delete") ||
                findInArray(words, "or") ||
                findInArray(words, "and") ||
                findInArray(words, "from") ||
                findInArray(words, "where") ||
                findInArray(words, "union");

    }

    public InputStream[] duplicateStream(InputStream inputStream) throws IOException {
        // Buffer to store the original stream data
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        // Copy the original stream into the buffer
        byte[] tempBuffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(tempBuffer)) != -1) {
            buffer.write(tempBuffer, 0, bytesRead);
        }

        // Create two independent InputStreams from the buffered data
        byte[] data = buffer.toByteArray();
        InputStream originalCopy = new ByteArrayInputStream(data);
        InputStream duplicateCopy = new ByteArrayInputStream(data);

        return new InputStream[]{originalCopy, duplicateCopy};
    }

    private boolean findInArray(String[] words, String s) {
        for (String word : words) {
            if (word.equalsIgnoreCase(s)) return true;
        }
        return false;
    }
}
