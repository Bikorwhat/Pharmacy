import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class OracleDatabaseToArrayExample {
    public static void main(String[] args) {
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String username = "system";
        String password = "1234";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM MEDICINE");

            // Assuming you want to store data in an array
            // Replace "DataType" with the appropriate Java data type you're storing
            String[] dataArray = new String[resultSet.getFetchSize()];
            int i = 0;

            while (resultSet.next()) {
                String data = resultSet.getString("NAME"); // Ensure "NAME" matches the actual column name
                if (data != null) {
                    dataArray[i] = data;
                    i++;
                }
            }

            // Now dataArray contains the data from the database
            for (int j = 0; j < i; j++) {
                System.out.println(dataArray[j]);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
