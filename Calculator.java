import java.util.Scanner;
import java.lang.Math;

public class Calculator {
	static Scanner scan = new Scanner(System.in);
	
	public static void main (String args[]) {
		
		int input = 1;
		
		while (input != 0) {
			displayMenu();
			input = scan.nextInt();
			
			switch(input) {
				case 1:
					System.out.println("Select option: ");
					System.out.println("1) Calculate Federal Tax Liability");
					System.out.println("9) Go back");
					input = scan.nextInt();
					switch(input) {
						case 1:
							calculateTaxLiability();
							break;
						case 9:
							break;
					}
				break;
					
				case 2:
					System.out.println("Select option: ");
					System.out.println("1) Future Value");
					System.out.println("2) Present Value");
					System.out.println("3) Retirement savings");
					System.out.println("9) Go back");
					input = scan.nextInt();
					switch(input) {
					case 1:
						calculateFutureValue();
						break;
					case 2:
						calculatePresentValue();
						break;
					case 3:
						retirementSavings();
						break;
					case 9:
						break;
				}
				break;
					
				case 3:
					System.out.println("Select option: ");
					System.out.println("1) Stock Value: AYTM/ROI");
					System.out.println("2) Bond Value: Annual Returns");
					System.out.println("9) Go back");
					input = scan.nextInt();
					switch(input) {
					case 1:
						calculateStockValue();
						break;
					case 2:
						calculateBondValue();
						break;
					case 9:
						break;
					}
				break;
					
				case 4:
					System.out.println("Select option: ");
					System.out.println("1) Mortgage payments");
					System.out.println("2) How much mortgage can you afford?");
					System.out.println("9) Go back");
					input = scan.nextInt();
					switch(input) {
					case 1:
						calculateMortgagePayments();
						break;
					case 2:
						calculateMortgagePurchase();
						break;
					case 9:
						break;
					}
				break;
					
				case 5:
					System.out.println("Select option: ");
					System.out.println("1) Installment Loan Disclosure Statement");
					System.out.println("9) Go back");
					input = scan.nextInt();
					switch(input) {
					case 1:
						calculateLDS();
						break;
					case 9:
						break;
					}
				break;
					
				case 0: 
					System.exit(0);
					
				default: 
					System.out.println("Invalid input.");
			}
			
		}
		
		scan.close();
	}
	
