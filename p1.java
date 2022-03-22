import java.util.*;

public class p1 {

	public static void main(String argv[]) {
		System.out.println(":: PROGRAM START");
		Scanner scan = new  Scanner(System.in);
		boolean isEndTransaction = false;
		BankingSystem.init();
		BankingSystem.testConnection();

		System.out.println("LOGIN SESSION: ");
		System.out.println("===================");
		System.out.print("Enter ID: ");
		String c_id = scan.nextLine();
		System.out.print("Enter pin: ");
		String c_pin = scan.nextLine();
		// use id = admin and pin = admin for admin login
		if(c_id.equals("admin") && c_pin.equals("admin")) {
			adminHandler(isEndTransaction, scan);
		}
		else {	// regular customer login
			if(!BankingSystem.login(c_id, c_pin)) {
				System.out.println("INCORRECT ID OR PIN!!!");
			}
			else {
				customerHandler(isEndTransaction, scan, c_id);
			}
		}

		System.out.println();
		System.out.println(":: PROGRAM END");
	}

	/**
	 * Handle transaction with admin role
	 * @param isEndTransaction decide exit transaction or not
	 * @param scan Scanner can scan input
	 */
	public static void adminHandler(boolean isEndTransaction, Scanner scan) {
		System.out.println("Welcome admin!!!");

		while(!isEndTransaction) {
			welcomeBankingAdmin();
			System.out.print("Select your option (1,2,3,4,5,6,7,8,9):");
			String option = scan.nextLine();

			if(!isNumeric(option)) {
				System.out.println("INVALID CHOICE!!!");
			}
			else if(Integer.parseInt(option) < 1 || Integer.parseInt(option) > 9) {
				System.out.println("INVALID CHOICE!!!");
			}
			else {
				int opt = Integer.parseInt(option);
				bankingTransaction(opt, scan);
				System.out.print("Do you want to continue?(Y/N) ");
				if(scan.nextLine().equalsIgnoreCase("n")) {
					isEndTransaction = true;
				}
			}
		}
	}
	
	/**
	 * Handle transaction with customer role
	 * @param isEndTransaction decide exit transaction or not
	 * @param scan Scanner can scan input
	 */
	public static void customerHandler(boolean isEndTransaction, Scanner scan, String cusID) {
		while(!isEndTransaction) {
			welcomeBankingCustomer();
			System.out.print("Select your option (1,2,3,4,5,6):");
			String option = scan.nextLine();

			if(!isNumeric(option)) {
				System.out.println("INVALID CHOICE!!!");
			}
			else if(Integer.parseInt(option) < 1 || Integer.parseInt(option) > 6) {
				System.out.println("INVALID CHOICE!!!");
			}
			else {
				int opt = Integer.parseInt(option);
				bankingTransactionCustomer(opt, scan, cusID);
				System.out.print("Do you want to continue?(Y/N) ");
				if(scan.nextLine().equalsIgnoreCase("n")) {
					isEndTransaction = true;
				}
			}
		}
	}

	/**
	 * Detail of banking transaction
	 * @param option type  of transaction is chosen
	 * @param scan Scanner can scan input
	 */
	public static void bankingTransaction(int option, Scanner scan) {
		String name, gender, age, pin, id, type, amount, accNum, srcAccNum, destAccNum,cusID, min, max;
		switch (option) {
		case 1:			// open account
			System.out.print("Enter customer id: ");
			id = scan.nextLine();
			System.out.print("Enter type: ");
			type = scan.nextLine();
			System.out.print("Enter amount: ");
			amount = scan.nextLine();
			BankingSystem.openAccount(id, type, amount);
			break;
		case 2:			// close account
			System.out.print("Enter account number: ");
			accNum = scan.nextLine();
			BankingSystem.closeAccount(accNum);
			break;
		case 3:			// deposit
			System.out.print("Enter account number: ");
			accNum = scan.nextLine();
			System.out.print("Enter amount: ");
			amount = scan.nextLine();
			BankingSystem.deposit(accNum, amount);
			break;
		case 4:			// withdraw
			System.out.print("Enter account number: ");
			accNum = scan.nextLine();
			System.out.print("Enter amount: ");
			amount = scan.nextLine();
			BankingSystem.withdraw(accNum, amount);
			break;
		case 5:			// transfer
			System.out.print("Enter source account number: ");
			srcAccNum = scan.nextLine();
			System.out.print("Enter destination account number: ");
			destAccNum = scan.nextLine();
			System.out.print("Enter amount: ");
			amount = scan.nextLine();
			BankingSystem.transfer(srcAccNum, destAccNum, amount);
			break;
		case 6:			// account summary
			System.out.print("Enter customer id: ");
			cusID = scan.nextLine();
			BankingSystem.accountSummary(cusID);
			break;
		case 7:			// open new customer
			System.out.print("Enter customer name: ");
			name = scan.nextLine();
			System.out.print("Enter gender: ");
			gender = scan.nextLine();
			System.out.print("Enter age: ");
			age = scan.nextLine();
			System.out.print("Enter pin: ");
			pin = scan.nextLine();
			BankingSystem.newCustomer(name, gender, age, pin);
			break;
		case 8:			// report A
			BankingSystem.reportA();
			break;
		case 9:			// report B
			System.out.print("Enter minimum age: ");
			min = scan.nextLine();
			System.out.print("Enter maximum age: ");
			max = scan.nextLine();
			BankingSystem.reportB(min, max);
			break;
		default:
			System.out.println("INVALID OPTION!!!");
			break;
		}
	}
	
