import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName ="data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(json);
    }

    public static void writeString(String json) {
        JSONObject obj = new JSONObject();
        JSONArray list = new JSONArray();
        
        try (FileWriter file = new FileWriter("new_data.json")) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>(){}.getType();
        return gson.toJson(list, listType);
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
}
