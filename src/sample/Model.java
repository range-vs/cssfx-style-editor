package sample;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Model{

    private byte data[] = new byte[4096];
    private HashMap<String,HashMap<String,String>> all;

    //чтение файла
    public void readFile(String path) {
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            try {
                fileInputStream.read(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fileInputStream.close();
            } catch (IOException e) {

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //читает и записывает
    public HashMap<String,HashMap<String,String>> parseData(){
        ArrayList<String> classes = new ArrayList<>();
        all = new HashMap<>();
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<data.length;i++) {
            stringBuilder.append((char)data[i]);
        }
        String chunkofdata = stringBuilder.toString();
        String chunksofdata[] = chunkofdata.split("}");
        StringBuilder classname = new StringBuilder();
        StringBuilder cssvalues = new StringBuilder();
        int counter = 0;
        for(int i=0;i<chunksofdata.length;i++) {
            HashMap<String,String> css = new HashMap<>();
            char onechunk[] = chunksofdata[i].toCharArray();
            for(int j=0;j<onechunk.length;j++){
                if(onechunk[j] == '.') {
                    while(onechunk[j] != '{') {
                        classname.append(onechunk[j]);
                        j++;
                    }
                    classes.add(classname.toString());
                    classname.delete(0,classname.capacity());
                }
                if(onechunk[j] == '{') {
                     while(j < onechunk.length && onechunk[j] != '.'){
                         if(onechunk[j] != '{') {
                             cssvalues.append(onechunk[j]);
                         }
                         j++;
                     }
                     String cssValues = cssvalues.toString().replaceAll("\r\n    ", "");
                     String attr[] = cssValues.split(";");
                     for(int k=0;k<attr.length-1;k++){
                         String tmp[] = attr[k].split(":");
                            css.put(tmp[0],tmp[1]);
                     }
                     cssvalues.delete(0,cssvalues.capacity());
                     all.put(classes.get(counter),css);
                     counter++;
                }
            }
        }
        return all;
    }
//печать в консоль
  //  public void printParsedData( HashMap<String,HashMap<String,String>> data) {
  //      Iterator<Map.Entry<String,HashMap<String,String>>> it = data.entrySet().iterator();
   //     while(it.hasNext()){
    //        Map.Entry<String,HashMap<String,String>> en = it.next();
    //        System.out.println();
    //        System.out.print(en.getKey());
     //       Iterator<Map.Entry<String,String>> map = en.getValue().entrySet().iterator();
     //       while(map.hasNext()) {
      //          Map.Entry<String,String> tmp = map.next();
      //          System.out.print(tmp.getKey()+" "+tmp.getValue());
       //     }
      //      System.out.println();
    //    }
  //  }

    public String getClassByName(HashMap<String,HashMap<String,String>> data,String classname) {
        Iterator<Map.Entry<String,HashMap<String,String>>> it = data.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String,HashMap<String,String>> en = it.next();
            if(classname.equals(en.getKey())) {
                return classname;
            }
        }
        return null;
    }

    public ArrayList<String> getAllClasses(HashMap<String,HashMap<String,String>> data) {
        Iterator<Map.Entry<String,HashMap<String,String>>> it = data.entrySet().iterator();
        ArrayList<String> classes = new ArrayList<>();
        while(it.hasNext()){
            Map.Entry<String,HashMap<String,String>> en = it.next();
            classes.add(en.getKey());
        }
        return classes;
    }

    public HashMap<String,String> getAllCssPropertitesByClass(HashMap<String,HashMap<String,String>> data,String classname){
        Iterator<Map.Entry<String,HashMap<String,String>>> it = data.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String,HashMap<String,String>> en = it.next();
            System.out.println();
            if(en.getKey().equals(classname)){
               return en.getValue();
            }
        }
        return null;
    }

    public HashMap<String, HashMap<String, String>> getStyles() {
        return all;
    }
}