	/**
	 * Detail of banking transaction
	 * @param option type  of transaction is chosen
	 * @param scan Scanner can scan input
	 */
	public static void bankingTransactionCustomer(int option, Scanner scan, String cusID) {
		String type, amount, accNum, srcAccNum, destAccNum;
		switch (option) {
		case 1:			// open account
			System.out.print("Enter type: ");
			type = scan.nextLine();
			System.out.print("Enter amount: ");
			amount = scan.nextLine();
			BankingSystem.openAccountCustomer(cusID, type, amount);
			break;
		case 2:			// close account
			System.out.print("Enter account number: ");
			accNum = scan.nextLine();
			BankingSystem.closeAccountCustomer(cusID, accNum);
			break;
		case 3:			// deposit
			System.out.print("Enter account number: ");
			accNum = scan.nextLine();
			System.out.print("Enter amount: ");
			amount = scan.nextLine();
			BankingSystem.deposit(accNum, amount);
			break;
		case 4:			// withdraw
			System.out.print("Enter account number: ");
			accNum = scan.nextLine();
			System.out.print("Enter amount: ");
			amount = scan.nextLine();
			BankingSystem.withdrawCustomer(cusID, accNum, amount);
			break;
		case 5:			// transfer
			System.out.print("Enter source account number: ");
			srcAccNum = scan.nextLine();
			System.out.print("Enter destination account number: ");
			destAccNum = scan.nextLine();
			System.out.print("Enter amount: ");
			amount = scan.nextLine();
			BankingSystem.transferCustomer(cusID, srcAccNum, destAccNum, amount);
			break;
		case 6:			// account summary
			BankingSystem.accountSummaryCustomer(cusID);
			break;
		default:
			System.out.println("INVALID OPTION!!!");
			break;
		}
	}

	// help method display welcome instruction for admin
	public static void welcomeBankingAdmin() {
		System.out.println("**************************************************");
		System.out.println("**********Welcome to banking application**********");
		System.out.println("**************************************************");
		System.out.println("*****1) Open New Account		**********");
		System.out.println("*****2) Close Account			**********");
		System.out.println("*****3) Deposit           		**********");
		System.out.println("*****4) Withdraw			**********");
		System.out.println("*****5) Transfer           		**********");
		System.out.println("*****6) AccountSummary			**********");
		System.out.println("*****7) Create New Customer		**********");
		System.out.println("*****8) Report A           		**********");
		System.out.println("*****9) Report B           		**********");
		System.out.println("**************************************************");
	}

	// help method display welcome instruction
	public static void welcomeBankingCustomer() {
		System.out.println("**************************************************");
		System.out.println("**********Welcome to banking application**********");
		System.out.println("**************************************************");
		System.out.println("*****1) Open New Account		**********");
		System.out.println("*****2) Close Account			**********");
		System.out.println("*****3) Deposit           		**********");
		System.out.println("*****4) Withdraw			**********");
		System.out.println("*****5) Transfer           		**********");
		System.out.println("*****6) AccountSummary			**********");
		System.out.println("**************************************************");
	}

	// Helping method
	// Check if String is number
	public static boolean isNumeric(String str) {
		if (str == null || str.length() == 0) {
			return false;
		}
		return str.chars().allMatch(Character::isDigit);
	}
}