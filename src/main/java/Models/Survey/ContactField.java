package Models.Survey;

import java.io.Serializable;

public class ContactField implements Serializable {
    private boolean nameRequire;
    private boolean phoneRequire;
    private boolean emailRequire;
    private boolean addressRequire;

    public boolean isNameRequire() {
        return nameRequire;
    }

    public void setNameRequire(boolean nameRequire) {
        this.nameRequire = nameRequire;
    }

    public boolean isPhoneRequire() {
        return phoneRequire;
    }

    public void setPhoneRequire(boolean phoneRequire) {
        this.phoneRequire = phoneRequire;
    }

    public boolean isEmailRequire() {
        return emailRequire;
    }

    public void setEmailRequire(boolean emailRequire) {
        this.emailRequire = emailRequire;
    }

    public boolean isAddressRequire() {
        return addressRequire;
    }

    public void setAddressRequire(boolean addressRequire) {
        this.addressRequire = addressRequire;
    }
}
