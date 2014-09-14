package com.ggt.slidescast.model.app;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Data model for a presentation use.
 * Time stamp of when the presentation was played.
 *
 * @author guiguito
 */
@Table(name = "PresentationUses")
public class PresentationUse extends Model implements Parcelable {

    private static final int HISTORY_DISPLAYED_SIZE = 10;
    @Column
    Presentation.PresentationType type;
    @Column
    String externalId;
    @Column
    long lastUse;

    public PresentationUse() {
        super();
    }

    public Presentation.PresentationType getType() {
        return type;
    }

    public void setType(Presentation.PresentationType type) {
        this.type = type;
    }


    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public long getLastUse() {
        return lastUse;
    }

    public void setLastUse(long lastUse) {
        this.lastUse = lastUse;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeSerializable(type);
        out.writeString(externalId);
        out.writeLong(lastUse);
    }

    private PresentationUse(Parcel in) {
        type = (Presentation.PresentationType) in.readSerializable();
        externalId = in.readString();
        lastUse = in.readLong();
    }

    public static final Creator<PresentationUse> CREATOR = new Creator<PresentationUse>() {
        public PresentationUse createFromParcel(Parcel in) {
            return new PresentationUse(in);
        }

        public PresentationUse[] newArray(int size) {
            return new PresentationUse[size];
        }
    };

    public static final List<Presentation> getLastPresentationsUsed() {
        List<PresentationUse> presentationUses = new Select().from(PresentationUse.class).where("lastUse > 0").orderBy("lastUse DESC").limit(HISTORY_DISPLAYED_SIZE).execute();
        List<Presentation> presentations = new ArrayList<Presentation>();
        for (PresentationUse presentationUse : presentationUses) {
            Presentation presentation = Presentation.getLastPresentationsByTypeAndExternalId(presentationUse.type, presentationUse.externalId);
            presentation.setLasUse(presentationUse.lastUse);
            if (presentation != null) {
                presentations.add(presentation);
            }
        }
        return presentations;
    }

    public static final void deleteAll() {
        new Delete().from(PresentationUse.class).execute();
    }

    public static void addOrUpdate(Presentation.PresentationType type, String externalId) {
        PresentationUse result = null;
        List<PresentationUse> presentationsUses = new Select().from(PresentationUse.class).where("type = ? AND externalId = ?", type, externalId).execute();
        if (presentationsUses.size() > 0) {
            result = presentationsUses.get(0);
        } else {
            result = new PresentationUse();
            result.type = type;
            result.externalId = externalId;
        }
        result.setLastUse(System.currentTimeMillis());
        result.save();
    }
}
