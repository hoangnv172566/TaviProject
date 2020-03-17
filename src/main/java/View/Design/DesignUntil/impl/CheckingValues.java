package View.Design.DesignUntil.impl;

import View.Design.DesignUntil.ICheckingValues;

public class CheckingValues implements ICheckingValues {

    @Override
    public boolean checkValidUsername(String data) {
        return false;
    }

    @Override
    public boolean checkValidPassword(String data) {
        return false;
    }

    @Override
    public boolean checkValidEmail(String data) {
        return false;
    }

    @Override
    public boolean checkPhoneNumber(String data) {
        return false;
    }
}
