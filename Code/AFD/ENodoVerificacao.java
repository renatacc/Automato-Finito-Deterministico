package AFD;

public enum ENodoVerificacao {
	NAO_VERIFICADO("NADA"), EQUIVALENTE("Equivalente"), NAO_EQUIVALENTE("N�o Equivalente");
	
	String nome;
	
	private ENodoVerificacao(String descricao) {
		nome = descricao;
	}

	public String getNome() {
		return nome;
	}
}
