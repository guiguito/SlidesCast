package com.ggt.slidescast.model.app;

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
 * Data model for a presentation.
 *
 * @author guiguito
 */
@Table(name = "Presentations")
public class Presentation extends Model implements Parcelable {

    public enum PresentationType {
        SLIDESHARE("SLIDESHARE"), LOCALFILE("LOCALFILE"), GOOGLEDRIVE("GOOGLEDRIVE");

        private final String type;

        /**
         * @param type
         */
        private PresentationType(final String type) {
            this.type = type;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return type;
        }
    }

    public enum PresentationExtensions {
        PDF("pdf"), ODP("odp"), ODT("odt"), ODS("ods"), FODT("fodt"), ODF("odf"),;

        private final String type;

        /**
         * @param type
         */
        private PresentationExtensions(final String type) {
            this.type = type;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return type;
        }

        public static PresentationExtensions stringToPresentationExtensions(String extension) {
            if (extension.equals("pdf")) {
                return PDF;
            } else if (extension.equals("odp")) {
                return ODP;
            } else if (extension.equals("odt")) {
                return ODT;
            } else if (extension.equals("ods")) {
                return ODS;
            } else if (extension.equals("fodt")) {
                return FODT;
            } else if (extension.equals("odf")) {
                return ODF;
            }
            return null;
        }
    }

    @Column
    PresentationType type;
    @Column
    String subType;
    @Column
    PresentationExtensions extension;
    @Column
    String externalId;
    @Column
    String title;
    @Column
    String description;
    @Column
    String author;
    @Column
    String url;
    @Column
    String thumbNailUrl;
    @Column
    String filepath;
    @Column
    String filename;
    @Column
    String tag;
    long lastUse;

    public Presentation() {
        super();
    }

    public PresentationType getType() {
        return type;
    }

    public void setType(PresentationType type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public PresentationExtensions getExtension() {
        return extension;
    }

    public void setExtension(PresentationExtensions extension) {
        this.extension = extension;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbNailUrl() {
        return thumbNailUrl;
    }

    public void setThumbNailUrl(String thumbNailUrl) {
        this.thumbNailUrl = thumbNailUrl;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getLasUse() {
        return lastUse;
    }

    public void setLasUse(long lastUse) {
        this.lastUse = lastUse;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeSerializable(type);
        out.writeString(subType);
        out.writeSerializable(extension);
        out.writeString(externalId);
        out.writeString(title);
        out.writeString(description);
        out.writeString(author);
        out.writeString(url);
        out.writeString(thumbNailUrl);
        out.writeString(filepath);
        out.writeString(filename);
        out.writeString(tag);
    }

    private Presentation(Parcel in) {
        type = (Presentation.PresentationType) in.readSerializable();
        subType = in.readString();
        extension = (Presentation.PresentationExtensions) in.readSerializable();
        externalId = in.readString();
        title = in.readString();
        description = in.readString();
        author = in.readString();
        url = in.readString();
        thumbNailUrl = in.readString();
        filepath = in.readString();
        filename = in.readString();
        tag = in.readString();
    }

    public static final Parcelable.Creator<Presentation> CREATOR = new Parcelable.Creator<Presentation>() {
        public Presentation createFromParcel(Parcel in) {
            return new Presentation(in);
        }

        public Presentation[] newArray(int size) {
            return new Presentation[size];
        }
    };

    public static final List<Presentation> getAllByType(PresentationType presentationType) {
        return new Select().from(Presentation.class).where("type = ?", presentationType).execute();
    }

    public static final List<Presentation> getAllByTypeAndSubType(PresentationType presentationType, String subType) {
        return new Select().from(Presentation.class).where("type = ? AND subType = ?", presentationType, subType).execute();
    }

    public static final void deleteAllByType(PresentationType presentationType) {
        new Delete().from(Presentation.class).where("type = ?", presentationType).execute();
    }

    public static final void deleteAllByTypeAndSubType(PresentationType presentationType, String subType) {
        new Delete().from(Presentation.class).where("type = ? AND subType = ?", presentationType, subType).execute();
    }

    public static void add(Presentation presentation) {
        presentation.save();
    }

    public static final void addAll(List<Presentation> presentations) {
        ActiveAndroid.beginTransaction();
        try {
            for (Presentation presentation : presentations) {
                presentation.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    public static Presentation getLastPresentationsByTypeAndExternalId(PresentationType type, String externalId) {
        Presentation result = null;
        List<Presentation> presentations = new Select().from(Presentation.class).where("type = ? AND externalId = ?", type, externalId).execute();
        if (presentations.size() > 0) {
            result = presentations.get(0);
        }
        return result;
    }

}
