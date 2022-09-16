package app;

import static spark.Spark.*;
import service.AnimeService;
import service.AnimeService;

public class Aplicacao {
	
	private static AnimeService animeService = new AnimeService();
	
    public static void main(String[] args) {
        port(6789);
        
        staticFiles.location("/public");
        
        post("/anime/insert", (request, response) -> animeService.insert(request, response));

        get("/anime/:id", (request, response) -> animeService.get(request, response));
        
        get("/anime/list/:orderby", (request, response) -> animeService.getAll(request, response));

        get("/anime/update/:id", (request, response) -> animeService.getToUpdate(request, response));
        
        post("/anime/update/:id", (request, response) -> animeService.update(request, response));
           
        get("/anime/delete/:id", (request, response) -> animeService.delete(request, response));

             
    }
}