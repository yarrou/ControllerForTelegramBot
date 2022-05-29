package site.alexkononsol.controllerfortelegrambot.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import org.apache.commons.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import site.alexkononsol.controllerfortelegrambot.dao.CityDao;
import site.alexkononsol.controllerfortelegrambot.entity.City;
import site.alexkononsol.controllerfortelegrambot.logHelper.LogHelper;

public class CityPictureService {
    private Context context;

    public CityPictureService(Context context) {
        this.context = context;
    }

    public String createPictureFile(String pictureAlsString) {
        File cacheDir = context.getCacheDir();
        File tempCityPictureFile = null;
        try {
            tempCityPictureFile = File.createTempFile("picture", ".pic_temp", cacheDir);
            FileOutputStream fOut = new FileOutputStream(tempCityPictureFile);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            myOutWriter.append(pictureAlsString);
            myOutWriter.close();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempCityPictureFile.getPath();
    }
    public String createPictureFile(Drawable tempDrawable){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) tempDrawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        String cityPictureString = new String(Base64.encodeBase64(imageInByte));
        return createPictureFile(cityPictureString);
    }
    public String createPictureFile(City city) {
        return createPictureFile(city.getPicture());
    }

    public String getPictureAlsStringFromTempFile(String pathToFile) {
        Path tempPicFile = Paths.get(pathToFile);
        try(BufferedReader reader = Files.newBufferedReader(tempPicFile)) {
            return reader.readLine();
        } catch (IOException e) {
            LogHelper.logError(this,e.getMessage(),e);
            e.printStackTrace();
            return "";
        }
    }

    public String getPictureAlsStringFromDrawable(Drawable drawable) {
        BitmapDrawable bitmapDrawable = ((BitmapDrawable) drawable);
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        return new String(Base64.encodeBase64(imageInByte));
    }

    public Drawable getDrawableFromPictureString(String pictureString) {
        byte[] imageByteArray = Base64.decodeBase64(pictureString.getBytes());
        InputStream inputStream = new ByteArrayInputStream(imageByteArray);
        return Drawable.createFromStream(inputStream, "src name");
    }
    public Drawable getDrawableFromCityDao(CityDao cityDao){
        String picStr = getPictureAlsStringFromTempFile(cityDao.getPictureFilePath());
        return getDrawableFromPictureString(picStr);
    }
}
