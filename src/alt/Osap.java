package alt;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Loan;

/**
 * Servlet implementation class Osap
 */
@WebServlet({"/Osap", "/Osap/*"})
public class Osap extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	private static final String COMM = "comm";
	private static final String AJAX = "ajax";
	
	private static String principal;
	private static String interest;
	private static String period;
	private static String appName;
	private static String fixedInterest;
	private static String gracePeriod;
	private HttpSession session;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Osap() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init(ServletConfig config) throws ServletException{
		super.init(config);
		ServletContext context = getServletContext();
		
		appName = context.getInitParameter("applicationName");
		fixedInterest = context.getInitParameter("fixedInterest");
		gracePeriod = context.getInitParameter("gracePeriod");
		
		context.setAttribute("applicationName", appName);
		context.setAttribute("mLoan", new Loan());
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//System.out.println("Hello, Got a GET request from Osap!");
		String resultPage = "/Results.jspx";
		String target = "/UI.jspx";

		response.setContentType("text/plain");
		ServletContext context = this.getServletContext();
		
		session = request.getSession();

	
		//used for ajax button to be compatible to prev implementation
		String grace = request.getParameter("grace");
		if (grace != null && grace.equals("false")) {
			grace = null;
		}

		
		//set values for principal
		setPrincipal(request);
		setInterest(request);
		setPeriod(request);
		
		
		Loan mLoan = (Loan) context.getAttribute("mLoan");
	
		double monthlyPayment = 0;
		try {
			monthlyPayment = mLoan.computePayment(principal, period, interest, grace, gracePeriod, fixedInterest);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		double interestInGrace = 0;
		try {
			interestInGrace = mLoan.computeGraceInterest(principal, gracePeriod, interest, fixedInterest);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String graceInterestString ="";
		String monthlyPaymentString = String.valueOf(monthlyPayment);
		
		
		String errorMsg = mLoan.getErrorMsg();
		session.setAttribute("error", errorMsg);
		
		
		if (request.getParameter("calculate") != null && errorMsg == "") {
			session.setAttribute("grace", grace);
			if (session.getAttribute("grace") == null) {
				interestInGrace = 0;
				graceInterestString = String.valueOf(interestInGrace);
			}else {
				graceInterestString = String.valueOf(interestInGrace);
			}
		}
		
		
		session.setAttribute("graceInterest", graceInterestString);
		session.setAttribute("payment", monthlyPaymentString);
		session.setAttribute("fixedInterest", fixedInterest);
		

		
		if (request.getParameter(COMM) != null && request.getParameter(COMM).equals(AJAX)) { //when your using the ajax button
			if (errorMsg != "") { //error checking for ajax button
				response.getWriter().append(errorMsg);
			}else {//Successful ajax button press with valid inputs
				session.setAttribute("principal", principal);
				session.setAttribute("interest", interest);
				session.setAttribute("period", period);
				
				//formatted ajax button output of principal and grace interest
				NumberFormat nfCa = NumberFormat.getCurrencyInstance(Locale.CANADA);
				String interestinGraceStringFormatted = nfCa.format(interestInGrace);
				String monthlyPaymentStringFormatted = nfCa.format(monthlyPayment);
				response.getWriter().append("Grace Period Interest: " + interestinGraceStringFormatted  + "<BR />");
				response.getWriter().append("Monthly payment: " + monthlyPaymentStringFormatted);
			}
		}else if (request.getParameter("calculate") == null) { //when your not calculating
			//to set default values for principal, interest & period upon launch
			if (session.getAttribute("principal") == null && session.getAttribute("interest") == null && session.getAttribute("period") == null) {
				session.setAttribute("principal", principal);
				session.setAttribute("interest", interest);
				session.setAttribute("period", period);
			}
			request.getRequestDispatcher(target).forward(request, response); //forward back to original page
		}else if(errorMsg != ""){ //when the inputs are not valid
			session.setAttribute("principal", principal);
			session.setAttribute("interest", interest);
			session.setAttribute("period", period);
			request.getRequestDispatcher(target).forward(request, response);
		}else { //input is valid && never used ajax button
			session.setAttribute("principal", principal);
			session.setAttribute("interest", interest);
			session.setAttribute("period", period);
			request.getRequestDispatcher(resultPage).forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		
	}
	

	public static void setPrincipal(HttpServletRequest request) {
		ServletContext context = request.getServletContext();
		String p = request.getParameter("principal");
		if(p == null) {
			principal = context.getInitParameter("principal");
		}else {
			principal = p;
		}
	}
	
	public static void setInterest(HttpServletRequest request) {
		ServletContext context = request.getServletContext();
		String i = request.getParameter("interest");
		if(i == null) {
			interest = context.getInitParameter("interest");
		}else {
			interest = i;
		}
	}
	
	public static void setPeriod(HttpServletRequest request) {
		ServletContext context = request.getServletContext();
		String a = request.getParameter("period");
		if(a == null) {
			period = context.getInitParameter("period");
		}else {
			period = a;
		}
	}
	
}
