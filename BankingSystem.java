import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
/**
 * Manage connection to database and perform SQL statements.
 */
public class BankingSystem {
	// Connection properties
	private static String driver;
	private static String url;
	private static String username;
	private static String password;

	// JDBC Objects
	private static Connection con;
	private static Statement stmt;
	private static ResultSet rs;

	/**
	 * Initialize database connection given properties file.
	 * @param filename name of properties file
	 */
	public static void init() {
		try {
			Properties props = new Properties();						// Create a new Properties object
			FileInputStream input = new FileInputStream("C:\\users\\hient\\db.properties");	// Create a new FileInputStream object using our filename parameter
			props.load(input);										// Load the file contents into the Properties object
			driver = props.getProperty("jdbc.driver");				// Load the driver
			url = props.getProperty("jdbc.url");					// Load the url
			username = props.getProperty("jdbc.username");			// Load the username
			password = props.getProperty("jdbc.password");			// Load the password
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test database connection.
	 */
	public static void testConnection() {
		System.out.println(":: TEST - CONNECTING TO DATABASE");
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			con.close();
			System.out.println(":: TEST - SUCCESSFULLY CONNECTED TO DATABASE");
		} catch (Exception e) {
			System.out.println(":: TEST - FAILED CONNECTED TO DATABASE");
			e.printStackTrace();
		}
	}

	/**
	 * Create a new customer.
	 * @param name customer name
	 * @param gender customer gender
	 * @param age customer age
	 * @param pin customer pin
	 */
	public static void newCustomer(String name, String gender, String age, String pin) 
	{
		System.out.println(":: CREATE NEW CUSTOMER - RUNNING");
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement();
			if(!isNumeric(pin)){
				System.out.println(":: CREATE NEW CUSTOMER - ERROR - INVALID PIN");
			}
			else if(Integer.parseInt(pin) < 0) {
				System.out.println(":: CREATE NEW CUSTOMER - ERROR - INVALID PIN");
			}
			else if(!isNumeric(age)) {
				System.out.println(":: CREATE NEW CUSTOMER - ERROR - INVALID AGE");
			}
			else if(!(gender.equals("M") || gender.equals("F"))) {
				System.out.println(":: CREATE NEW CUSTOMER - ERROR - INVALID GENDER");
			}
			else {
				String query = "INSERT INTO p1.customer(name,gender,age,pin) VALUES('"+name+"','"+gender+"',"
						+Integer.parseInt(age)+","+Integer.parseInt(pin)+")";
				stmt.execute(query);
				stmt.close();
				System.out.println(":: CREATE NEW CUSTOMER - SUCCESS");
			}           
			con.close(); 
		} catch (Exception e) {
			System.out.println(":: TEST - FAILED OPEN NEW CUSTOMER");
			e.printStackTrace();
		}
	}

