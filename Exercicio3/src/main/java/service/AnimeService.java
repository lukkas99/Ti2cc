package service;

import java.util.Scanner;
import java.time.LocalDate;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import dao.AnimeDAO;
import model.Anime;
import spark.Request;
import spark.Response;


public class AnimeService {

	private AnimeDAO animeDAO = new AnimeDAO();
	private String form;
	private final int FORM_INSERT = 1;
	private final int FORM_DETAIL = 2;
	private final int FORM_UPDATE = 3;
	private final int FORM_ORDERBY_ID = 1;
	private final int FORM_ORDERBY_TITULO = 2;
	private final int FORM_ORDERBY_NOTA = 3;
	
	
	public AnimeService() {
		makeForm();
	}

	
	public void makeForm() {
		makeForm(FORM_INSERT, new Anime(), FORM_ORDERBY_TITULO);
	}

	
	public void makeForm(int orderBy) {
		makeForm(FORM_INSERT, new Anime(), orderBy);
	}

	
	public void makeForm(int tipo, Anime anime, int orderBy) {
		String nomeArquivo = "form.html";
		form = "";
		try{
			Scanner entrada = new Scanner(new File(nomeArquivo));
		    while(entrada.hasNext()){
		    	form += (entrada.nextLine() + "\n");
		    }
		    entrada.close();
		}  catch (Exception e) { System.out.println(e.getMessage()); }
		
		String umanime = "";
		if(tipo != FORM_INSERT) {
			umanime += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umanime += "\t\t<tr>";
			umanime += "\t\t\t<td align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;<a href=\"/anime/list/1\">Novo anime</a></b></font></td>";
			umanime += "\t\t</tr>";
			umanime += "\t</table>";
			umanime += "\t<br>";			
		}
		
		if(tipo == FORM_INSERT || tipo == FORM_UPDATE) {
			String action = "/anime/";
			String name, titulo, buttonLabel;
			if (tipo == FORM_INSERT){
				action += "insert";
				name = "Inserir anime";
				titulo = "Digite aqui o titulo do desenho!";
				buttonLabel = "Inserir";
			} else {
				action += "update/" + anime.getID();
				name = "Atualizar anime (ID " + anime.getID() + ")";
				titulo = anime.getTitulo();
				buttonLabel = "Atualizar";
			}
			umanime += "\t<form class=\"form--register\" action=\"" + action + "\" method=\"post\" id=\"form-add\">";
			umanime += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umanime += "\t\t<tr>";
			umanime += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;" + name + "</b></font></td>";
			umanime += "\t\t</tr>";
			umanime += "\t\t<tr>";
			umanime += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
			umanime += "\t\t</tr>";
			umanime += "\t\t<tr>";
			umanime += "\t\t\t<td>&nbsp;Descrição: <input class=\"input--register\" type=\"text\" name=\"titulo\" value=\""+ titulo +"\"></td>";
			umanime += "\t\t\t<td>Nota: <input class=\"input--register\" type=\"text\" name=\"quantidade\" value=\""+ anime.getNota() +"\"></td>";
			umanime += "\t\t</tr>";
			umanime += "\t\t<tr>";
			umanime += "\t\t\t<td>&nbsp;Data de fabricação: <input class=\"input--register\" type=\"text\" name=\"dataFabricacao\" value=\""+ anime.getDataAtual().toString() + "\"></td>";
			umanime += "\t\t\t<td align=\"center\"><input type=\"submit\" value=\""+ buttonLabel +"\" class=\"input--main__style input--button\"></td>";
			umanime += "\t\t</tr>";
			umanime += "\t</table>";
			umanime += "\t</form>";		
		} else if (tipo == FORM_DETAIL){
			umanime += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umanime += "\t\t<tr>";
			umanime += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Detalhar anime (ID " + anime.getID() + ")</b></font></td>";
			umanime += "\t\t</tr>";
			umanime += "\t\t<tr>";
			umanime += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
			umanime += "\t\t</tr>";
			umanime += "\t\t<tr>";
			umanime += "\t\t\t<td>&nbsp;Titulo: "+ anime.getTitulo() +"</td>";
			umanime += "\t\t\t<td>Nota: "+ anime.getNota() +"</td>";
			umanime += "\t\t</tr>";
			umanime += "\t\t<tr>";
			umanime += "\t\t\t<td>&nbsp;Data de fabricação: "+ anime.getDataAtual().toString() + "</td>";
			umanime += "\t\t\t<td>&nbsp;</td>";
			umanime += "\t\t</tr>";
			umanime += "\t</table>";		
		} else {
			System.out.println("ERRO! Tipo não identificado " + tipo);
		}
		form = form.replaceFirst("<UM-anime>", umanime);
		
		String list = new String("<table width=\"80%\" align=\"center\" bgcolor=\"#f3f3f3\">");
		list += "\n<tr><td colspan=\"6\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Relação de animes</b></font></td></tr>\n" +
				"\n<tr><td colspan=\"6\">&nbsp;</td></tr>\n" +
    			"\n<tr>\n" + 
        		"\t<td><a href=\"/anime/list/" + FORM_ORDERBY_ID + "\"><b>ID</b></a></td>\n" +
        		"\t<td><a href=\"/anime/list/" + FORM_ORDERBY_TITULO + "\"><b>Descrição</b></a></td>\n" +
        		"\t<td><a href=\"/anime/list/" + FORM_ORDERBY_NOTA + "\"><b>Preço</b></a></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Detalhar</b></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Atualizar</b></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Excluir</b></td>\n" +
        		"</tr>\n";
		
		List<Anime> animes;
		if (orderBy == FORM_ORDERBY_ID) {                 	animes = animeDAO.getOrderByID();
		} else if (orderBy == FORM_ORDERBY_TITULO) {		animes = animeDAO.getOrderByTitulo();
		} else if (orderBy == FORM_ORDERBY_NOTA) {			animes = animeDAO.getOrderByNota();
		} else {											animes = animeDAO.get();
		}

		int i = 0;
		String bgcolor = "";
		for (Anime p : animes) {
			bgcolor = (i++ % 2 == 0) ? "#fff5dd" : "#dddddd";
			list += "\n<tr bgcolor=\""+ bgcolor +"\">\n" + 
            		  "\t<td>" + p.getID() + "</td>\n" +
            		  "\t<td>" + p.getTitulo() + "</td>\n" +
            		  "\t<td>" + p.getNota() + "</td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"/anime/" + p.getID() + "\"><img src=\"/image/detail.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"/anime/update/" + p.getID() + "\"><img src=\"/image/update.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"javascript:confirmarDeleteanime('" + p.getID() + "', '" + p.getTitulo() + "', '" + p.getNota() + "');\"><img src=\"/image/delete.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "</tr>\n";
		}
		list += "</table>";		
		form = form.replaceFirst("<LISTAR-anime>", list);				
	}
	
	
	public Object insert(Request request, Response response) {
		String titulo = request.queryParams("titulo");
		int nota = Integer.parseInt(request.queryParams("nota"));
		LocalDateTime dataAtual = LocalDateTime.parse(request.queryParams("dataAtual"));
		
		String resp = "";
		
		Anime anime = new Anime(-1, titulo, dataAtual, nota);
		
		if(animeDAO.insert(anime) == true) {
            resp = "anime (" + titulo + ") inserido!";
            response.status(201); // 201 Created
		} else {
			resp = "anime (" + titulo + ") não inserido!";
			response.status(404); // 404 Not found
		}
			
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}

	
	public Object get(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Anime anime = (Anime) animeDAO.get(id);
		
		if (anime != null) {
			response.status(200); // success
			makeForm(FORM_DETAIL, anime, FORM_ORDERBY_TITULO);
        } else {
            response.status(404); // 404 Not found
            String resp = "anime " + id + " não encontrado.";
    		makeForm();
    		form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

		return form;
	}

	
	public Object getToUpdate(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Anime anime = (Anime) animeDAO.get(id);
		
		if (anime != null) {
			response.status(200); // success
			makeForm(FORM_UPDATE, anime, FORM_ORDERBY_TITULO);
        } else {
            response.status(404); // 404 Not found
            String resp = "anime " + id + " não encontrado.";
    		makeForm();
    		form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

		return form;
	}
	
	
	public Object getAll(Request request, Response response) {
		int orderBy = Integer.parseInt(request.params(":orderby"));
		makeForm(orderBy);
	    response.header("Content-Type", "text/html");
	    response.header("Content-Encoding", "UTF-8");
		return form;
	}			
	
	public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
		Anime anime = animeDAO.get(id);
        String resp = "";       

        if (anime != null) {
        	anime.setTitulo(request.queryParams("titulo"));
        	anime.setDataAtual(LocalDateTime.parse(request.queryParams("dataAtual")));
        	anime.setNota(Integer.parseInt(request.queryParams("nota")));
        	animeDAO.update(anime);
        	response.status(200); // success
            resp = "anime (ID " + anime.getID() + ") atualizado!";
        } else {
            response.status(404); // 404 Not found
            resp = "anime (ID \" + anime.getId() + \") não encontrado!";
        }
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}

	
	public Object delete(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Anime anime = animeDAO.get(id);
        String resp = "";       

        if (anime != null) {
            animeDAO.delete(id);
            response.status(200); // success
            resp = "anime (" + id + ") excluído!";
        } else {
            response.status(404); // 404 Not found
            resp = "anime (" + id + ") não encontrado!";
        }
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}
}