	private static void calculateTaxLiability() {
		double final_deduction = 0, standard_deductions = 0, itemized_deductions = 0,
				taxable_income = 0, tax_liability = 0,
				tax_credit = 0, LTCG = 0, paid_tax = 0;
		boolean done = false;
		
		//filing status
		System.out.println("How are you filing? \n1) single \n2) married \n3) head of household");
		int status = scan.nextInt();
		
		if (status == 1) 
			standard_deductions = 12000;
		else if (status == 2)
			standard_deductions = 24000;
		else
			standard_deductions = 18000;
		
		//income
		System.out.println("Input total gross yearly income. If married, the combined income. Do not include non-taxable income. \n"
				+ "Do include short-term capital gains. Subtract any deductions from your gross income.");
		double income = scan.nextDouble();
		
		//determine deductions
		while(!done) {
			System.out.println("Which type of deductions are you using? \n1) standard \n2) itemized \n3) not sure");
			double deductions_input = scan.nextDouble();
		
			if (deductions_input == 1) {
				final_deduction = standard_deductions;
				done = true;
			}
		
			else if (deductions_input == 3) {
				System.out.println("The standard deduction is a government-set fixed amount that can"
					+ " be deducted from your taxable income before tax calculations. \nAn itemized deduction"
					+ " is a list of eligible expenses that can be deducted such as home mortgage interest and"
					+ " medical expenses. \nNot all individuals have"
					+ " much in itemized deductions, so you want to use whichever amount is greater (which the"
					+ " system automatically does). \nIf you don't know your itemized deductions, use the standard. ");
				pressEnterToContinue();
			}

			else {
				System.out.println("Input amount in itemized deductions: ");
				itemized_deductions = scan.nextDouble();
				
				if (itemized_deductions < standard_deductions)
					final_deduction = standard_deductions;
				else
					final_deduction = itemized_deductions;
				
				done = true;
			}
		
		}
		
		//determine tax liability
		taxable_income = income - final_deduction;
		
		if (taxable_income < 0)
			taxable_income = 0;
		
		tax_liability = taxTable(status, taxable_income);
		
		//tax credits?, assumes they are refundable
		System.out.println("Input amount claimed in tax credits, 0 if none. If you have children "
				+ "under age 17, claim $2000 for each child:");
		tax_credit = scan.nextDouble();
		
		tax_liability -= tax_credit;
		
		//long-term capital gains? (15% flat tax rate)
		System.out.println("Input income received from long-term capital gains and dividends, 0 if none:");
		LTCG = scan.nextDouble();
		
		tax_liability += 0.15 * LTCG;
		
		//amount paid in taxes already
		System.out.println("How much have you paid in federal tax already?");
		paid_tax = scan.nextDouble();
		
		tax_liability -= paid_tax;
		
		//result
		System.out.printf("Adjusted Gross Income          : %.2f\n", income);
		
		if (standard_deductions >= itemized_deductions)
			System.out.printf("Standard Deduction (-)         : %.2f\n", final_deduction);
		else 
			System.out.printf("Itemized Deduction (-)         : %.2f\n", final_deduction);
		
		System.out.printf("Taxable Income                 : %.2f\n", taxable_income);
		System.out.printf("Preliminary Tax Liability      : %.2f\n", (tax_liability - 0.15 * LTCG + tax_credit + paid_tax) );
		System.out.printf("Tax Credits (-)                : %.2f\n", tax_credit);
		System.out.printf("Long Term Capital Gains (+15%%) : %.2f\n", LTCG);
		System.out.printf("Final Tax Liability            : %.2f\n",(tax_liability + paid_tax) );
		System.out.printf("Already paid in federal taxes  : %.2f\n", paid_tax);
		
		if (tax_liability > 0)
			System.out.printf("Taxes due       : %.2f\n", tax_liability);
		else
			System.out.printf("Tax Refund due  : %.2f\n", (-1 * tax_liability) );
		pressEnterToContinue();
	}
	
	private static void calculateFutureValue() {
		double principal = 0, annuity = 0, int_rate = 0, factor1, factor2;
		int years;
		
		System.out.println("Input starting principal amount: ");
		principal = scan.nextDouble();
		
		System.out.println("Input yearly contribution to account, 0 if none: ");
		annuity = scan.nextDouble();
		
		System.out.println("How many years will this grow?");
		years = scan.nextInt();
		
		System.out.println("What interest rate are you expecting on your investment? Input as a decimal.\n"
				+ " For example, input 8% as 0.08.");
		int_rate = scan.nextDouble();
		
		factor1 = D_1(years, int_rate);
		factor2 = D_2(years, int_rate);
		
		System.out.printf("Starting amount         : %.2f\n", principal);
		System.out.printf("Annual contribution     : %.2f\n", annuity);
		System.out.printf("Total value in %d years : %.2f\n", years, principal*factor1 + annuity*factor2);
		pressEnterToContinue();
	}
	
	private static void calculatePresentValue() {
		double amount, int_rate, factor1, factor2;
		int type, years, num_payments = 1;
		
		System.out.println("Are you expecting a lump sum or an annuity? \n1) Lump sum \n2) Annuity");
		type = scan.nextInt();
		
		System.out.println("What amount are you expecting? If it's an annuity, input one annuity payment.");
		amount = scan.nextDouble();
		
		System.out.println("How many years until you receive the payment? For an annuity, input the number"
				+ " of years until your first payment.");
		years = scan.nextInt();
		
		if (type == 2) {
			years -= 1;
			System.out.println("How many payments are you receiving?");
			num_payments = scan.nextInt();
		}
		
		System.out.println("What interest rate would you expect from an investment today? Input as a decimal.\n"
				+ "For example, input 8% as 0.08?");
		int_rate = scan.nextDouble();
		
		factor1 = D_3(years, int_rate);
		factor2 = D_4(num_payments, int_rate);
		
		if (type == 1) {
			System.out.printf("Present Value: %.2f\n", amount*factor1);
		}
		if (type == 2) {
			System.out.printf("Present Value: %.2f\n", amount*factor2*factor1);
		}
		pressEnterToContinue();
	}
	
