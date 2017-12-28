package AFD;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class AFD {

	private ArrayList<State> estados;
	private ArrayList<String> alfabeto;
	private ArrayList<Transition> transicoes;
	private State estadoInicial;
	private ArrayList<State> estadosFinais;
	
	public AFD() {
		estados = new ArrayList<>();
		alfabeto = new ArrayList<>();
		transicoes = new ArrayList<>();
		estadoInicial = new State();	
		estadosFinais = new ArrayList<>();
		
		estadoInicial.setId(-1);
		estadoInicial.setName(-1);	
	}
	
	//Metodos Publicos *********************************************************************************************************
	public ArrayList<String> getAlfabeto() {
		return alfabeto;
	}
	
	public void load() throws IOException {
		State newState = null;
		Transition newTransition = null;
		String 	linha, tagTemp="", tipoTag="";
		boolean stateFinal = false, stateInitial = false;
		
		JFileChooser file = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".jff", "jff");
	    file.setFileFilter(filter);
	    file.setDialogType(JFileChooser.OPEN_DIALOG);
	    int i= file.showOpenDialog(null);
	    if (i==1){
	    	return;
	    } else {
	    	File arquivo = file.getSelectedFile();			//pega o nome do arquivo
			Reader reader = new FileReader(arquivo);		//abre o arquivo para leitura
			//************************************************************************
			BufferedReader lerArq = new BufferedReader(reader);
			linha = lerArq.readLine();	
			
			while (linha != null) {					// enquanto existir linhas a serem lidas
				tagTemp = pegaTag(linha);			
				tipoTag = pegaTipoTag(tagTemp);
				if (tipoTag.equals("state")) {		// se a tag for state, cria um novo estado
					newState = new State();
					newState.setId(pegaID(tagTemp));
					newState.setName(pegaName(tagTemp));
					stateFinal = false; 
					stateInitial = false;
				} else {
					if (tagTemp.equals("<initial/>")) {			// se a tag for initial, define como estado inicial
						newState.setStateInitial(true);
						stateInitial = true;
					} else {
						if (tagTemp.equals("<final/>")) {		// se a tag for final, define como estado final
							newState.setStateFinal(true);
							stateFinal = true;
						} else {
							if (tagTemp.equals("</state>")) {	// adiciona estado no automato
								addState(newState.getId(), stateInitial, stateFinal);
								estados.get(pegaIndiceState(newState.getId())).setName(newState.getName());
							} else {
								if (tagTemp.equals("<transition>")) {	// se a tag for transition, cria nova transicao
									newTransition = new Transition();
								} else {
									if (tagTemp.equals("<from>")) {		// se a tag for from, define a origem da transicao
										newTransition.setFrom(Integer.parseInt(pegaValorTag(linha)));
									} else {
										if (tagTemp.equals("<to>")) {	// se a tag for to, define o destino da transicao
											newTransition.setTo(Integer.parseInt(pegaValorTag(linha)));
										} else {
											if (tagTemp.equals("<read>")) {	// se a tag for read, define a letra desta transicao
												newTransition.setLetra(pegaValorTag(linha));
											} else {
												if (tagTemp.equals("</transition>")) {	// adiciona transicao e a letra no alfabeto
													addTransition(newTransition.getFrom(), newTransition.getTo(), newTransition.getLetra());
													addAlfabeto(newTransition.getLetra());
												}
											}
										}
									}
								}
							}
						}
					}
				}

				linha = lerArq.readLine();
			}
			//************************************************************************
			reader.close();
	    }
    }
	
	public void save() throws IOException {
		JFileChooser file = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".jff", "jff");
	    file.setFileFilter(filter);
		
	    file.setDialogType(JFileChooser.SAVE_DIALOG);
	    int j= file.showSaveDialog(null);
	    if (j==1){
	    	//JtextFieldLocal.setText("");
	    } else {
	    	File arquivo = file.getSelectedFile();			//pega o nome do arquivo
	    	
	    	
	    	//Writer writer = new FileWriter(arquivo + ".jflap");		//abre um arquivo para escrita
	    	FileWriter arq = new FileWriter(arquivo + ".jff");
	        PrintWriter gravarArq = new PrintWriter(arq);
			//********************************************************************************
	        gravarArq.printf("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!--Created with JFLAP 6.4.--><structure>\n");
		        gravarArq.printf("\t<type>fa</type>\n");
			    // grava estados    
		        if (estados.size() == 0) {
		        	gravarArq.printf("\t<automaton/>\n");
		        } else {	
			        gravarArq.printf("\t<automaton>\n");
		        	gravarArq.printf("\t\t<!--The list of states.-->\n");
		        	for (int i = 0; i < estados.size(); i++) {
			        	gravarArq.printf("\t\t<state id=\"%d\" name=\"%d\">\n", estados.get(i).getId(), estados.get(i).getName());
			        		gravarArq.printf("\t\t\t<x></x>\n");
			        		gravarArq.printf("\t\t\t<y></y>\n");
							if (estados.get(i).isStateInitial()) {
				        		gravarArq.printf("\t\t\t<initial/>\n");
			        		}
			        		if (estados.get(i).isStateFinal()) {
				        		gravarArq.printf("\t\t\t<final/>\n");
			        		}
			        	gravarArq.printf("\t\t</state>\n");
					}
		        }
		        if (estados.size() != 0 && transicoes.size() == 0) {
		        	gravarArq.printf("\t</automaton>\n");
		        } 
		        // grava transicoes
		        if (transicoes.size() == 0) {
		        } else {
		        	gravarArq.printf("\t\t<!--The list of transitions.-->\n");
		        	for (int i = 0; i < transicoes.size(); i++) {
			        	gravarArq.printf("\t\t<transition>\n");
				        	gravarArq.printf("\t\t\t<from>%d</from>\n", transicoes.get(i).getFrom());
				        	gravarArq.printf("\t\t\t<to>%d</to>\n", transicoes.get(i).getTo());
				        	gravarArq.printf("\t\t\t<read>%s</read>\n", transicoes.get(i).getLetra());
			        	gravarArq.printf("\t\t</transition>\n");
		        	}
		        	gravarArq.printf("\t</automaton>\n");
		        }
			        
			gravarArq.printf("</structure>\n");
	        //********************************************************************************
	        gravarArq.close();									//fecha o arquivo
	    }
	}
	
	public boolean accept(String palavra) {
		String letra;
		for (int i = 0; i < palavra.toCharArray().length; i++) {
			letra = palavra.substring(i, i+1);
			if (!alfabeto.contains(letra)) {
				return false;
			}
		}
		State state = this.initial();	// pega estado inicial
		
		state = move(state, palavra);	// anda no automato consumindo a palavra
		
		if (isFinal(state)) {	
			return true;
		}
		
		return false;
	}
	
	public State move(State estado, String palavra) {
		State state = estado;
		String letra;
		
		while (!palavra.equals("") && state != null) {			// enquanto a palavra nao for vazia
			letra = palavra.substring(0, 1);	//consome letra
			palavra = palavra.substring(1, palavra.length());

			state = walkAFD(state, letra);		// caminha com a letra para o proximo estado
		}
		
		return state;
	}
	
	public AFD union(AFD afd) {
		int indices[][] = new int[estados.size()][afd.estados.size()];	// matriz para indexar o novo indice do automato resultante da uniao
		boolean stateInitial, stateFinal;
		AFD newAFD = new AFD();
		int cont = 0, afd1To, afd2To;
		
		for (int i = 0; i < estados.size(); i++) {
			for (int j = 0; j < afd.estados.size(); j++) {
				
				indices[i][j] = cont;  // o indice da linha corresponde ao estado do automato 1 e o indice da coluna corresponde ao estado do automato 2
				
				if (isInitial(estados.get(i)) && afd.isInitial(afd.estados.get(j))) {	// verifica se ambos os estados sao iniciais
					stateInitial = true;
				} else {
					stateInitial = false;
				}
				
				if (isFinal(estados.get(i)) || afd.isFinal(afd.estados.get(j))) {	// verifica se um dos estados sao finais
					stateFinal = true;
				} else {
					stateFinal = false;
				}
				
				newAFD.addState(cont, stateInitial, stateFinal);	// adiciona estado no novo afd 
				newAFD.estados.get(cont).setName(cont+1);
				
				//System.out.print(indices[i][j] + " ");
				cont++;
			}
			
			//System.out.println("\n");
		}
		
		newAFD.alfabeto = alfabeto;		// copia o alfabeto
		
		for (int i = 0; i < estados.size(); i++) {	// gera novas transicoes para o novo automato
			for (int j = 0; j < afd.estados.size(); j++) {
				for (int k = 0; k < alfabeto.size(); k++) {					
					afd1To = searchDestino(estados.get(i).getId(), alfabeto.get(k));			
					afd2To = afd.searchDestino(afd.estados.get(j).getId(), alfabeto.get(k));
					
					newAFD.addTransition(indices[i][j], indices[afd1To][afd2To], alfabeto.get(k));
				} 
			}
		}
		
		return newAFD;
	}
	
	public AFD intersection(AFD afd) {	
		int indices[][] = new int[estados.size()][afd.estados.size()]; // matriz para indexar o novo indice do automato resultante da intersecao
		boolean stateInitial, stateFinal;
		AFD newAFD = new AFD();
		int cont = 0, afd1To, afd2To;
		
		for (int i = 0; i < estados.size(); i++) {
			for (int j = 0; j < afd.estados.size(); j++) {
				
				indices[i][j] = cont;	// o indice da linha corresponde ao estado do automato 1 e o indice da coluna corresponde ao estado do automato 2
				
				if (isInitial(estados.get(i)) && afd.isInitial(afd.estados.get(j))) {	// verifica se ambos os estados sao iniciais
					stateInitial = true;
				} else {
					stateInitial = false;
				}
				
				if (isFinal(estados.get(i)) && afd.isFinal(afd.estados.get(j))) {		// verifica se ambos os estados sao finais
					stateFinal = true;
				} else {
					stateFinal = false;
				}
				
				newAFD.addState(cont, stateInitial, stateFinal);	// adiciona estado no novo afd 
				newAFD.estados.get(cont).setName(cont+1);
				
				//System.out.print(indices[i][j] + " ");
				cont++;
			}
			
			//System.out.println("\n");
		}
		
		newAFD.alfabeto = alfabeto;
		
		for (int i = 0; i < estados.size(); i++) {	// gera novas transicoes para o novo automato
			for (int j = 0; j < afd.estados.size(); j++) {
				for (int k = 0; k < alfabeto.size(); k++) {					
					afd1To = searchDestino(estados.get(i).getId(), alfabeto.get(k));
					afd2To = afd.searchDestino(afd.estados.get(j).getId(), alfabeto.get(k));
					
					newAFD.addTransition(indices[i][j], indices[afd1To][afd2To], alfabeto.get(k));
				} 
			}
		}
		
		return newAFD;
	}
	
	public AFD complement() {
		AFD newAFD = new AFD();		
		// copia alfabeto para o novo afd	
		for (int i = 0; i < alfabeto.size(); i++) {
			newAFD.addAlfabeto(alfabeto.get(i));
		}
		// copia os estados para o novo afd
		for (int i = 0; i < estados.size(); i++) {
			newAFD.addState(estados.get(i).getId(), isInitial(estados.get(i)), !isFinal(estados.get(i)));
		}
		
		// copia as transicoes para o novo afd
		for (int i = 0; i < transicoes.size(); i++) {
			newAFD.addTransition(transicoes.get(i).getFrom(), transicoes.get(i).getTo(), transicoes.get(i).getLetra());
		}
		
		return newAFD;
	}
	
	public AFD difference(AFD afd) {
		return intersection(afd.complement());	// diferenca é a intersecao de um automato com o complemento do segundo
	}

	public List<ParOrdenado> equivalents() {
		int x, y, tam = estados.size()-1;	// o tamanho é a quantidade de estados menos 1
		// a matriz da classe NodoMinimizacao é utilizada para armazenar cada par de estados a serem analisados
		NodoMinimizacao[][] matriz = new NodoMinimizacao[tam][tam]; //sera utilizado apenas a parte inferior da matriz
		ParOrdenado newPar = null;
		int i = 0, j = 0;
		
		// inicializacao da matriz
		while (i <= tam-1) {
			x = estados.get(i+1).getId();	// o indice de linhas inicia no segundo estado do afd
			y = estados.get(j).getId();		// o indice de colunas inicia no primeiro estado do afd
			// o x sempre sera o estado que corresponde ao indice da linha incrementado de 1
			matriz[i][j] = new NodoMinimizacao(x, y); // armazena o par ordenado que representa os estados a serem analisados nessa posicao
			if (estados.get(i+1).isStateFinal() != estados.get(j).isStateFinal()) {	// se um estado é final e o outro nao, entao este par ja nao é equivalente
				matriz[i][j].setEquivalente(ENodoVerificacao.NAO_EQUIVALENTE);
			}
			//System.out.printf("Matriz[%d][%d] = Par(%d,%d) %s\n",i,j, x, y, matriz[i][j].getEquivalente().getNome());
			if (i == j) {	// faz movimentacao na matriz triangular inferior
				i++;
				j = 0;
			} else {
				j++;
			}
		}
		
		i = 0;
		j = 0;
		//System.out.println();
		// anda na matriz verificando se os estados sao equivalentes
		while (i <= tam-1) {
			if (matriz[i][j].getEquivalente() == ENodoVerificacao.NAO_VERIFICADO) {	// se o par ainda nao foi verificado
				for (int k = 0; k < alfabeto.size(); k++) {	// verifica todas as letras do alfabeto
					x = searchDestino(matriz[i][j].getPar().getX(), alfabeto.get(k));	// procura destino do x consumindo esta letra 
					y = searchDestino(matriz[i][j].getPar().getY(), alfabeto.get(k));	// procura destino do y consumindo esta letra 
					newPar = searchParOrdenado(matriz, x, y);	// procura par de destino correspondente 
					
					if (x != y) {	// se os estados sao diferentes
						// se o par destino ja nao é equivalente, entao marca o estado e todas as suas pendencias como nao equivalente
						if (matriz[newPar.getX()][newPar.getY()].getEquivalente() == ENodoVerificacao.NAO_EQUIVALENTE) {
							removePendencias(matriz, i, j);
							break;
						} else {
							// adicionar na pendencia do destino
							if ((newPar.getX() == i && newPar.getY() == j) || (newPar.getX() == j && newPar.getY() == i)) {
							} else {
								matriz[newPar.getX()][newPar.getY()].getPendencias().add(new ParOrdenado(i, j));
								//System.out.printf("Matriz[%d][%d] é Pendencia de Matriz[%d][%d]\n", i, j, newPar.getX(), newPar.getY());
							}
						}
					}
				}
			}
			
			if (i == j) {	// faz movimentacao na matriz triangular inferior
				i++;
				j = 0;
			} else {
				j++;
			}
		}

		List<ParOrdenado> newLista = new ArrayList<ParOrdenado>();	// cria lista de pares ordenados equivalentes
		i = 0;
		j = 0;
		while (i <= tam-1) {
			x = estados.get(i+1).getId();
			y = estados.get(j).getId();
			// System.out.printf("Matriz[%d][%d] = Par(%d,%d) %s\n",i,j, x, y, matriz[i][j].getEquivalente().getNome());
			// se o estado continua como nao verificado apos todas as verificacoes, entao ele é equivalente e adiciona o par a lista de equivalencias
			if (matriz[i][j].getEquivalente() == ENodoVerificacao.NAO_VERIFICADO) { 
				matriz[i][j].setEquivalente(ENodoVerificacao.EQUIVALENTE);
				newLista.add(matriz[i][j].getPar());
			}
			if (i == j) { 	// faz movimentacao na matriz triangular inferior
				i++;
				j = 0;
			} else {
				j++;
			}
		}
		
		return newLista;
	}
	
	public AFD minimum() {
		ArrayList<HashSet<Integer>> listaConjuntos = new ArrayList<>();	// cria lista de conjuntos de estados equivalentes, 
		// cada posicao nesta lista será correspondente a um novo estado no novo afd
		List<ParOrdenado> listaEstadosEquivalentes = equivalents();		// lista de estados equivalencia do afd com os pares ordenados
		AFD newAfd = new AFD();
		boolean stateInitial = false, stateFinal = false;
		int destino, indConjuntoDestino;
		
		listaConjuntos = createNewsConjuntos(listaEstadosEquivalentes);	// cria a lista de conjuntos a partir da lista de estados equivalentes
		//se um estado nao tem um equivalente ele sera um conjunto com um unico estado
		/*Cria Estados do novo AFD*/
		for (int i = 0; i < listaConjuntos.size(); i++) {
			stateFinal = false;
			stateInitial = false;
			
			if (isFinal(searchState(listaConjuntos.get(i).iterator().next()))) { // verifica se é um conjunto com estados finais
				stateFinal = true;
			}
			if (listaConjuntos.get(i).contains(initial().getId())) {			// verifica se o conjunto contem o estado inicial
				stateInitial = true;
			}
			newAfd.addState(i, stateInitial, stateFinal);	// cria um novo estado, onde o indice do estado é o indice correspodende ao conjunto na lista
		}
		/*Cria Alfabeto do novo AFD*/
		for (int i = 0; i < alfabeto.size(); i++) {
			newAfd.addAlfabeto(alfabeto.get(i));
		}
		/*Cria Transições do novo AFD*/
		for (int i = 0; i < newAfd.estados.size(); i++) {
			for (int j = 0; j < alfabeto.size(); j++) {
				destino = searchDestino(listaConjuntos.get(i).iterator().next(), alfabeto.get(j));	// procura o destino consumindo uma letra do primeiro estado
				indConjuntoDestino = searchIndiceInConjunto(listaConjuntos, destino);				// busca o indice do conjunto que contem o estado de destino encontrado, que corresponde ao estado no novo afd
				
				newAfd.addTransition(newAfd.estados.get(i).getId(), indConjuntoDestino, alfabeto.get(j)); // cria transicao com origem, destino e letra
			}
			newAfd.estados.get(i);
		}
		return newAfd;
	}
	
	public boolean equivalents(AFD m1, AFD m2) {
		//AFD afd1 = m1;
		//AFD afd2 = m2;
		AFD afd1 = m1.minimum();
		AFD afd2 = m2.minimum();
		AFD afdX, afdY;
		int tamAfd1 = afd1.estados.size();
		int tamAfd2 = afd2.estados.size();
		int x, y, tam = tamAfd1-1 + tamAfd2;
		NodoEquivalenciaAFD[][] matriz = new NodoEquivalenciaAFD[tam][tam];
		ParOrdenadoOrigem newPar = null;
		int i = 0, j = 0, indX = 0, indY = 0;

		// inicializacao da matriz
		while (i <= tam-1) {
			//controla os automatos a serem utilizados
			if (i < tamAfd1-1) {
				afdX = afd1;
			} else {
				afdX = afd2;
			}
			if (j >= tamAfd1) {
				afdY = afd2;
			} else {
				afdY = afd1;
			}
			//controla o indice dos estados a serem pegos dependendo do automato
			if (i == tamAfd1-1 && j == 0) {
				indX = -1;
			}
			if (j == tamAfd1 && i > tamAfd1-1) {
				indY = 0;
			}
			
			x = afdX.estados.get(indX+1).getId();	// o indice de linhas inicia no segundo estado do afd1
			y = afdY.estados.get(indY).getId();		// o indice de colunas inicia no primeiro estado do afd2
			matriz[i][j] = new NodoEquivalenciaAFD(x, y, afdX, afdY);	// armazena o par ordenado que representa os estados a serem analisados nessa posicao
			if (afdX.estados.get(indX+1).isStateFinal() != afdY.estados.get(indY).isStateFinal()) {
				matriz[i][j].setEquivalente(ENodoVerificacao.NAO_EQUIVALENTE);	// se um estado é final e o outro nao, entao este par ja nao é equivalente
			}
			//System.out.printf("Matriz[%d][%d] = Par(%d,%d) %s\n",i,j, x, y, matriz[i][j].getEquivalente().getNome());
			if (i == j) {	// faz movimentacao na matriz triangular inferior
				i++;
				j = 0;
				indX++;
				indY = 0;
			} else {
				j++;
				indY++;
			}
		}

		//System.out.println();
		i = 0;
		j = 0;
		// anda na matriz verificando se os estados sao equivalentes
		while (i <= tam-1) {
			if (matriz[i][j].getEquivalente() == ENodoVerificacao.NAO_VERIFICADO) {	// se o par ainda nao foi verificado
				for (int k = 0; k < afd1.alfabeto.size(); k++) {	// verifica todas as letras do alfabeto
					x = matriz[i][j].getPar().getxOrigem().searchDestino(matriz[i][j].getPar().getX(), afd1.alfabeto.get(k));	// procura destino do x em seu determinado afd consumindo esta letra
					y = matriz[i][j].getPar().getyOrigem().searchDestino(matriz[i][j].getPar().getY(), afd1.alfabeto.get(k));	// procura destino do y em seu determinado afd consumindo esta letra
					newPar = searchParOrdenado(matriz, x, y, tam, matriz[i][j].getPar().getxOrigem(), matriz[i][j].getPar().getyOrigem());	// procura par de destino correspondente 
					
					if (!(x == y && matriz[i][j].getPar().getxOrigem() == matriz[i][j].getPar().getyOrigem())) { // se os estados sao diferentes
						// se o par destino ja nao é equivalente, entao marca o estado e todas as suas pendencias como nao equivalente
						if (matriz[newPar.getX()][newPar.getY()].getEquivalente() == ENodoVerificacao.NAO_EQUIVALENTE) {
							removePendencias(matriz, i, j);
							break;
						} else {
							// adicionar na pendencia do destino
							if ((newPar.getX() == i && newPar.getY() == j) || (newPar.getX() == j && newPar.getY() == i)) {
								//se o par é pendencia dele mesmo, não faz nada
							} else {
								matriz[newPar.getX()][newPar.getY()].getPendencias().add(new ParOrdenadoOrigem(i, j, newPar.getxOrigem(), newPar.getyOrigem()));
								//System.out.printf("Matriz[%d][%d] é Pendencia de Matriz[%d][%d]\n", i, j, newPar.getX(), newPar.getY());
							}
						}
					}
				}
			}
			
			if (i == j) {	// faz movimentacao na matriz triangular inferior
				i++;
				j = 0;
			} else {
				j++;
			}
		}

		//System.out.println();
		//System.out.println(afd1.estadoInicial.getId());
		//System.out.println(afd2.estadoInicial.getId());
		i = 0;
		j = 0;
		boolean resp = false;
		
		// busca posicao onde o par é inicial nos dois automatos e verifica se sao equivalentes
		while (i <= tam-1) {
			x = matriz[i][j].getPar().getX();
			y = matriz[i][j].getPar().getY();
			//System.out.printf("Matriz[%d][%d] = Par(%d,%d) %s\n",i,j, x, y, matriz[i][j].getEquivalente().getNome());
			if (matriz[i][j].getPar().getxOrigem() == afd1 && matriz[i][j].getPar().getyOrigem() == afd2) {
				if (afd1.isInitial(afd1.searchState(matriz[i][j].getPar().getX())) && afd2.isInitial(afd2.searchState(matriz[i][j].getPar().getY()))) {
					//System.out.println("Inicial");
					if (matriz[i][j].getEquivalente() == ENodoVerificacao.NAO_VERIFICADO) {
						resp = true;
					}
				}
			}
			if (matriz[i][j].getPar().getxOrigem() == afd2 && matriz[i][j].getPar().getyOrigem() == afd1) {
				if (afd2.isInitial(afd2.searchState(matriz[i][j].getPar().getX())) && afd1.isInitial(afd1.searchState(matriz[i][j].getPar().getY()))) {
					//System.out.println("Inicial");
					if (matriz[i][j].getEquivalente() == ENodoVerificacao.NAO_VERIFICADO) {
						resp = true;
					}
				}
			}
			if (i == j) {	// faz movimentacao na matriz triangular inferior
				i++;
				j = 0;
			} else {
				j++;
			}
		}
		
		return resp;
	}
	
	public State initial() {
		return estadoInicial;
	}
	
	public ArrayList<State> finals() {
		return estadosFinais;
	} 
	
	public boolean isInitial(State state) {
		if(state.getId() == initial().getId()){
			return true;
		}
		
		return false;
	}
	
	public boolean isFinal(State state) {
		if (state == null)
			return false;
		
		for (int i = 0; i < estadosFinais.size(); i++) { // procura pelo estado na lista de finais
			if (estadosFinais.get(i).getId() == state.getId()) {
				return true;
			}
		}
		
		return false;
	}
	
	public void addAlfabeto(String letra) {
		if (!isInAlfabeto(letra)) {	// se a letra ainda nao esta no alfabeto adiciona na lista 
			this.alfabeto.add(letra);
		}		
	}
	
	public void addState(int id, boolean stateInitial, boolean stateFinal) {
		State newState = new State(id, stateInitial, stateFinal);
		estados.add(newState);
		
		if (stateInitial) {	// se é inicial adiciona como inicial
			estadoInicial = newState;
		}
		
		if (stateFinal) {	// se é final adiciona na lista de finais
			estadosFinais.add(newState); 
		}
	}
	
	public void addStateFinal(State state) {
		estadosFinais.add(state);
	}
	
	public void addStateInitial(State state) {
		this.estadoInicial = state;
	}
	
	public void addTransition(int from, int to, String consume) {
		Transition newTransition = new Transition(from, to, consume);
		transicoes.add(newTransition);
	}
	
	public void deleteState(int id) {
		for (int i = 0; i < estados.size(); i++) {	// busca pelo estado
			if (estados.get(i).getId() == id) {
				if (isFinal(estados.get(i))) {		// se ele é final remove da lista
					removeStateFinal(id);
				}
				if (isInitial(estados.get(i))) {	// se for inicial remove do inicial 
					estadoInicial = null;
				}
				estados.remove(i);					// remove estado da lista de estados
				return;
			}
		}
	}
	
	public void deleteTransition(int from, int to, String consume) {
		for (int i = 0; i < transicoes.size(); i++) {	// procura pela transicao
			if (transicoes.get(i).getLetra().equals(consume) && transicoes.get(i).getFrom() == from 
			   && transicoes.get(i).getTo() == to) {	// se encontra a remove
				transicoes.remove(i);
				return;
			}
		}
	}
	
	//Funções Privadas *********************************************************************************************************
	private ArrayList<HashSet<Integer>> createNewsConjuntos(List<ParOrdenado> listaEstadosEquivalentes) {
		ArrayList<HashSet<Integer>> listaConjuntos = new ArrayList<>();	// lista de conjuntos equivalentes
		HashSet<Integer> newConjuntoX, newConjuntoY, newConjunto;		
		
		for (int i = 0; i < listaEstadosEquivalentes.size(); i++) { // loop em pares equivalentes
			newConjuntoX = searchStateInConjunto(listaConjuntos, listaEstadosEquivalentes.get(i).getX());	// procura o conjunto que contem o estado X
			newConjuntoY = searchStateInConjunto(listaConjuntos, listaEstadosEquivalentes.get(i).getY());	// procura o conjunto que contem o estado Y
			
			if (newConjuntoX == null && newConjuntoY == null) {			 //significa que ainda não foi inserido em nenhum conjunto
				newConjunto = new HashSet<>();
				newConjunto.add(listaEstadosEquivalentes.get(i).getX());
				newConjunto.add(listaEstadosEquivalentes.get(i).getY());
				listaConjuntos.add(newConjunto);
			} else {													//significa que ja esta em algum conjunto
				if (newConjuntoX != null && newConjuntoY == null) {		// se o Y ainda nao foi inserido
					newConjunto = newConjuntoX;
					newConjunto.add(listaEstadosEquivalentes.get(i).getY());
				} else {
					if (newConjuntoX == null && newConjuntoY != null) { // se o X ainda nao foi inserido
						newConjunto = newConjuntoY;
						newConjunto.add(listaEstadosEquivalentes.get(i).getX());
					} else {											// se ambos ja foram inseridos
						newConjunto = newConjuntoX;
					}
				}
			}
			
			for (int j = i+1; j < listaEstadosEquivalentes.size(); j++) {	// procuram estados que sao equivalentes ao X e ao Y
				if (listaEstadosEquivalentes.get(i).getX() == listaEstadosEquivalentes.get(j).getX() || 
						listaEstadosEquivalentes.get(i).getX() == listaEstadosEquivalentes.get(j).getY() ||
						listaEstadosEquivalentes.get(i).getY() == listaEstadosEquivalentes.get(j).getX() ||
						listaEstadosEquivalentes.get(i).getY() == listaEstadosEquivalentes.get(j).getY()) {
					// se encontrar, insere no conjunto
					newConjunto.add(listaEstadosEquivalentes.get(i).getX());	
					newConjunto.add(listaEstadosEquivalentes.get(i).getY());
				}
			}
		}
		
		for (int i = 0; i < estados.size(); i++) {	// se existir um estado que nao está contido em algum conjunto, cria um novo conjunto
			if (searchStateInConjunto(listaConjuntos, estados.get(i).getId()) == null && 
					//isDestino(estados.get(i).getId())
					vemDoInicio(estados.get(i).getId(), new ArrayList<Integer>())
					) {//|| 
					//(isInitial(estados.get(i)))) {
				newConjunto = new HashSet<>();
				newConjunto.add(estados.get(i).getId());
				listaConjuntos.add(newConjunto);
			}
		}
		if((searchStateInConjunto(listaConjuntos, initial().getId()) == null)) {
			newConjunto = new HashSet<>();
			newConjunto.add(initial().getId());
			listaConjuntos.add(newConjunto);
		}
		
		/*for (int i = 0; i < listaConjuntos.size(); i++) {
			System.out.println(listaConjuntos.get(i));
		}*/
		
		return listaConjuntos;
	}
	
	private boolean vemDoInicio(int id, ArrayList<Integer> estadosVisitados) {
		ArrayList<Integer> lista;	// lista de origens com determinada letra
		estadosVisitados.add(id);	// lista de estados que ja foram visitados, para nao gerar ciclos no automato
		boolean retorno = false;
		
		if (searchState(id).isStateInitial()) {
			return true;
		}
		for (int i = 0; i < alfabeto.size(); i++) {
			lista = searchOrigem(id, alfabeto.get(i));
			if (lista.size() == 0) {
				//return false;
			}
			for (int j = 0; j < lista.size(); j++) {
				if (!estadosVisitados.contains(lista.get(j))) {
					if (vemDoInicio(lista.get(j), estadosVisitados)) {
						retorno = true;
						break;
					}
				}				
			}
			if (retorno) {
				break;
			}
		}
		
		return retorno;
	}
	
	private ArrayList<Integer> searchOrigem(int to, String letra) {
		ArrayList<Integer> lista = new ArrayList<>();
		for (int i = 0; i < transicoes.size(); i++) {
			if (transicoes.get(i).getTo() == to && transicoes.get(i).getLetra().equals(letra) && transicoes.get(i).getTo() != transicoes.get(i).getFrom()) {
				lista.add(transicoes.get(i).getFrom());
			}
		}
		
		return lista;
	}
	
	private HashSet<Integer> searchStateInConjunto(ArrayList<HashSet<Integer>> lista, int state) {
		// procura se o estado está contido em algum conjunto
		int ind = searchIndiceInConjunto(lista, state);
		
		if (ind == -1) {
			return null;
		} else {
			return lista.get(ind);
		}
	}
	
	private int searchIndiceInConjunto(ArrayList<HashSet<Integer>> lista, int state) {
		// retorna o indice do conjunto do estado na lista de conjuntos
		for (int i = 0; i < lista.size(); i++) {
			if (lista.get(i).contains(state)) {
				return i;
			}
		}
		
		return -1;
	}
	
	private State walkAFD(State state, String letra) {
		// retorna o proximo estado ao caminhar com uma letra
		for (int i = 0; i < transicoes.size(); i++) {
			if (transicoes.get(i).getFrom() == state.getId() && transicoes.get(i).getLetra().equals(letra)) {
				return searchState(transicoes.get(i).getTo()); 
			}
		}
		
		return null;
	}
	
	private State searchState(int id) {
		for (int i = 0; i < estados.size(); i++) {
			if (estados.get(i).getId() == id) {
				return estados.get(i);
			}
		}
		
		return null;
	}
	
	private int searchDestino(int from, String letra) {
		for (int i = 0; i < transicoes.size(); i++) {
			if (transicoes.get(i).getFrom() == from && transicoes.get(i).getLetra().equals(letra)) {
				return transicoes.get(i).getTo();
			}
		}
		
		return -1;
	}
	
	private ParOrdenado searchParOrdenado(NodoMinimizacao[][] matriz, int x, int y) {
		int i = 0, j = 0, tam = estados.size()-1;
		
		// retorna o par ordenado de indices onde se encontra o x e o y na matriz
		while (i <= tam-1) {
			if ((matriz[i][j].getPar().getX() == x && matriz[i][j].getPar().getY() == y) || (matriz[i][j].getPar().getX() == y && matriz[i][j].getPar().getY() == x)) {
				return new ParOrdenado(i,j);
			}
			
			if (i == j) {
				i++;
				j = 0;
			} else {
				j++;
			}
		}
		
		return null;
	}
	
	private ParOrdenadoOrigem searchParOrdenado(NodoEquivalenciaAFD[][] matriz, int x, int y, int tam, AFD afd1, AFD afd2) {
		int i = 0, j = 0;
		
		// retorna o par ordenado de indices onde se encontra o x e o y na matriz
		while (i <= tam-1) {
			if ((matriz[i][j].getPar().getX() == x && matriz[i][j].getPar().getY() == y && matriz[i][j].getPar().getxOrigem() == afd1 && matriz[i][j].getPar().getyOrigem() == afd2)) {
				return new ParOrdenadoOrigem(i,j, matriz[i][j].getPar().getxOrigem(), matriz[i][j].getPar().getyOrigem());
			}
			if (afd1 == afd2 && ((matriz[i][j].getPar().getX() == x && matriz[i][j].getPar().getY() == y) || ((matriz[i][j].getPar().getX() == y && matriz[i][j].getPar().getY() == x))) ){
				return new ParOrdenadoOrigem(i,j, matriz[i][j].getPar().getxOrigem(), matriz[i][j].getPar().getyOrigem());
			}
			
			if (i == j) {
				i++;
				j = 0;
			} else {
				j++;
			}
		}
		
		return null;
	}
	
	private boolean isInAlfabeto(String letra) {
		for (int i = 0; i < alfabeto.size(); i++) {
			if (alfabeto.get(i).equals(letra)) {
				return true;
			}
		}
		
		return false;
	}
	
	private void removePendencias(NodoMinimizacao[][] matriz, int i, int j) {
		int x, y;

		matriz[i][j].setEquivalente(ENodoVerificacao.NAO_EQUIVALENTE);
		while (matriz[i][j].getPendencias().size() != 0) {		
			// enquanto existirem pendencias neste determinado par, entra recursivamente dentro de cada par pendente, 
			// repete o processo alterando o estado para nao equivalente e o remove da lista de pendencias
			x = matriz[i][j].getPendencias().get(0).getX();
			y = matriz[i][j].getPendencias().get(0).getY();
			ParOrdenado newPar = new ParOrdenado(x, y);
			if(matriz[newPar.getX()][newPar.getY()].getEquivalente() != ENodoVerificacao.NAO_EQUIVALENTE) {
				removePendencias(matriz, newPar.getX(), newPar.getY());
			}
			
			matriz[i][j].getPendencias().remove(0);
		}
		
		//matriz[i][j].setEquivalente(ENodoVerificacao.NAO_EQUIVALENTE);
		//System.out.printf("Matriz[%d][%d] = Par(%d,%d) %s\n",i,j, matriz[i][j].getPar().getX(), matriz[i][j].getPar().getY(), matriz[i][j].getEquivalente().getNome());
	}
	
	private void removePendencias(NodoEquivalenciaAFD[][] matriz, int i, int j) {
		int x, y;
		
		while (matriz[i][j].getPendencias().size() != 0) {		
			// enquanto existirem pendencias neste determinado par, entra recursivamente dentro de cada par pendente,
			// repete o processo alterando o estado para nao equivalente e o remove da lista de pendencias
			x = matriz[i][j].getPendencias().get(0).getX();
			y = matriz[i][j].getPendencias().get(0).getY();
			ParOrdenado newPar = new ParOrdenado(x, y);
			
			removePendencias(matriz, newPar.getX(), newPar.getY());
			
			matriz[i][j].getPendencias().remove(0);
		}
		
		matriz[i][j].setEquivalente(ENodoVerificacao.NAO_EQUIVALENTE);
		//System.out.printf("Matriz[%d][%d] = Par(%d,%d) %s\n",i,j, matriz[i][j].getPar().getX(), matriz[i][j].getPar().getY(), matriz[i][j].getEquivalente().getNome());
	}
	
	private void removeStateFinal(int id) {
		for (int i = 0; i < estadosFinais.size(); i++) {
			if (estadosFinais.get(i).getId() == id) {
				estadosFinais.remove(i);
				return;
			}
		}
	}
	
	private String pegaTag(String linha) {
		int i = linha.indexOf("<");
		int j = linha.indexOf(">");
		
		return linha.substring(i,j+1);
	}
	
	private String pegaTipoTag(String linha) {
		int i = linha.indexOf("<");
		int j = linha.indexOf(" ");
		
		if (j == -1) {
			j = 1;
		}
		return linha.substring(i+1,j);
	}
	
	private String pegaValorTag(String linha) {
		int i = linha.indexOf(">");
		String valor = linha.substring(i+1, linha.length());
		i = valor.indexOf("<");
		return valor.substring(0,i);
	}
	
	private int pegaID(String linha) {
		int i = linha.indexOf("\"");
		linha = linha.substring(i+1, linha.length());
		i = linha.indexOf("\"");
		
		return Integer.parseInt(linha.substring(0,i));
	}
	
	private int pegaName(String linha) {
		int i = linha.indexOf("\"");
		linha = linha.substring(i+1, linha.length());
		i = linha.indexOf("\"");
		linha = linha.substring(i+1, linha.length());
		i = linha.indexOf("\"");
		linha = linha.substring(i+1, linha.length());
		i = linha.indexOf("\"");
		
		return Integer.parseInt(linha.substring(0,i));
	}
	
	private int pegaIndiceState(int id) {
		for (int i = 0; i < estados.size(); i++) {
			if (estados.get(i).getId() == id) {
				return i;
			}
		}
		return -1;
	}
}
