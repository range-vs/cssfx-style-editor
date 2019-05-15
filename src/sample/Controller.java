package sample;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import java.io.*;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Controller {

    @FXML
    private GridPane gridPane;
    @FXML
    private Button buttonchoosefile;

    @FXML
    private Button buttonsavefile;

    @FXML
    private TextArea codetext;

    @FXML
    private Label labelfilename;

    @FXML
    private ChoiceBox<String> box;

    @FXML
    private ChoiceBox<String> fontsize;

    @FXML
    private ColorPicker backfont;

    private Model model = new Model();

    private String filename;

    public Controller(){
        box=new ChoiceBox<>();
    }


    public void chooseFileAction() throws MalformedURLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(null);
        System.out.println(file.getAbsoluteFile());
        String filepath = file.getAbsolutePath();
        filename = file.getName();


        gridPane.getStylesheets().add((filename));

      labelfilename.setText(filename);
                model.readFile(filepath);
                HashMap<String, HashMap<String,String>> parsedData = model.parseData();
      //          model.printParsedData(parsedData);
                box.setItems(FXCollections.observableArrayList(model.getAllClasses(parsedData)));
                fontsize.setItems(FXCollections.observableArrayList("10px","11px","12px","13px","14px","15px"));
                box.setValue(model.getAllClasses(parsedData).get(0));
                box.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                       HashMap<String,String> hashMap = model.getAllCssPropertitesByClass(parsedData,box.getSelectionModel().getSelectedItem());
                      // System.out.println(hashMap.entrySet().iterator().next().getValue());
                        Iterator<Map.Entry<String,String>> it = hashMap.entrySet().iterator();
                        //System.out.println(box.getSelectionModel().getSelectedItem());
                               // System.out.println(box.getSelectionModel().getSelectedItem()+" "+model.getAllClasses(parsedData).get(i));
                                while(it.hasNext()){
                                    Map.Entry<String,String> en = it.next();
                                   // System.out.println(en.getValue());
                                    if(en.getKey().contains("background") && en.getKey().contains("color")){
                                        backfont.setValue(Color.web(en.getValue()));
                                    }
                                    if(en.getKey().contains("font") && en.getKey().contains("size")){
                                        fontsize.setValue(en.getValue());
                                    } else {
                                        fontsize.setValue(null);
                                    }
                                }

                        }
                });
        byte[] b=new byte[4096];
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileInputStream inputStream = new FileInputStream(filepath);
            try {
                inputStream.read(b);
                for(int i=0;i<b.length;i++) {
                    stringBuilder.append((char)b[i]);
                }
                codetext.setText(stringBuilder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

  /*  public void saveFileAction() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String data = codetext.getText();
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(filename+"copy");
                    try {
                        fileOutputStream.write(data.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }*/
}
