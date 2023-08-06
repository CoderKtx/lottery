package com.raffleclub.app.utils;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;
import smartdevelop.ir.eram.showcaseviewlib.config.PointerType;

public class Utills {


    private static Utills utills = new Utills();

    public static Utills  newInstance() {
        return utills;
    }

    @SuppressLint("SimpleDateFormat")
    public String formatDate(String str){

        String dateString4 = str;
        String date ="";
        //old format
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date date4 = sdf3.parse(dateString4);
            //new format
            SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy/MM/dd hh.mm.ss aa");
            //formatting the given time to new format with AM/PM
            date = sdf4.format(date4);
            Log.i("testTime", "Given date and time in AM/PM: "+sdf4.format(date4));

        }catch(ParseException e){
            e.printStackTrace();
        }
        return date;
    }

    @SuppressLint("SimpleDateFormat")
    public String formatDateAM_PM(String str){

        String dateString4 = str;
        String date ="";
        //old format
        SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
        try{
            Date date4 = sdf3.parse(dateString4);
            //new format
            SimpleDateFormat sdf4 = new SimpleDateFormat("dd/MM/yyyy");
            //formatting the given time to new format with AM/PM
            date = sdf4.format(date4);
            Log.i("testTime", "Given date and time in AM/PM: "+sdf4.format(date4));

        }catch(ParseException e){
            e.printStackTrace();
        }
        return date;
    }


    public  boolean isValidPassword(String password)
    {
        String regex1 = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,25}$";
        Pattern pattern = Pattern.compile(regex1);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public  boolean isValidAlphaNumeric(String text)
    {
        String regex1 = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]+$";
        Pattern pattern = Pattern.compile(regex1);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }
    public boolean isValidLength(String password)
    {
       if (password.length() >= 6)
           return true;
       else
           return false;
    }
    public  boolean isValidUpperCase(String password)
    {
        String regex1 = ".*[A-Z].*";
        Pattern pattern = Pattern.compile(regex1);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    public  boolean isValidlowerCase(String password)
    {
        String regex1 = ".*[a-z].*";
        Pattern pattern = Pattern.compile(regex1);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    public  boolean isValidNumber(String password)
    {
        String regex1 = ".*[0-9].*";
        Pattern pattern = Pattern.compile(regex1);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    public  boolean isValidSymbol(String password)
    {
        String regex1 = ".*[@#$%!].*";
        Pattern pattern = Pattern.compile(regex1);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }


    public void setGuid(View view , String content ){
        new GuideView.Builder(view.getContext())
//                .setTitle(view.getContext().getString(R.string.guide))
                .setContentText(content)
                .setGravity(Gravity.center)//optional
                .setTargetView(view)
                .setPointerType(PointerType.circle)
                .setDismissType(DismissType.outside) //optional - default dismissible by TargetView
                .build()
                .show();
    }

}
