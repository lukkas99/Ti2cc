package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Anime {
	private int id;
	private String titulo;
	private LocalDateTime dataAtual;	
	private int nota;
	
	public Anime() {
		id = -1;
		titulo = "";
		dataAtual = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
		nota = 0;
	}

	public Anime(int id, String titulo, LocalDateTime dataAtual, int nota) {
		setId(id);
		setTitulo(titulo);
		setNota(nota);
		setDataAtual(dataAtual);
	}		
	
	public int getID() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String descricao) {
		this.titulo = descricao;
	}


	public int getNota() {
		return nota;
	}
	
	public void setNota(int nota) {
		this.nota = nota;
	}
	

	public LocalDateTime getDataAtual() {
		return dataAtual;
	}

	public void setDataAtual(LocalDateTime dataAtual) {
		// Pega a Data Atual
		this.dataAtual = LocalDateTime.now();
	}

	/**
	 * Método sobreposto da classe Object. É executado quando um objeto precisa
	 * ser exibido na forma de String.
	 */
	@Override
	public String toString() {
		return "Titulo Anime: " + titulo +   "      Data: " + " Nota.: " + nota
				+ dataAtual;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (this.getID() == ((Anime) obj).getID());
	}	
}