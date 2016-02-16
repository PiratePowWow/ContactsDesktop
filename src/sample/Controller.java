package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import jodd.json.JsonParser;
import jodd.json.JsonSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Controller implements Initializable{
    ObservableList<Contact> contacts = FXCollections.observableArrayList();

    @FXML
    ListView list;

    public void addContact() throws IOException {
        if ((!name.getText().isEmpty()) && (!phone.getText().isEmpty()) && (!email.getText().isEmpty())) {
            contacts.add(new Contact(name.getText(), phone.getText(), email.getText()));
            name.setText("");
            phone.setText("");
            email.setText("");
            File f = new File("contacts.json");
            FileWriter fw = new FileWriter(f);
            JsonSerializer serializer = new JsonSerializer();
            String json = serializer.serialize(contacts);
            fw.write(json);
            fw.close();
        }

    }
    @FXML
    TextField name;
    @FXML
    TextField phone;
    @FXML
    TextField email;


    public void removeContact() throws IOException {
        Contact contact = (Contact) list.getSelectionModel().getSelectedItem();
        contacts.remove(contact);
        File f = new File("contacts.json");
        FileWriter fw = new FileWriter(f);
        JsonSerializer serializer = new JsonSerializer();
        String json = serializer.serialize(contacts);
        fw.write(json);
        fw.close();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File f = new File("contacts.json");
            Scanner s = null;
            try {
                s = new Scanner(f);
                s.useDelimiter("\\Z");
                String contents = s.next();
                JsonParser parser = new JsonParser();
                ArrayList<HashMap> contactlist = parser.parse(contents);
                for (HashMap<String, String> contactMap: contactlist){
                    contacts.add(new Contact(contactMap.get("name"),contactMap.get("phone"), contactMap.get("email") ));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        list.setItems(contacts);
    }
}
