package com.ggt.slidescast.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Datamodel representing data information from local file.
 *
 * @author guiguito
 */
@Table(name = "LocalFiles")
public class LocalFile extends Model implements Parcelable {

    @Column
    private String filePath;
    @Column
    private String fileName;

    public LocalFile() {
        super();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(filePath);
        out.writeString(fileName);
    }

    private LocalFile(Parcel in) {
        filePath = in.readString();
        fileName = in.readString();
    }

    public static final Parcelable.Creator<LocalFile> CREATOR = new Parcelable.Creator<LocalFile>() {
        public LocalFile createFromParcel(Parcel in) {
            return new LocalFile(in);
        }

        public LocalFile[] newArray(int size) {
            return new LocalFile[size];
        }
    };

    public static final void addAll(List<LocalFile> localFiles) {
        ActiveAndroid.beginTransaction();
        try {
            for (LocalFile localFile : localFiles) {
                localFile.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    public static final List<LocalFile> getAll() {
        return new Select().from(LocalFile.class).execute();
    }

    public static final void deleteAll() {
        new Delete().from(LocalFile.class).execute();
    }

}