	private static void retirementSavings() {
		double retirement_goal = 0, int_rate = 0, factor1, factor2;
		int years = 0;
		
		System.out.println("How much are you looking to save for retirement?");
		retirement_goal = scan.nextDouble();
		
		System.out.println("How many years until you retire?");
		years = scan.nextInt();
		
		System.out.println("What interest rate are you expecting on your investment(s)? Input as a decimal.\n"
				+ "For example, input 8% as 0.08.");
		int_rate = scan.nextDouble();
		
		factor1 = D_1(years, int_rate);
		factor2 = D_2(years, int_rate);
		
		System.out.printf("Annual payment required : %.2f\n", retirement_goal/factor2);
		System.out.printf("Lump sum required       : %.2f\n", retirement_goal/factor1);
		pressEnterToContinue();
	}
	
	private static void calculateStockValue() {
		double price1, price2, num_shares, margin, avg_div, percent_gain,
		AYTM, ROI, dollar_profit, margin_call;
		int years;
		
		System.out.println("At what price are you purchasing stock?");
			price1 = scan.nextDouble();
			
		System.out.println("How many shares are you buying?");
			num_shares = scan.nextDouble();
			
		System.out.println("What % margin are you using? Input as decimal. For example, input 50%"
				+ " as 0.5. If you aren't using margin, input 1.");
			margin = scan.nextDouble();
			
		System.out.println("What average dividend yield are you expecting annually per share?");
			avg_div = scan.nextDouble();
			
		System.out.println("How long are you holding the stock (in years)?");
			years = scan.nextInt();
			
		System.out.println("At what price do you expect the stock will be then?");
			price2 = scan.nextDouble();
			
		System.out.println("What % gain do you desire to earn on this investment annually? Input as decimal."
				+ "\nFor example, input 8% as 0.08:");
			percent_gain = scan.nextDouble();
			
		//calculations
		//all margin calculations ignore interest
		//margin % = the % financed by the buyer. A $2000 purchase on 60% margin = $800 loan
		dollar_profit = num_shares * (price2 - price1) + avg_div * num_shares * years;
		AYTM = (avg_div + (price2 - price1)/years) / ((price2 + 2*price1) * (margin)/3);
		ROI = dollar_profit / ((num_shares * price1) * (margin));
		margin_call = (num_shares * price1 * (1 - margin)) / 0.75 / num_shares;
		
		System.out.printf("Amount borrowed on margin   : %.2f\n", num_shares*price1*(1-margin));
		System.out.printf("Dollar Profit               : %.2f\n", dollar_profit);
		System.out.printf("Margin Call price           : %.2f\n", margin_call);
		System.out.printf("Return on Investment        : %.2f%%\n", ROI*100);
		System.out.printf("Approx. Yield to Maturity   : %.2f%%\n", AYTM*100);
		System.out.printf("Desired annual yield        : %.2f%%\n", percent_gain*100);
		if (percent_gain < AYTM) {
			System.out.printf("Should consider this stock? : Yes");
			if (margin < 1) {
				System.out.print("*\n*Does not consider interest on margin.\n");
			}
			else
				System.out.println();
		}
		else
			System.out.printf("Should consider this stock? : No\n");
		pressEnterToContinue();
	}
	
