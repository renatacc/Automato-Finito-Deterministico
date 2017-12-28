package AFD;

import java.util.ArrayList;

public class NodoEquivalenciaAFD {
		private ENodoVerificacao equivalente;  			//define a verificação da posição: se foi verificado, se é equivalente, ou se nao é equivalente
		private ParOrdenadoOrigem par;					//par ordenado de id estados a que se refere o nodo 
		private ArrayList<ParOrdenadoOrigem> pendencias;		//lista de pares que tem que voltar ao resolver este
		
		public NodoEquivalenciaAFD() {
			equivalente 	= ENodoVerificacao.NAO_VERIFICADO;
			par 			= new ParOrdenadoOrigem();
			pendencias 		= new ArrayList<>();
		}
		
		public NodoEquivalenciaAFD(int x, int y, AFD xOrigem, AFD yOrigem) {
			equivalente 	= ENodoVerificacao.NAO_VERIFICADO;
			par 			= new ParOrdenadoOrigem(x, y, xOrigem, yOrigem);
			pendencias 		= new ArrayList<>();
		}

		public ENodoVerificacao getEquivalente() {
			return equivalente;
		}

		public void setEquivalente(ENodoVerificacao equivalente) {
			this.equivalente = equivalente;
		}

		public ParOrdenadoOrigem getPar() {
			return par;
		}

		public void setPar(ParOrdenadoOrigem par) {
			this.par = par;
		}

		public ArrayList<ParOrdenadoOrigem> getPendencias() {
			return pendencias;
		}

		public void setPendencias(ArrayList<ParOrdenadoOrigem> pendencias) {
			this.pendencias = pendencias;
		}
	}