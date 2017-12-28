
import java.io.IOException;

import AFD.AFD;
import AFD.State;

public class Principal {

	public static void main(String[] args) throws IOException {
		/*Minimiza��o de Automatos*/
		AFD m = new AFD();
		m.load();
		m.minimum().save();
				
		/*Equivalencia de Automatos*/
		/*AFD m = new AFD();
		AFD m2 = new AFD();
		m.load();
		m2.load();
		if (new AFD().equivalents(m, m2)) {
			System.out.println("S�o Equivalentes");
		} else {
			System.out.println("N�o s�o Equivalentes");
		}*/

		
		/*Uni�o de Automatos*/
		/*AFD m = new AFD();
		AFD m2 = new AFD();
		m.load();
		m2.load();
		m.union(m2).save();*/
		
		
		/*Interse��o de Automatos*/
		/*AFD m = new AFD();
		AFD m2 = new AFD();
		m.load();
		m2.load();
		m.intersection(m2).save();*/
		
		
		/*Complemento de Automatos*/
		/*AFD m = new AFD();
		m.load();
		m.complement().save();
		m.save();*/
		
		
		/*Diferen�a de Automatos*/
		/*AFD m = new AFD();
		AFD m2 = new AFD();
		m.load();
		m2.load();
		m.difference(m2).save();*/
		
		
		/*Lista de pares equivalentes*/
		/*AFD m = new AFD();
		m.load();
		List<ParOrdenado> listaEstadosEquivalentes = m.equivalents();
		for (int i = 0; i < listaEstadosEquivalentes.size(); i++) {
			System.out.printf("Par(%d, %d) s�o equivalentes.\n", listaEstadosEquivalentes.get(i).getX(), listaEstadosEquivalentes.get(i).getY());
		}*/
		
		
		/*Aceita determinada palavra*/
		/*AFD m = new AFD();
		m.load();
		if (m.accept("abbababbababbaaa")) {
			System.out.println("Aceita");
		} else {
			System.out.println("N�o Aceita");
		}*/
		
		
		/*Estado inicial*/
		/*AFD m = new AFD();
		m.load();
		State state = m.initial();*/
		
		
		/*Anda no automato*/
		/*AFD m = new AFD();
		m.load();
		State state = m.initial();
		state = m.move(state, "aaaa");
		System.out.println(state.getId());*/
		
		
		/*Verifica se Estado � final*/
		/*AFD m = new AFD();
		m.load();
		State state = m.initial();
		state = m.move(state, "aaaa");
		if (m.isFinal(state)) {
			System.out.println("Aceita");
		} else {
			System.out.println("N�o Aceita");
		}*/
		
		/*Salva Automato*/
		/*AFD m = new AFD();
		m.load();
		m.save();*/
	}
}
