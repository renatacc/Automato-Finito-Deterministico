package AFD;

import java.util.ArrayList;

public class NodoMinimizacao {
		private ENodoVerificacao equivalente;  			//define a verificação da posição: se foi verificado, se é equivalente, ou se nao é equivalente
		private ParOrdenado par;						//par ordenado de id estados a que se refere o nodo 
		private ArrayList<ParOrdenado> pendencias;		//lista de pares que tem que voltar ao resolver este
		
		public NodoMinimizacao() {
			equivalente 	= ENodoVerificacao.NAO_VERIFICADO;
			par 			= new ParOrdenado();
			pendencias 		= new ArrayList<>();
		}
		
		public NodoMinimizacao(int x, int y) {
			equivalente 	= ENodoVerificacao.NAO_VERIFICADO;
			par 			= new ParOrdenado(x, y);
			pendencias 		= new ArrayList<>();
		}
		
		public ENodoVerificacao getEquivalente() {
			return equivalente;
		}

		public void setEquivalente(ENodoVerificacao equivalente) {
			this.equivalente = equivalente;
		}

		public ParOrdenado getPar() {
			return par;
		}

		public void setPar(ParOrdenado par) {
			this.par = par;
		}

		public ArrayList<ParOrdenado> getPendencias() {
			return pendencias;
		}

		public void setPendencias(ArrayList<ParOrdenado> pendencias) {
			this.pendencias = pendencias;
		}
	}