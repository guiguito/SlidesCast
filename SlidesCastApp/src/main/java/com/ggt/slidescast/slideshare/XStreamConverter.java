package com.ggt.slidescast.slideshare;

import com.ggt.slidescast.slideshare.model.Favorite;
import com.ggt.slidescast.slideshare.model.Favorites;
import com.ggt.slidescast.slideshare.model.Message;
import com.ggt.slidescast.slideshare.model.Meta;
import com.ggt.slidescast.slideshare.model.SlideShareServiceError;
import com.ggt.slidescast.slideshare.model.SlideShowDeleted;
import com.ggt.slidescast.slideshare.model.SlideShowUploaded;
import com.ggt.slidescast.slideshare.model.Slideshow;
import com.ggt.slidescast.slideshare.model.Slideshows;
import com.ggt.slidescast.slideshare.model.User;
import com.ggt.slidescast.utils.GLog;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.MapperWrapper;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/**
 * XStream Converter for retrofit.
 *
 * @author guiguito
 */
public class XStreamConverter implements Converter {

    private static final String CHARSET = "UTF-8";
    private static final String MIME_TYPE = "application/xml; charset=" + CHARSET;

    private static XStream mXStream;

    public XStreamConverter() {
        if (mXStream == null) {
            mXStream = new XStream() {
                @Override
                protected MapperWrapper wrapMapper(MapperWrapper next) {
                    return new MapperWrapper(next) {
                        @Override
                        public boolean shouldSerializeMember(@SuppressWarnings("rawtypes") Class definedIn, String fieldName) {
                            if (definedIn == Object.class) {
                                return false;
                            }
                            return super.shouldSerializeMember(definedIn, fieldName);
                        }
                    };
                }
            };
            mXStream.autodetectAnnotations(true);
            mXStream.alias("SlideShareServiceError", SlideShareServiceError.class);
            mXStream.alias("favorite", Favorite.class);
            mXStream.alias("favorites", Favorites.class);
            mXStream.alias("Message", Message.class);
            mXStream.alias("User", User.class);
            mXStream.alias("Meta", Meta.class);
            mXStream.alias("Slideshow", Slideshow.class);
            mXStream.alias("Slideshows", Slideshows.class);
            mXStream.alias("SlideShowUploaded", SlideShowUploaded.class);
            mXStream.alias("SlideShowDeleted", SlideShowDeleted.class);
        }
    }

    @Override
    public Object fromBody(TypedInput body, Type type) throws ConversionException {
        try {
            String result = getStringFromInputStream(body.in());
            GLog.d(getClass().getName(), result);
            Object resultObj = mXStream.fromXML(result);
            return resultObj;
            // return mXStream.fromXML(body.in());
        } catch (Exception e) {
            throw new ConversionException(e);
        }
    }

    @Override
    public TypedOutput toBody(Object source) {
        OutputStreamWriter osw = null;

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            osw = new OutputStreamWriter(bos, CHARSET);
            mXStream.toXML(source, osw);
            osw.flush();
            return new TypedByteArray(MIME_TYPE, bos.toByteArray());
        } catch (Exception e) {
            throw new AssertionError(e);
        } finally {
            try {
                if (osw != null) {
                    osw.close();
                }
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }
    }

    // convert InputStream to String
    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

}
