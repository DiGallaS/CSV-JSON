import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        String fileName1 = "new_data.json";
        String fileName2 = "data2.json";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(json, fileName1);


        //Задача 2.
        List<Employee> list2 = parseXML("data.xml");
        String json2 = listToJson(list2);
        writeString(json2, fileName2);


        //Задача 3
        String json3 = readString(fileName1);
        List<Employee> list3 = jsonToList(json3);
        for (Employee employee :
                list3) {
            System.out.println(employee);
        }
    }


    public static void writeString(String json, String fileName) {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> String listToJson(List<T> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        return gson.toJson(list);
    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> staff = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy =
                    new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            staff = csv.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return staff;
    }


    //задача 2
    private static List<Employee> parseXML(String s) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(s));

        Node root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        List<Employee> staff2 = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node2 = nodeList.item(i);
            if (Node.ELEMENT_NODE == node2.getNodeType()) {
                NodeList nodeList1 = node2.getChildNodes();
                Employee employee = new Employee();
                for (int j = 0; j < nodeList1.getLength(); j++) {
                    Node node3 = nodeList1.item(j);
                    switch (node3.getNodeName()) {
                        case "id" -> employee.id = Long.parseLong(node3.getTextContent());
                        case "firstName" -> employee.firstName = node3.getTextContent();
                        case "lastName" -> employee.lastName = node3.getTextContent();
                        case "country" -> employee.country = node3.getTextContent();
                        case "age" -> employee.age = Integer.parseInt(node3.getTextContent());
                    }
                }
                staff2.add(employee);
            }
        }
        return staff2;
    }


    //Задание 3
    private static List<Employee> jsonToList(String json) {
        List<Employee> list = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try {
            JSONArray jsonArray = (JSONArray) parser.parse(json);
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            for (Object obj : jsonArray) {
                list.add(gson.fromJson(obj.toString(), Employee.class));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return list;
    }

    private static String readString(String fileName) {
        JSONParser parser = new JSONParser();
        String str = "";
        try {
            JSONArray jsonObject = (JSONArray) parser.parse(new FileReader(fileName));
            str = String.valueOf(jsonObject);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}