	/**
	 * Open a new account.
	 * @param id customer id
	 * @param type type of account
	 * @param amount initial deposit amount
	 */
	public static void openAccount(String id, String type, String amount) 
	{
		System.out.println(":: OPEN ACCOUNT - RUNNING");
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement(); 	
			int count = 0;
			String query = "SELECT * FROM p1.customer WHERE id = "+ id;
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				count += 1;
			}
			if(count==0) {
				System.out.println(":: OPEN ACCOUNT - ERROR - INVALID CUSTOMER ID");
			}
			else if(!isNumeric(amount)) {
				System.out.println(":: OPEN ACCOUNT - ERROR - INVALID AMOUNT");
			}
			else if (Integer.parseInt(amount) < 0) {
				System.out.println(":: OPEN ACCOUNT - ERROR - INVALID AMOUNT");
			}
			else if(!(type.equals("C")||type.equals("S"))) {
				System.out.println(":: OPEN ACCOUNT - ERROR - INVALID TYPE");
			}
			else {
				query = "INSERT INTO p1.account(id,type,balance,status) VALUES("+Integer.parseInt(id)+",'"+type+"','"+Integer.parseInt(amount)+"', 'A')";	
				stmt.execute(query);
				System.out.println(":: OPEN ACCOUNT - SUCCESS");
			}
			stmt.close();                                                                         
			con.close(); 	
		} catch (Exception e) {
			System.out.println(":: TEST - FAILED OPEN NEW ACCOUNT");
			e.printStackTrace();
		}
	}

	/**
	 * Close an account.
	 * @param accNum account number
	 */
	public static void closeAccount(String accNum) 
	{
		System.out.println(":: CLOSE ACCOUNT - RUNNING");
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement(); 	
			int count = 0;
			String query = "SELECT * FROM p1.account WHERE number = " + accNum;
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				count += 1;
			}
			if(count==0) {
				System.out.println("INVALID ACCOUNT");
			}
			else {
				query = "UPDATE p1.account SET balance = 0, status = 'I' WHERE number = " + accNum;	
				stmt.execute(query);
				System.out.println(":: CLOSE ACCOUNT - SUCCESS");
			}
			stmt.close();                                                                         
			con.close(); 		
		} catch (Exception e) {
			System.out.println(":: TEST - FAILED CLOSE ACCOUNT");
			e.printStackTrace();
		}
	}

	/**
	 * Deposit into an account.
	 * @param accNum account number
	 * @param amount deposit amount
	 */
	public static void deposit(String accNum, String amount) 
	{
		System.out.println(":: DEPOSIT - RUNNING");
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement(); 	
			int count = 0;
			if(!isNumeric(amount)) {
				System.out.println(":: DEPOSIT - ERROR - INVALID AMOUNT");
			}
			else if(Integer.parseInt(amount) < 0){
				System.out.println(":: DEPOSIT - ERROR - INVALID AMOUNT");
			}
			else {
				String query = "SELECT * FROM p1.account WHERE number = " + accNum + " AND status = 'A'";
				rs = stmt.executeQuery(query);
				while(rs.next()) {
					count += 1;
				}
				if(count==0) {
					System.out.println(":: DEPOSIT - ERROR - INVALID ACCOUNT");
				}
				else {
					query = "UPDATE p1.account SET balance = balance + " + amount + " WHERE number = " + accNum;	
					stmt.execute(query);
					System.out.println(":: DEPOSIT ACCOUNT - SUCCESS");
				}
			}	
			stmt.close();                                                                         
			con.close(); 	
		} catch (Exception e) {
			System.out.println(":: TEST - FAILED DEPOSIT");
			e.printStackTrace();
		}	
	}

	/**
	 * Withdraw from an account.
	 * @param accNum account number
	 * @param amount withdraw amount
	 */
	public static void withdraw(String accNum, String amount) 
	{
		System.out.println(":: WITHDRAW - RUNNING");
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement(); 	
			int count = 0;
			if(!isNumeric(amount)) {
				System.out.println(":: WITHDRAW - ERROR - INVALID AMOUNT");
			}
			else if(Integer.parseInt(amount) < 0){
				System.out.println(":: WITHDRAW - ERROR - INVALID AMOUNT");
			}
			else if(!isNumeric(accNum)) {
				System.out.println(":: WITHDRAW - ERROR - INVALID ACCOUNT");
			}
			else {
				String bal = null;
				String query = "SELECT * FROM p1.account WHERE number = " + accNum + " AND status = 'A'";
				rs = stmt.executeQuery(query);
				while(rs.next()) {
					count += 1;
					bal = rs.getString("balance");
				}
				if(count==0) {
					System.out.println(":: WITHDRAW - ERROR - INVALID ACCOUNT OR ACCOUNT IS INACTIVE");
				}
				else {
					if(Integer.parseInt(amount)  > Integer.parseInt(bal)){
						System.out.println(":: WITHDRAW - ERROR - NOT ENOUGH FUNDS");
					}
					else {
						query = "UPDATE p1.account SET balance = balance - " + amount + " WHERE number = " + accNum;	
						stmt.execute(query);
						System.out.println(":: WITHDRAW - SUCCESS");
					}
				}
			}
			stmt.close();                                                                         
			con.close(); 
		} catch (Exception e) {
			System.out.println(":: TEST - FAILED WITHDRAW");
			e.printStackTrace();
		}
	}

	/**
	 * Transfer amount from source account to destination account. 
	 * @param srcAccNum source account number
	 * @param destAccNum destination account number
	 * @param amount transfer amount
	 */
	public static void transfer(String srcAccNum, String destAccNum, String amount) 
	{
		System.out.println(":: TRANSFER - RUNNING");
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement(); 	
			int count = 0;
			if(!isNumeric(amount)) {
				System.out.println(":: TRANSFER - ERROR - INVALID AMOUNT");
			}
			else if(Integer.parseInt(amount) < 0){
				System.out.println(":: TRANSFER - ERROR - INVALID AMOUNT");
			}
			else if(!isNumeric(srcAccNum) || !isNumeric(destAccNum)) {
				System.out.println(":: TRANSFER - ERROR - INVALID ACCOUNT");
			}
			else {
				String bal = null;
				String query = "SELECT * FROM p1.account WHERE number = " + srcAccNum + " AND status = 'A'";
				rs = stmt.executeQuery(query);
				while(rs.next()) {
					count += 1;
					bal = rs.getString("balance");
				}
				if(count==0) {
					System.out.println(":: TRANSFER - ERROR - INVALID ACCOUNT OR ACCOUNT IS INACTIVE");
				}
				else {
					if(Integer.parseInt(amount)  > Integer.parseInt(bal)){
						System.out.println(":: TRANSFER - ERROR - NOT ENOUGH FUNDS");
					}
					else {
						count = 0;	// reset count
						query = "SELECT * FROM p1.account WHERE number = " + destAccNum + " AND status = 'A'";
						rs = stmt.executeQuery(query);
						while(rs.next()) {
							count += 1;
						}
						if(count==0) {
							System.out.println(":: TRANSFER - ERROR - INVALID ACCOUNT OR ACCOUNT IS INACTIVE");
						}
						else {
							query = "UPDATE p1.account SET balance = balance - " + amount + " WHERE number = " + srcAccNum;
							stmt.execute(query);
							query = "UPDATE p1.account SET balance = balance + " + amount + " WHERE number = " + destAccNum;
							stmt.execute(query);
							System.out.println(":: TRANSFER - SUCCESS");
						}
					}
				}
			}
			stmt.close();                                                                         
			con.close(); 
		} catch (Exception e) {
			System.out.println(":: TEST - FAILED TRANSFER");
			e.printStackTrace();
		}
	}

	/**
	 * Display account summary.
	 * @param cusID customer ID
	 */
	public static void accountSummary(String cusID) 
	{
		System.out.println(":: ACCOUNT SUMMARY - RUNNING");
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement(); 	
			int count = 0;
			if(!isNumeric(cusID)) {
				System.out.println(":: ACCOUNT SUMMARY - ERROR - INVALID CUSTOMER ID");
			}
			else {
				String query = "SELECT * FROM p1.account WHERE id = "+ cusID + " AND status = 'A'";
				rs = stmt.executeQuery(query);
				ArrayList<String> numbers = new ArrayList<String>();
				ArrayList<String> balances = new ArrayList<String>();
				while(rs.next()) {
					count += 1;
					numbers.add(rs.getString("number"));
					balances.add(rs.getString("balance"));
				}
				if(count==0) {
					System.out.println(":: ACCOUNT SUMMARY - ERROR - INVALID CUSTOMER ID");
				}
				else {
					count = 0;
					System.out.println("ACCOUNT\t\tBALANCE");
					System.out.println("----------------------------");
					for(int i = 0; i < numbers.size();i++) {						
						System.out.println(numbers.get(i) + "\t\t" + balances.get(i));
						count += Integer.parseInt(balances.get(i));
					}
					System.out.println("----------------------------");
					System.out.println("TOTAL\t\t" + count);
					System.out.println(":: ACCOUNT SUMMARY - SUCCESS");
				}
			}		
			stmt.close();                                                                         
			con.close(); 	
		} catch (Exception e) {
			System.out.println(":: TEST - FAILED ACCOUNT SUMMARY");
			e.printStackTrace();
		}
	}

	/**
	 * Display Report A - Customer Information with Total Balance in Decreasing Order.
	 */
	public static void reportA() 
	{
		System.out.println(":: REPORT A - RUNNING");
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement(); 	
			String id, name, gender, age, total;
			String query = "select C.id, C.name, C.gender, C.age, SUM(A.Balance) As TOTAL "
					+ "from p1.customer C, p1.account A "
					+ "where C.id = A.id "
					+ "group by C.id, C.name, C.gender, C.age "
					+ "order by TOTAL DESC";
			rs = stmt.executeQuery(query);
			System.out.println("ID\t\tName\t\tGENDER\t\tAGE\t\tTOTAL");
			System.out.println("---------------------------------------------------------------------");
			while(rs.next()) {
				id = rs.getString("id");
				name = rs.getString("name");
				gender = rs.getString("gender");
				age = rs.getString("age");
				total = rs.getString("TOTAL");
				System.out.println(id + "\t\t"+ name + "\t\t" + gender + "\t\t" + age + "\t\t" + total);
			}	
			System.out.println(":: REPORT A - SUCCESS");
			stmt.close();                                                                         
			con.close(); 	
		} catch (Exception e) {
			System.out.println(":: TEST - FAILED REPORT A");
			e.printStackTrace();
		}
	}

	/**
	 * Display Report B - Customer Information with Total Balance in Decreasing Order.
	 * @param min minimum age
	 * @param max maximum age
	 */
	public static void reportB(String min, String max) 
	{
		System.out.println(":: REPORT B - RUNNING");
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement(); 	
			String avg;
			if(!isNumeric(min) || !isNumeric(max)) {
				System.out.println(":: REPORT B - ERROR - INVALID RANGE");
			}
			else {
				String query = "SELECT AVG(TOTAL) AS AVERAGE "
								+ "FROM (SELECT C.id, C.name, C.gender, C.age, SUM(A.Balance) AS TOTAL "
										+ "FROM p1.customer C, p1.account A "
										+ "WHERE C.id = A.id "
										+ "AND (C.age BETWEEN 10 AND 18) "
										+ "GROUP BY C.id, C.name, C.gender, C.age)";
				rs = stmt.executeQuery(query);
				System.out.println("AVERAGE");
				System.out.println("--------------");
				while(rs.next()) {
					avg = rs.getString("AVERAGE");
					System.out.println(avg);
				}
				System.out.println(":: REPORT B - SUCCESS");
			}		
			stmt.close();                                                                         
			con.close(); 	
		} catch (Exception e) {
			System.out.println(":: TEST - FAILED REPORT B");
			e.printStackTrace();
		}
	}
	
	/**
	 * Check Login.
	 * @param cusID customer ID
	 * @param pin Pin
	 */
	public static boolean login(String cusID, String pin) 
	{
		boolean result = true;
		System.out.println(":: CHECKING LOGIN - RUNNING");
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement(); 	
			int count = 0;
			if(!isNumeric(cusID)) {
				System.out.println("INVALID CUSTOMER ID");
				result = false;
			}
			else {
				String query = "SELECT * FROM p1.customer WHERE id = "+ cusID + " AND pin = "+pin+"";
				rs = stmt.executeQuery(query);
				String name = null;
				while(rs.next()) {
					count += 1;
					name = rs.getString("name");
				}
				if(count==0) {
					System.out.println("INVALID CUSTOMER ID");
					result = false;
				}
				else {
					System.out.println("WELCOME " + name);
				}
			}		
			stmt.close();                                                                         
			con.close(); 
		} catch (Exception e) {
			System.out.println(":: TEST - FAILED LOGIN");
			e.printStackTrace();
		}
		return result;
	}

	// Helping method
	// Check if String is number
	public static boolean isNumeric(String str) {
		if (str == null || str.length() == 0) {
			return false;
		}

		return str.chars().allMatch(Character::isDigit);
	}
	
	/**
	 * Open a new account.
	 * @param id customer id
	 * @param type type of account
	 * @param amount initial deposit amount
	 */
	public static void openAccountCustomer(String id, String type, String amount) 
	{
		System.out.println(":: OPEN ACCOUNT - RUNNING");
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement(); 				
			if(!isNumeric(amount)) {
				System.out.println(":: OPEN ACCOUNT - ERROR - INVALID AMOUNT");
			}
			else if (Integer.parseInt(amount) < 0) {
				System.out.println(":: OPEN ACCOUNT - ERROR - INVALID AMOUNT");
			}
			else if(!(type.equals("C")||type.equals("S"))) {
				System.out.println(":: OPEN ACCOUNT - ERROR - INVALID TYPE");
			}
			else {
				String query = "INSERT INTO p1.account(id,type,balance,status) VALUES("+Integer.parseInt(id)+",'"+type+"','"+Integer.parseInt(amount)+"', 'A')";	
				stmt.execute(query);
				System.out.println(":: OPEN ACCOUNT - SUCCESS");
			}
			stmt.close();                                                                         
			con.close(); 	
		} catch (Exception e) {
			System.out.println(":: TEST - FAILED OPEN NEW ACCOUNT");
			e.printStackTrace();
		}
	}

	/**
	 * Close an account.
	 * @param accNum account number
	 */
	public static void closeAccountCustomer(String cusID, String accNum) 
	{
		System.out.println(":: CLOSE ACCOUNT - RUNNING");
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement(); 	
			int count = 0;
			String query = "SELECT * FROM p1.account WHERE number = "+accNum+" AND id = "+cusID+" ";
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				count += 1;
			}
			if(count==0) {
				System.out.println("INVALID ACCOUNT");
			}
			else {
				query = "UPDATE p1.account SET balance = 0, status = 'I' WHERE number = " + accNum;	
				stmt.execute(query);
				System.out.println(":: CLOSE ACCOUNT - SUCCESS");
			}
			stmt.close();                                                                         
			con.close(); 		
		} catch (Exception e) {
			System.out.println(":: TEST - FAILED CLOSE ACCOUNT");
			e.printStackTrace();
		}
	}
	
	/**
	 * Withdraw from an account.
	 * @param accNum account number
	 * @param amount withdraw amount
	 */
	public static void withdrawCustomer(String cusID, String accNum, String amount) 
	{
		System.out.println(":: WITHDRAW - RUNNING");
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement(); 	
			int count = 0;
			if(!isNumeric(amount)) {
				System.out.println(":: WITHDRAW - ERROR - INVALID AMOUNT");
			}
			else if(Integer.parseInt(amount) < 0){
				System.out.println(":: WITHDRAW - ERROR - INVALID AMOUNT");
			}
			else if(!isNumeric(accNum)) {
				System.out.println(":: WITHDRAW - ERROR - INVALID ACCOUNT");
			}
			else {
				String bal = null;
				String query = "SELECT * FROM p1.account WHERE number = " + accNum + " AND status = 'A' AND id = "+cusID+"";
				rs = stmt.executeQuery(query);
				while(rs.next()) {
					count += 1;
					bal = rs.getString("balance");
				}
				if(count==0) {
					System.out.println(":: WITHDRAW - ERROR - INVALID ACCOUNT OR ACCOUNT IS INACTIVE");
				}
				else {
					if(Integer.parseInt(amount)  > Integer.parseInt(bal)){
						System.out.println(":: WITHDRAW - ERROR - NOT ENOUGH FUNDS");
					}
					else {
						query = "UPDATE p1.account SET balance = balance - " + amount + " WHERE number = " + accNum;	
						stmt.execute(query);
						System.out.println(":: WITHDRAW - SUCCESS");
					}
				}
			}
			stmt.close();                                                                         
			con.close(); 
		} catch (Exception e) {
			System.out.println(":: TEST - FAILED WITHDRAW");
			e.printStackTrace();
		}
	}
	
	/**
	 * Transfer amount from source account to destination account. 
	 * @param srcAccNum source account number
	 * @param destAccNum destination account number
	 * @param amount transfer amount
	 */
	public static void transferCustomer(String cusID, String srcAccNum, String destAccNum, String amount) 
	{
		System.out.println(":: TRANSFER - RUNNING");
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement(); 	
			int count = 0;
			if(!isNumeric(amount)) {
				System.out.println(":: TRANSFER - ERROR - INVALID AMOUNT");
			}
			else if(Integer.parseInt(amount) < 0){
				System.out.println(":: TRANSFER - ERROR - INVALID AMOUNT");
			}
			else if(!isNumeric(srcAccNum) || !isNumeric(destAccNum)) {
				System.out.println(":: TRANSFER - ERROR - INVALID ACCOUNT");
			}
			else {
				String bal = null;
				String query = "SELECT * FROM p1.account WHERE number = " + srcAccNum + " AND status = 'A' AND id = "+cusID+"";
				rs = stmt.executeQuery(query);
				while(rs.next()) {
					count += 1;
					bal = rs.getString("balance");
				}
				if(count==0) {
					System.out.println(":: TRANSFER - ERROR - INVALID ACCOUNT OR ACCOUNT IS INACTIVE");
				}
				else {
					if(Integer.parseInt(amount)  > Integer.parseInt(bal)){
						System.out.println(":: TRANSFER - ERROR - NOT ENOUGH FUNDS");
					}
					else {
						count = 0;	// reset count
						//check if destination account is Active or Inactive
						query = "SELECT * FROM p1.account WHERE number = " + destAccNum + " AND status = 'A'";
						rs = stmt.executeQuery(query);
						while(rs.next()) {
							count += 1;
						}
						if(count==0) {
							System.out.println(":: TRANSFER - ERROR - INVALID ACCOUNT OR ACCOUNT IS INACTIVE");
						}
						else {
							count = 0;	// reset count
							query = "UPDATE p1.account SET balance = balance - " + amount + " WHERE number = " + srcAccNum;
							stmt.execute(query);
							query = "UPDATE p1.account SET balance = balance + " + amount + " WHERE number = " + destAccNum;
							stmt.execute(query);
							System.out.println(":: TRANSFER - SUCCESS");
						}
					}
				}
			}
			stmt.close();                                                                         
			con.close(); 
		} catch (Exception e) {
			System.out.println(":: TEST - FAILED TRANSFER");
			e.printStackTrace();
		}
	}

	/**
	 * Display account summary.
	 * @param cusID customer ID
	 */
	public static void accountSummaryCustomer(String cusID) 
	{
		System.out.println(":: ACCOUNT SUMMARY - RUNNING");
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement(); 	
			int count = 0;
			if(!isNumeric(cusID)) {
				System.out.println(":: ACCOUNT SUMMARY - ERROR - INVALID CUSTOMER ID");
			}
			else {
				String query = "SELECT * FROM p1.account WHERE id = "+ cusID + " AND status = 'A'";
				rs = stmt.executeQuery(query);
				ArrayList<String> numbers = new ArrayList<String>();
				ArrayList<String> balances = new ArrayList<String>();
				while(rs.next()) {
					count += 1;
					numbers.add(rs.getString("number"));
					balances.add(rs.getString("balance"));
				}
				if(count==0) {
					System.out.println(":: ACCOUNT SUMMARY - ERROR - INVALID CUSTOMER ID");
				}
				else {
					count = 0;
					System.out.println("ACCOUNT\t\tBALANCE");
					System.out.println("----------------------------");
					for(int i = 0; i < numbers.size();i++) {						
						System.out.println(numbers.get(i) + "\t\t" + balances.get(i));
						count += Integer.parseInt(balances.get(i));
					}
					System.out.println("----------------------------");
					System.out.println("TOTAL\t\t" + count);
					System.out.println(":: ACCOUNT SUMMARY - SUCCESS");
				}
			}		
			stmt.close();                                                                         
			con.close(); 	
		} catch (Exception e) {
			System.out.println(":: TEST - FAILED ACCOUNT SUMMARY");
			e.printStackTrace();
		}
	}
}
