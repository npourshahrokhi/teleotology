package net.gotev.uploadservice;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public final class TaskParameters implements Parcelable {
    public static final Parcelable.Creator<TaskParameters> CREATOR = new Parcelable.Creator<TaskParameters>() {
        public TaskParameters createFromParcel(Parcel in) {
            return new TaskParameters(in);
        }

        public TaskParameters[] newArray(int size) {
            return new TaskParameters[size];
        }
    };
    private boolean autoDeleteSuccessfullyUploadedFiles;
    private String customUserAgent;
    private ArrayList<UploadFile> files;
    private String id;
    private int maxRetries;
    private String method;
    private UploadNotificationConfig notificationConfig;
    private ArrayList<NameValue> requestHeaders;
    private ArrayList<NameValue> requestParameters;
    private String url;
    private boolean usesFixedLengthStreamingMode;

    public TaskParameters() {
        this.method = "POST";
        this.maxRetries = 0;
        this.usesFixedLengthStreamingMode = true;
        this.autoDeleteSuccessfullyUploadedFiles = false;
        this.requestHeaders = new ArrayList<>();
        this.requestParameters = new ArrayList<>();
        this.files = new ArrayList<>();
    }

    public void writeToParcel(Parcel parcel, int arg1) {
        int i = 1;
        parcel.writeString(this.id);
        parcel.writeString(this.url);
        parcel.writeString(this.method);
        parcel.writeString(this.customUserAgent);
        parcel.writeInt(this.maxRetries);
        parcel.writeByte((byte) (this.autoDeleteSuccessfullyUploadedFiles ? 1 : 0));
        if (!this.usesFixedLengthStreamingMode) {
            i = 0;
        }
        parcel.writeByte((byte) i);
        parcel.writeParcelable(this.notificationConfig, 0);
        parcel.writeList(this.requestHeaders);
        parcel.writeList(this.requestParameters);
        parcel.writeList(this.files);
    }

    private TaskParameters(Parcel in) {
        boolean z = true;
        this.method = "POST";
        this.maxRetries = 0;
        this.usesFixedLengthStreamingMode = true;
        this.autoDeleteSuccessfullyUploadedFiles = false;
        this.requestHeaders = new ArrayList<>();
        this.requestParameters = new ArrayList<>();
        this.files = new ArrayList<>();
        this.id = in.readString();
        this.url = in.readString();
        this.method = in.readString();
        this.customUserAgent = in.readString();
        this.maxRetries = in.readInt();
        this.autoDeleteSuccessfullyUploadedFiles = in.readByte() == 1;
        this.usesFixedLengthStreamingMode = in.readByte() != 1 ? false : z;
        this.notificationConfig = (UploadNotificationConfig) in.readParcelable(UploadNotificationConfig.class.getClassLoader());
        in.readList(this.requestHeaders, NameValue.class.getClassLoader());
        in.readList(this.requestParameters, NameValue.class.getClassLoader());
        in.readList(this.files, UploadFile.class.getClassLoader());
    }

    public int describeContents() {
        return 0;
    }

    public void addFile(UploadFile file) throws FileNotFoundException {
        this.files.add(file);
    }

    public List<UploadFile> getFiles() {
        return this.files;
    }

    public void addRequestHeader(String name, String value) {
        this.requestHeaders.add(new NameValue(name, value));
    }

    public void addRequestParameter(String name, String value) {
        this.requestParameters.add(new NameValue(name, value));
    }

    public List<NameValue> getRequestHeaders() {
        return this.requestHeaders;
    }

    public List<NameValue> getRequestParameters() {
        return this.requestParameters;
    }

    public UploadNotificationConfig getNotificationConfig() {
        return this.notificationConfig;
    }

    public TaskParameters setNotificationConfig(UploadNotificationConfig notificationConfig2) {
        this.notificationConfig = notificationConfig2;
        return this;
    }

    public String getId() {
        return this.id;
    }

    public TaskParameters setId(String id2) {
        this.id = id2;
        return this;
    }

    public String getUrl() {
        return this.url;
    }

    public TaskParameters setUrl(String url2) {
        this.url = url2;
        return this;
    }

    public String getMethod() {
        return this.method;
    }

    public TaskParameters setMethod(String method2) {
        if (method2 != null && method2.length() > 0) {
            this.method = method2;
        }
        return this;
    }

    public String getCustomUserAgent() {
        return this.customUserAgent;
    }

    public boolean isCustomUserAgentDefined() {
        return this.customUserAgent != null && !"".equals(this.customUserAgent);
    }

    public TaskParameters setCustomUserAgent(String customUserAgent2) {
        this.customUserAgent = customUserAgent2;
        return this;
    }

    public int getMaxRetries() {
        return this.maxRetries;
    }

    public TaskParameters setMaxRetries(int maxRetries2) {
        if (maxRetries2 < 0) {
            this.maxRetries = 0;
        } else {
            this.maxRetries = maxRetries2;
        }
        return this;
    }

    public boolean isAutoDeleteSuccessfullyUploadedFiles() {
        return this.autoDeleteSuccessfullyUploadedFiles;
    }

    public TaskParameters setAutoDeleteSuccessfullyUploadedFiles(boolean autoDeleteSuccessfullyUploadedFiles2) {
        this.autoDeleteSuccessfullyUploadedFiles = autoDeleteSuccessfullyUploadedFiles2;
        return this;
    }

    public boolean isUsesFixedLengthStreamingMode() {
        return this.usesFixedLengthStreamingMode;
    }

    public TaskParameters setUsesFixedLengthStreamingMode(boolean usesFixedLengthStreamingMode2) {
        this.usesFixedLengthStreamingMode = usesFixedLengthStreamingMode2;
        return this;
    }
}
