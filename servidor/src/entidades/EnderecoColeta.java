package entidades;

import enums.TipoColeta;

public class EnderecoColeta {

    private String endereco;
    private TipoColeta tipoColeta;
    
    public EnderecoColeta(String endereco, TipoColeta tipoColeta) {
        this.endereco = endereco;
        this.tipoColeta = tipoColeta;
    }

    public String getEndereco() {
        return endereco;
    }

    public TipoColeta getTipoColeta() {
        return tipoColeta;
    }

    
}
