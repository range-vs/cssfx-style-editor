package sample;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Controller {

    @FXML
    private GridPane gridCss;
    @FXML
    private Label labelCss;
    @FXML
    private CheckBox checkBoxCss;
    @FXML
    private Button buttonCss;
    @FXML
    private ComboBox comboBoxCss;
    @FXML
    private Slider sliderCss;
    @FXML
    private TextField textFieldCss;
    @FXML
    private Button buttonchoosefile;
    @FXML
    private Button buttonsavefile;
    @FXML
    private TextArea codetext;
    @FXML
    private Label labelfilename;
    @FXML
    private ChoiceBox<String> listsheets;
    @FXML
    private ChoiceBox<String> box;
    @FXML
    private ChoiceBox<String> fontsize;

    private Model model;
    private String filename;
    private StringBuilder allCssFile;
    private String currentCssClass;
    private HashMap<String, Parent> controls;

    public void initialize(){
        currentCssClass = null;
        listsheets.setItems(FXCollections.observableArrayList(Font.getFamilies()));
        listsheets.getSelectionModel().select(0);
        ArrayList<String> size = new ArrayList<>();
        for(int i = 0;i<30;i++){
            size.add((i+1) + "px");
        }
        fontsize.setItems(FXCollections.observableArrayList(size));
        fontsize.getSelectionModel().select(0);
        controls = new HashMap<String, Parent>(){{
            put(".root", gridCss);
            put(".label", labelCss);
            put(".check-box", checkBoxCss);
            put(".button", buttonCss);
            put(".combo-box", comboBoxCss);
            put(".slider", sliderCss);
            put(".text-field", textFieldCss);
        }};
    }


    public void chooseFileAction() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSS files (*.css)", "*.css");
        fileChooser.setTitle("Open style file");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(null);
        if(file == null){
            return;
        }
        model = new Model();
        System.out.println(file.getAbsoluteFile());
        String filepath = file.getAbsolutePath();
        filename = file.getName();
        gridCss.getStylesheets().add("file:///" + filepath.replace('\\', '/'));
        labelfilename.setText(filename);
        model.readFile(filepath);
        HashMap<String, HashMap<String, String>> parsedData = model.parseData();
        box.setItems(FXCollections.observableArrayList(model.getAllClasses(parsedData)));
        box.setValue(model.getAllClasses(parsedData).get(0));
        currentCssClass = model.getAllClasses(parsedData).get(0);
        box.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currentCssClass = box.getSelectionModel().getSelectedItem();
            }
        });
        // load file to text field
        createCssAllText(parsedData);
        codetext.setText(allCssFile.toString());
        // init styles
        setAllStyle();
    }

    private void createCssAllText(HashMap<String, HashMap<String, String>> parsedData){
        allCssFile = new StringBuilder();
        for(Map.Entry<String, HashMap<String, String>> kv: parsedData.entrySet()){
            allCssFile.append(kv.getKey() + "{");
            for(Map.Entry v: kv.getValue().entrySet()){
                allCssFile.append("\r\n     " + v.getKey() + ": " + v.getValue() + ";");
            }
            allCssFile.append("\n}\n\n");
        }
    }

    private void setStyle(Parent cnt, String key){
        cnt.getStylesheets().clear();
        HashMap<String, String> coll = model.getStyles().get(key);
        StringBuilder cssClass = new StringBuilder();
        for(Map.Entry elem: coll.entrySet()) {
            cssClass.append(elem.getKey() + ": "+elem.getValue()+"; ");
        }
        cnt.setStyle(cssClass.toString());
    }

    private void setAllStyle(){
        for(Map.Entry<String, Parent> value: controls.entrySet()){
            setStyle(value.getValue(), value.getKey());
        }
    }

    public void actionCssProperty(Event actionEvent) {
        if(currentCssClass != null) {
            String key = (((Parent)actionEvent.getSource()).getUserData()).toString();
            String value = "";
            if(actionEvent.getSource() instanceof ColorPicker){
                StringBuilder v = new StringBuilder(((ColorPicker)actionEvent.getSource()).getValue().toString());
                v.delete(0, 2);
                v.insert(0,'#');
                value = v.toString();
            }else if(actionEvent.getSource() instanceof TextField){
                value = ((TextField)actionEvent.getSource()).getText();
                if(value.equals("")){
                    value = "0";
                }
            }else if(actionEvent.getSource() instanceof ChoiceBox){
                value = ((ChoiceBox)actionEvent.getSource()).getValue().toString();
            }
            if(value.equals("")){
                return;
            }
            System.out.println(value);
            if(!tryPadding(key, value)) {
                HashMap<String, String> currentClassProperties = model.getStyles().get(currentCssClass);
                currentClassProperties.put(key, value);
            }
            createCssAllText(model.getStyles());
            codetext.setText(allCssFile.toString());
            setStyle(controls.get(currentCssClass), currentCssClass);
        }
    }

    private boolean tryPadding(String key, String value){
        if(key.indexOf("padding") == -1){
            return false;
        }
        String [] params = key.split("-");
        HashMap<String, String> currentClassProperties = model.getStyles().get(currentCssClass);
        if(!currentClassProperties.containsKey("-fx-padding")){
            currentClassProperties.put("-fx-padding", "0 0 0 0");
        }
        String padding = currentClassProperties.get("-fx-padding");
        String [] partsPadding = padding.split(" ");
        HashMap<String, Integer> indexs = new HashMap<String, Integer>(){{
           put("up", 0);
           put("right", 1);
           put("bottom", 2);
           put("left", 3);
        }};
        partsPadding[indexs.get(params[1])] = value;
        padding = "";
        for(int i = 0;i<partsPadding.length;i++){
            padding += partsPadding[i] + " ";
        }
        currentClassProperties.put("-fx-padding", padding);
        return true;
    }

    public void saveStyle(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSS files (*.css)", "*.css");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("Save style file as...");
        File file = fileChooser.showSaveDialog(null);
        try(FileWriter writer = new FileWriter(file.getAbsolutePath(), false))
        {
            writer.write(allCssFile.toString());
            writer.flush();
        }
        catch(Exception ex){

            System.out.println(ex.getMessage());
        }
    }
}
