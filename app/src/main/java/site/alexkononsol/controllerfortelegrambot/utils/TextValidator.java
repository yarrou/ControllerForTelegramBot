package site.alexkononsol.controllerfortelegrambot.utils;

import android.widget.TextView;

public class TextValidator {
    public static boolean noEmptyValidation(TextView view){
        String text = view.getText().toString();
        text.trim();
        if (text.length()<2){
            view.setError("this field is required");
            return false;
        }else return true;
    }
}