	private static void calculateBondValue() {
		double purchase_price, max_purchase, par_value, coupon_rate, desired_gain, dividend, AYTM;
		int years;
		
		System.out.println("At what price are you purchasing the bond?");
			purchase_price = scan.nextDouble();
		
		System.out.println("What is the par value of the bond?");
			par_value = scan.nextDouble();
			
		System.out.println("What coupon rate does the bond have? Input as a decimal."
				+ " For example, input 5% as 0.05:");
			coupon_rate = scan.nextDouble();
			
		System.out.println("How many years until maturity?");
			years = scan.nextInt();
			
		System.out.println("What % gain do you desire to earn annually? Input as a decimal.");
			desired_gain = scan.nextDouble();
			
		//calculations
		dividend = par_value * coupon_rate;
		AYTM = (dividend + (par_value - purchase_price)/years) / ((par_value + 2*purchase_price)/3);
		max_purchase = dividend * D_4(years, desired_gain) + par_value * D_3(years, desired_gain);
		
		System.out.printf("Approx. Yield to Maturity  : %.2f%%\n", AYTM*100);
		System.out.printf("Desired annual yield       : %.2f%%\n", desired_gain*100);
		System.out.printf("Maximum purchase price     : %.2f\n", max_purchase);
		System.out.printf("Current price              : %.2f\n", purchase_price);
		if (AYTM >= desired_gain || purchase_price < max_purchase)
			System.out.printf("Should consider this bond? : Yes\n");
		else
			System.out.printf("Should consider this bond? : No\n");
		pressEnterToContinue();
	}
	
	private static void calculateMortgagePayments() {
		double home_value, down_percent, mort_int, P, monthly_payment, int_cost;
		int years;
		
		System.out.println("Input total price of the house:");
			home_value = scan.nextDouble();
			
		System.out.println("What % are you putting as a down payment? Input as decimal.");
			down_percent = scan.nextDouble();
			
		System.out.println("What % interest rate is on the mortgage? Input as decimal.");
			mort_int = scan.nextDouble();
			
		System.out.println("How many years is the mortgage?");
			years = scan.nextInt();
			
		//calculations
		P = home_value * (1 - down_percent); //principal loan
		monthly_payment = M_1(years, mort_int, P);
		int_cost = monthly_payment * (years * 12) - P;
			
		System.out.printf("Monthly payment     : %.2f\n", monthly_payment);
		System.out.printf("Total interest cost : %.2f\n", int_cost);
		pressEnterToContinue();
	}
	
	private static void calculateMortgagePurchase() {
		double monthly_income, other_loans = 0, monthly_payment, int_rate, percent_down
		, mortgage_afford, home_afford;
		int input;
		
		System.out.println("Input your gross annual income: ");
			monthly_income = scan.nextDouble()/12;
			
		System.out.println("Do you have any other outstanding loans? \n1) yes \n2) no");
			input = scan.nextInt();
		
		if (input == 1) {
			System.out.println("Input total amount paid monthly for all other outstanding loans:");
			other_loans = scan.nextDouble();
		}
		
		System.out.println("What % interest rate will be on the mortgage? Input as decimal.");
			int_rate = scan.nextDouble();
		
		System.out.println("What % will you put down on the house? Input as decimal.");
			percent_down = scan.nextDouble();
			
		//calculations
		if (input == 1)
			monthly_payment = 0.33*monthly_income - other_loans;
		else
			monthly_payment = 0.25*monthly_income;
		mortgage_afford = M_2(int_rate, monthly_payment);
		home_afford = mortgage_afford / (1 - percent_down);
		
		System.out.println("The recommended maximum you can afford:\n");
		System.out.printf("Monthly payment  : %.2f\n", monthly_payment);
		System.out.printf("Total Mortgage   : %.2f\n", mortgage_afford);
		System.out.printf("Total home value : %.2f\n", home_afford);
		pressEnterToContinue();
	}
	
	private static void calculateLDS() {
		double loan, int_rate, monthly_payment, tv_payments, finance_cost;
		int months;
		
		System.out.println("Input loan amount: ");
			loan = scan.nextDouble();
			
		System.out.println("Input interest rate on the loan (APR). Input as decimal. ");
			int_rate = scan.nextDouble();
			
		System.out.println("Input term length of loan in months: ");
			months = scan.nextInt();
			
		//calculations
		monthly_payment = M_1(months/12, int_rate, loan);
		tv_payments = monthly_payment * months;
		finance_cost = tv_payments - loan;
		
		System.out.printf("Annual %% rate (APR)         : %.2f%%\n", int_rate*100);
		System.out.printf("Monthly payment             : %.2f\n", monthly_payment);
		System.out.printf("Total value of all payments : %.2f\n", tv_payments);
		System.out.printf("Actual amount borrowed      : %.2f\n", loan);
		System.out.printf("Total finance cost          : %.2f\n", finance_cost);
		pressEnterToContinue();
	}
	
