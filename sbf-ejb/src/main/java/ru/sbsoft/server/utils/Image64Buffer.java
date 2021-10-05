package ru.sbsoft.server.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.codec.binary.Base64;
import ru.sbsoft.shared.model.ImageBase64;

/**
 *
 * @author Kiselev
 */
public class Image64Buffer {

    private static final Set<String> IMAGE_MIME = new HashSet<>(Arrays.asList("image/png", "image/jpeg", "image/jpg", "image/gif"));

    private static final Image64Buffer INSTANCE = new Image64Buffer();

    public static Image64Buffer getInst() {
        return INSTANCE;
    }

    private final Map<URL, SoftReference<ImageBase64>> cache = new ConcurrentHashMap<>();

    private Image64Buffer() {
    }

    public ImageBase64 getImage64(URL imgFile) throws IOException {
        if (imgFile == null) {
            throw new IllegalArgumentException("Image url can't be null");
        }
        ImageBase64 res = get(imgFile);
        if (res == null) {
            return putAndGet(imgFile);
        }
        return res;
    }

    private ImageBase64 get(URL imgFile) {
        SoftReference<ImageBase64> ref = cache.get(imgFile);
        if (ref != null && ref.get() != null) {
            return ref.get();
        } else {
            return null;
        }
    }

    private ImageBase64 putAndGet(URL imgFile) throws IOException {
        synchronized (imgFile) {
            ImageBase64 res = get(imgFile);
            if (res == null) {
                res = toBase64(imgFile);
                cache.put(imgFile, new SoftReference(res));
            }
            return res;
        }
    }

    private static ImageBase64 toBase64(URL imgFile) throws IOException {
        if (imgFile == null) {
            throw new IllegalArgumentException("Image file url can't be null");
        }

        try (InputStream in = imgFile.openStream()) {
            String mime = URLConnection.guessContentTypeFromStream(in);
            if (mime == null) {
                mime = URLConnection.guessContentTypeFromName(imgFile.getFile());
            }

            if (mime == null || !IMAGE_MIME.contains(mime)) {
                StringBuilder msg = new StringBuilder();

                if (mime == null) {
                    msg.append("MIME type is not found. ");
                } else {
                    msg.append("MIME type is '").append(mime).append("'. ");
                }
                msg.append("MIME type must be in ").append(IMAGE_MIME).append(" for: ").append(imgFile);
                throw new IllegalArgumentException(msg.toString());
            }
            return new ImageBase64(mime, getBase64Body(in));
        }
    }

    private static String getBase64Body(InputStream in) throws IOException {
        byte[] buf = new byte[1024 << 3];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int read;
        while ((read = in.read(buf)) >= 0) {
            out.write(buf, 0, read);
        }
        return Base64.encodeBase64String(out.toByteArray());
    }
}
