package model;

public class Loan {
	private String principal;
	private String interest;
	private String period;
	private static String errorMsg = "";
	
	public Loan(){
		principal = "0";
		interest = "0";
		period = "0";
	}
	
	public Loan(String p, String a, String i) {
		principal = p;
		interest = i;
		period = a;
	}
	
	
	public double computePayment(String p, String a, String i, String g, String gp, String fi) throws Exception{
		principal = p;
		interest = i;
		period = a;
		if (isPositiveNumber(p)) {
			if (isPositiveNumber(i) && Double.parseDouble(i) < 100) {
				if (isPositiveNumber(a)) {
					setErrorMsg("");
				}else {
					setErrorMsg("Period must be greater than 0!");
				}
			}else {
				setErrorMsg("Interest must be greater than 0 and less than 100!");
			}
		}else {
			setErrorMsg("Principal must be greater than 0!");
		}
		
		
		if(errorMsg == "") {
			double grace;
			if (g == null) {
				grace = 0;
			}else {
				grace = 1;
			}
			
			Double gi = computeGraceInterest(p,gp,i,fi);
			
			double principalValue = Double.parseDouble(p) + (grace * gi);
			
			double actualInterest = (Double.parseDouble(i) + Double.parseDouble(fi)) / 100;
			double powrr = Math.pow((1 + (actualInterest/12.0)), -Double.parseDouble(a));
			return ((actualInterest/12.0) * (principalValue/(1 - powrr)));
		}else {
			return 0;
		}
	}
	
	public double computeGraceInterest(String p, String gp, String i,String fi) throws Exception{
		if(errorMsg == "") {
			double interest = Double.parseDouble(i)/100;
			double fixedInterest = Double.parseDouble(fi)/100;
			return (Double.parseDouble(p) * ((interest + fixedInterest)/12) * Double.parseDouble(gp));
		}else {
			return 0;
		}
		
	}
	
	private static void setErrorMsg(String s) {
		errorMsg=s;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}
	
	
	public static boolean isPositiveNumber(String s) {
		try {
			Double a = Double.parseDouble(s);
			if(a > 0) {
				return true;
			}else {
				return false;
			}
		}catch(NumberFormatException e){
			return false;
		}
	}
	
	public static void main(String[] args) throws Exception {
		String p = "5424";
		String i = "20";
		String a = "0";
		
		String g = "true";
		String gp = "6";
		String fi = "5";
		
		Loan test1 = new Loan();
		
		System.out.println(test1.computePayment(p,a,i,g, gp,fi));
		System.out.println(test1.computeGraceInterest(p,gp,i,fi));
		
		System.out.println(test1.getErrorMsg());
	}
}