	private static void displayMenu() {
		System.out.println("----------------------------");
		System.out.println("1) Taxes");
		System.out.println("2) TVOM");
		System.out.println("3) Stocks/Bonds");
		System.out.println("4) Housing");
		System.out.println("5) Credit");
		System.out.println("0) Exit");
		System.out.println("----------------------------");
	}
	
	//calculate tax liability using 2018 tax table
	private static double taxTable(int status, double taxable) {
		
		if (taxable <= 0)
			return 0;
		
		//single tax table
		if (status == 1) {
			if (taxable > 500000) {
				return 150689.50 + 0.37 * (taxable - 500000);
			}
			else if (taxable > 200000) {
				return 45689.50 + 0.35 * (taxable - 200000);
			}
			else if (taxable > 157500) {
				return 32089.50 + 0.32 * (taxable - 157500);
			}
			else if (taxable > 82500) {
				return 14089.50 + 0.24 * (taxable - 82500);
			}
			else if (taxable > 38700) {
				return 4453.50 + 0.22 * (taxable - 38700);
			}
			else if (taxable > 9525) {
				return 952.50 + 0.12 * (taxable - 9525);
			}
			else
				return 0.1 * taxable;
		}
		
		//married tax table
		else if (status == 2) {
			if (taxable > 600000) {
				return 161379 + 0.37 * (taxable - 600000);
			}
			else if (taxable > 400000) {
				return 91379 + 0.35 * (taxable - 400000);
			}
			else if (taxable > 315000) {
				return 64379 + 0.32 * (taxable - 315000);
			}
			else if (taxable > 165000) {
				return 28379 + 0.24 * (taxable - 165000);
			}
			else if (taxable > 77400) {
				return 8907 + 0.22 * (taxable - 77400);
			}
			else if (taxable > 19050) {
				return 1905 + 0.12 * (taxable - 19050);
			}
			else
				return 0.1 * taxable;
		}
		
		//HOH tax table
		else {
			if (taxable > 500000) {
				return 149298 + 0.37 * (taxable - 500000);
			}
			else if (taxable > 200000) {
				return 44298 + 0.35 * (taxable - 200000);
			}
			else if (taxable > 157500) {
				return 30698 + 0.32 * (taxable - 157500);
			}
			else if (taxable > 82500) {
				return 12698 + 0.24 * (taxable - 82500);
			}
			else if (taxable > 51800) {
				return 5944 + 0.22 * (taxable - 51800);
			}
			else if (taxable > 13600) {
				return 1360 + 0.12 * (taxable - 13600);
			}
			else
				return 0.1 * taxable;
		}
	}
	
	private static double D_1(int years, double rate) {
		return Math.pow(1 + rate, years);
	}
	
	private static double D_2(int years, double rate) {
		return (Math.pow(1 + rate, years) - 1) / rate;
	}
	
	private static double D_3(int years, double rate) {
		return 1 / Math.pow(1 + rate, years);
	}
	
	private static double D_4(int years, double rate) {
		return (Math.pow(1 + rate, years) - 1) / (rate * Math.pow(1 + rate, years));
	}
	
	//n = years, r = annual rate, P = principal loan
	private static double M_1(int n, double r, double P) {
		n *= 12;
		r /= 12;
		return P * ((r * Math.pow(1 + r, n)) / (Math.pow(1 + r, n) - 1));
	}
	
	//calculate how much mortgage that can be afforded
	//r = annual rate, m = monthly payment, mortgage is assumed to be 30 years
	private static double M_2(double r, double m) {
		r /= 12;
		int n = 360;
		return m * (Math.pow(1 + r, n) - 1) / (r * Math.pow(1 + r, n));
	}
	
	private static void pressEnterToContinue()
	{ 
	        System.out.println("Press Enter key to continue...");
	        try
	        {
	            System.in.read();
	        }  
	        catch(Exception e)
	        {}  
	}
}
