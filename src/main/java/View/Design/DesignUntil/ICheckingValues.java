package View.Design.DesignUntil;

public interface ICheckingValues {
    boolean checkValidUsername(String data);//true->valid
    boolean checkValidPassword(String data);
    boolean checkValidEmail(String data);
    boolean checkPhoneNumber(String data);

}
