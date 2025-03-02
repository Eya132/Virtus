package Controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class JavaConnector {
    private final StringProperty addressProperty = new SimpleStringProperty();

    public StringProperty addressProperty() {
        return addressProperty;
    }

    public void sendAddressToJava(String address) {
        addressProperty.set(address);
    }
}