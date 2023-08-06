package com.raffleclub.app.utils;


import android.text.method.PasswordTransformationMethod;
import android.view.View;

public  class MyPasswordTransformationMethod extends PasswordTransformationMethod {
    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return new PasswordCharSequence(source);
    }

    private class PasswordCharSequence implements CharSequence {
        private CharSequence mSource;
        public PasswordCharSequence(CharSequence source) {
            mSource = source;
        }
        public char charAt(int index) {
            char caracter;
            switch (index)
            {
//                case 4:
//                    caracter = ' ';
//                    break;
//                case 9:
//                    caracter = ' ';
//                    break;
//                case 14:
//                    caracter = ' ';
//                    break;
//                default:
//                    if(index < 15)
//                        return '*';
//                    else
//                        caracter = mSource.charAt(index);
//                    break;
                default:
                    if(index < mSource.length()-1)
                        return '*';
                    else
                        caracter = mSource.charAt(index);
                    break;



            }


            return caracter;
        }
        public int length() {
            return mSource.length();
        }
        public CharSequence subSequence(int start, int end) {
            return mSource.subSequence(start, end);
        }
    }
}