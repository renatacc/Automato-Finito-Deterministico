package AFD;

public enum ENodoVerificacao {
	NAO_VERIFICADO("NADA"), EQUIVALENTE("Equivalente"), NAO_EQUIVALENTE("Não Equivalente");
	
	String nome;
	
	private ENodoVerificacao(String descricao) {
		nome = descricao;
	}

	public String getNome() {
		return nome;
	}
}
