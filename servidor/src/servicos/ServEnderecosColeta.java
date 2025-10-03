package servicos;

import java.util.ArrayList;

import entidades.EnderecoColeta;
import enums.TipoColeta;

public class ServEnderecosColeta {

    public static ArrayList<EnderecoColeta> enderecos = new ArrayList<>();

    public static ArrayList<EnderecoColeta> getEnderecos() {
        return enderecos;
    }

    public static void cadastrarEnderecos(String endereco, TipoColeta tipoColeta){
        enderecos.add(new EnderecoColeta(endereco, tipoColeta));
    }
}
