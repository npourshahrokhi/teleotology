package net.gotev.uploadservice;

import android.os.Parcel;
import android.os.Parcelable;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class UploadFile implements Parcelable {
    public static final Parcelable.Creator<UploadFile> CREATOR = new Parcelable.Creator<UploadFile>() {
        public UploadFile createFromParcel(Parcel in) {
            return new UploadFile(in);
        }

        public UploadFile[] newArray(int size) {
            return new UploadFile[size];
        }
    };
    private static final String LOG_TAG = UploadFile.class.getSimpleName();
    private static final String NEW_LINE = "\r\n";
    private static final String UNUSED = "UNUSED";
    private final Charset US_ASCII;
    private final Charset UTF8;
    protected String contentType;
    protected final File file;
    protected final String fileName;
    protected final String paramName;

    UploadFile(String path, String parameterName, String fileName2, String contentType2) throws FileNotFoundException, IllegalArgumentException {
        this.US_ASCII = Charset.forName("US-ASCII");
        this.UTF8 = Charset.forName("UTF-8");
        if (path == null || "".equals(path)) {
            throw new IllegalArgumentException("Please specify a file path! Passed path value is: " + path);
        }
        File file2 = new File(path);
        if (!file2.exists()) {
            throw new FileNotFoundException("Could not find file at path: " + path);
        }
        this.file = file2;
        if (parameterName == null || "".equals(parameterName)) {
            throw new IllegalArgumentException("Please specify parameterName value for file: " + path);
        }
        this.paramName = parameterName;
        if (contentType2 == null || contentType2.isEmpty()) {
            this.contentType = autoDetectMimeType();
            Logger.debug(LOG_TAG, "Detected MIME type for " + file2.getAbsolutePath() + " is: " + contentType2);
        } else {
            this.contentType = contentType2;
            Logger.debug(LOG_TAG, "Content Type set for " + file2.getAbsolutePath() + " is: " + contentType2);
        }
        if (fileName2 == null || "".equals(fileName2)) {
            this.fileName = this.file.getName();
            Logger.debug(LOG_TAG, "Using original file name: " + fileName2);
            return;
        }
        this.fileName = fileName2;
        Logger.debug(LOG_TAG, "Using custom file name: " + fileName2);
    }

    UploadFile(String path) throws FileNotFoundException {
        this(path, UNUSED, UNUSED, UNUSED);
    }

    public long length() {
        return this.file.length();
    }

    public final InputStream getStream() throws FileNotFoundException {
        return new FileInputStream(this.file);
    }

    public byte[] getMultipartHeader(boolean isUtf8) throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        builder.append("Content-Disposition: form-data; name=\"").append(this.paramName).append("\"; filename=\"").append(this.fileName).append("\"").append(NEW_LINE);
        builder.append("Content-Type: ").append(this.contentType).append(NEW_LINE).append(NEW_LINE);
        return builder.toString().getBytes(isUtf8 ? this.UTF8 : this.US_ASCII);
    }

    public long getTotalMultipartBytes(long boundaryBytesLength, boolean isUtf8) throws UnsupportedEncodingException {
        return ((long) getMultipartHeader(isUtf8).length) + boundaryBytesLength + this.file.length();
    }

    private String autoDetectMimeType() {
        String mimeType;
        String extension = null;
        String absolutePath = this.file.getAbsolutePath();
        int index = absolutePath.lastIndexOf(".") + 1;
        if (index >= 0 && index <= absolutePath.length()) {
            extension = absolutePath.substring(index);
        }
        if (extension == null || extension.isEmpty() || (mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase())) == null) {
            return ContentType.APPLICATION_OCTET_STREAM;
        }
        return mimeType;
    }

    public void writeToParcel(Parcel parcel, int arg1) {
        parcel.writeString(this.file.getAbsolutePath());
        parcel.writeString(this.paramName);
        parcel.writeString(this.fileName);
        parcel.writeString(this.contentType);
    }

    public int describeContents() {
        return 0;
    }

    protected UploadFile(Parcel in) {
        this.US_ASCII = Charset.forName("US-ASCII");
        this.UTF8 = Charset.forName("UTF-8");
        this.file = new File(in.readString());
        this.paramName = in.readString();
        this.fileName = in.readString();
        this.contentType = in.readString();
    }
}
