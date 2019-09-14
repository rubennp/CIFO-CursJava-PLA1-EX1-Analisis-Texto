package main;

import java.util.Scanner;
import java.util.ArrayList;
import java.text.Collator;
import java.util.Locale;

public class Main {
	
	/*
	 * Pide texto largo.
	 * Almacena cada frase (separada por '.') en ArrayList.
	 * Almacena cada palabra (separada por ' ') en otro ArrayList.
	 * 
	 * -> Muestra lista de frases ordenada de la más corta a la más larga (en nº de letras).
	 * -> Muestra palabras en orden de las más frecuentes a las menos (si varias palabras tienen mismo nº se utiliza orden alfabético).
	 */
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		
		// Pide texto al usuario y almacena
		System.out.println("*Introduce un texto:");
		String texto = in.nextLine();
		in.close();
		
		// Guarda en ArrayList cada frase del texto (separadas por '.')
		ArrayList<String> frases = guardaFrases(texto);
	
		// Guarda en ArrayList palabras de cada frase (separada por ' ')
		ArrayList<String> palabras = guardaPalabras(texto);
		
		// Muestra frases de menor a mayor número de letras a menos
		System.out.println("\n-----------------------------------------");
		System.out.println("Frases de menor a mayor número de letras:");
		System.out.println("-----------------------------------------");
		frases = ordenaFrases(frases);
		for (String frase : frases) System.out.println("-> " + frase);
		
		// Muestra palabras de mayor aparición a menor (o alfabéticamente si se repite nº apariciones)
		System.out.println("\n---------");
		System.out.println("Palabras:");
		System.out.println("---------");
		ArrayList<Integer> apariciones = cuentaApariciones(palabras);
		palabras = ordenaPalabras(palabras, apariciones);
		for (int i = 0; i < palabras.size(); i++) System.out.println("-> " + (i+1) + ". " + palabras.get(i) + "(" + apariciones.get(i) + ")");
	}
	
	/*
	 * ordenaPalabras(ArrayList<String>, ArrayList<Integer>) -> ArrayList<String>
	 * 
	 * Ordena palabras de mayor a menor aparición.
	 * Palabras que tienen igual aparición ordena alfabéticamente.
	 */
	private static ArrayList<String> ordenaPalabras(ArrayList<String> palabras, ArrayList<Integer> apariciones) {
		ArrayList<String> palabrasOrdenadas = new ArrayList<String>();
		// para ordenación alfabética
		Collator orden = Collator.getInstance(new Locale("ca"));	// (he escogido el catalán como Locale por que es más restrictivo con las tildes y creo funcionará en otros idiomas con menos tildes)
		orden.setStrength(Collator.PRIMARY);
		
		// 1o ordena palabras por aparición
		for (int i1 = 0; i1 < palabras.size(); i1++) {
			for (int i2 = i1+1; i2 < palabras.size(); i2++) {
				if (apariciones.get(i1) < apariciones.get(i2)) {
					// memoriza 1r elemento
					String palabraAux = palabras.get(i1);
					int aparicionesAux = apariciones.get(i1);
					
					// pasa 2o elemento a posición 1o
					palabras.set(i1, palabras.get(i2));
					apariciones.set(i1, apariciones.get(i2));
					
					// pasa memorizado a 2a posición
					palabras.set(i2, palabraAux);
					apariciones.set(i2, aparicionesAux);
				}
			}
		}
				
		// ahora ordena alfabéticamente dentro de cada grupo de repeticiones
		// recorre lista palabras
		int i1 = 0;
		while (i1 < palabras.size()) {
			// guarda grupo segun número de repeticiones
			int grupoApariciones = apariciones.get(i1);
			
			// donde guardará palabras grupo
			ArrayList<String> palabrasGrupo = new ArrayList<String>();
			
			// guarda 1a palabra del grupo
			palabrasGrupo.add(palabras.get(i1));
			
			// coge siguiente palabra hasta que cambie número de repeticiones o se acaben palabras
			int i2 = i1+1; 
			while (i2 < palabras.size() && grupoApariciones == apariciones.get(i2)) {
				palabrasGrupo.add(palabras.get(i2));
				i2++;
			}
			
			i1 = i2;	// se posiciona en siguiente palabra para no recorrer palabras ya analizadas
			
			palabrasGrupo.sort(orden);	// ordena alfabéticamente un grupo de palabras
			
			// guarda las palabras del grupo ordenadas en Array para retornar
			palabrasOrdenadas.addAll(palabrasGrupo);
		}
		
		return palabrasOrdenadas;
	}
	/*
	 * guardaPalabras(String) -> ArrayList<String>
	 * 
	 * Guarda en ArrayList palabras de cada frase (separada por ' ')
	 */
	private static ArrayList<String> guardaPalabras (String texto) {
		ArrayList<String> palabras = new ArrayList<String>();
		
		String[] palabrasAux = texto.split("[\\\"+*()/&%$:;.,!¡¿? ]");
		for (int i = 0; i < palabrasAux.length; i++) {
			// quita palabras "vacias"
			if (!palabrasAux[i].equals("")) palabras.add(palabrasAux[i]);
		}
		
		return palabras;
	}
	/*
	 * guardaFrases(String) -> ArrayList<String>
	 * 
	 * Guarda en ArrayList cada frase del texto (separadas por '.')
	 */
	private static ArrayList<String> guardaFrases(String texto) {
		ArrayList<String> frases = new ArrayList<String>();
		
		String[] frasesAux = texto.split("[.]");
		for (String frase : frasesAux) frases.add(frase.trim());
		
		return frases; 
	}
	/*
	 * cuentaApariciones(ArrayList<String>) -> ArrayList<Integer>
	 * 
	 * Cuenta número apariciones de palabras.
	 */
	private static ArrayList<Integer> cuentaApariciones(ArrayList<String> palabras) {
		ArrayList<Integer> apariciones = new ArrayList<Integer>();
		
		// 1a palabra para comparar
		for (int i1 = 0; i1 < palabras.size(); i1++) {
			int aparece = 1;	// palabra aparecerá (al menos) 1 vez
			// 2a palabra a comparar
			for (int i2 = i1+1; i2 < palabras.size(); i2++) {
				// si se repite aumenta valor aparición
				if (palabras.get(i1).equalsIgnoreCase(palabras.get(i2))) {
					aparece += 1;
					palabras.remove(i2); // quita la palabra para no volver a contar-la en 1r for
				}
			}
			
			apariciones.add(aparece);	// guarda total apariciones
		}
		
		return apariciones;
	}
	
	/*
	 * ordenaFrases(ArrayList<String>) -> ArrayList<String>
	 * 
	 * Ordena las frases de menor número de letras a mayor y retorna el ArrayList.
	 */
	private static ArrayList<String> ordenaFrases(ArrayList<String> frases) {
		for (int i1 = 0; i1 < frases.size(); i1++) {
			for (int i2 = i1+1; i2 < frases.size(); i2++) {
				if (cuentaLetras(frases.get(i1)) > cuentaLetras(frases.get(i2))) {
					String fraseAux = frases.get(i1);
					frases.set(i1, frases.get(i2));
					frases.set(i2, fraseAux);
				}
			}
		}
		
		return frases;
	}
	
	/*
	 * cuentaLetras(String) -> Int
	 * 
	 * Cuenta letras en una frase y retorna número de ellas.
	 */
	private static int cuentaLetras(String frase) {
		return frase.replace("[\\\"+*()/&%$:;.,!¡¿? ]", "").length();
	}
}
