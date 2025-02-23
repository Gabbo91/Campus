package controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.implementations.CorsoDao;
import dao.implementations.DataAppelloDao;
import dao.implementations.FacoltaDao;
import dao.implementations.ProfessoreDao;
import model.Account;
import model.DataAppello;
import model.Professore;
import model.Utente;

/**
 * Servlet implementation class AppelloInserimentoServlet
 */
@WebServlet("/AppelloInserimento")
public class AppelloInserimentoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AppelloInserimentoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ProfessoreDao pd = new ProfessoreDao();
		
		Utente u = ((Account) request.getSession().getAttribute("account")).getUtente();
		
		request.setAttribute("cattedre", pd.getByIdWithCorsi(u.getId()).getProfessoriCorsi());
		
		request.getRequestDispatcher("dataAppelloForm.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//instanziazione
		DataAppelloDao dad = new DataAppelloDao();
		FacoltaDao fDao = new FacoltaDao();
		CorsoDao cDao = new CorsoDao();
		
		DataAppello da = new DataAppello();
		
		
		Professore pf = (Professore) ((Account) request.getSession().getAttribute("account")).getUtente();
		da.setProfessore(pf);
		
		//gestisco il tipo Date
		String param = request.getParameter("data");
		Date dataDaInserire = null;
		try {
			dataDaInserire = new SimpleDateFormat("yy-MM-dd").parse(param);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String[] values = request.getParameter("cattedra").split("-");
		
		da.setFacolta(fDao.getById(Integer.parseInt(values[0])));
		da.setCorso(cDao.getById(Integer.parseInt(values[1])));
		
		//setting della data e reindirizzamento
		if(dataDaInserire != null) {
			da.setDataAppello(dataDaInserire);
			dad.inserimento(da);
			response.sendRedirect("index.jsp");
		}else
		{
			response.sendRedirect("dataAppelloForm.jsp");
		}
	}

}
