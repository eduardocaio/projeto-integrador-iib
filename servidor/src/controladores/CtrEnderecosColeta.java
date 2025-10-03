package controladores;

import entidades.EnderecoColeta;
import enums.TipoColeta;
import servicos.ServEnderecosColeta;

public class CtrEnderecosColeta {

    public static String getEnderecosColeta(String opcaoColeta){
        StringBuilder retornoEnderecos = new StringBuilder();
        int qtd = 1;
        TipoColeta tipo;
        switch(opcaoColeta){
            case "1":
                tipo = TipoColeta.METAL;
                break;
            case "2":
                tipo = TipoColeta.VIDRO;
                break;
            case "3":
                tipo = TipoColeta.PAPEL;
                break;
            case "4":
                tipo = TipoColeta.PLASTICO;
                break;
            default:
                tipo = TipoColeta.PAPEL;
                break;
        }
        for(EnderecoColeta endereco : ServEnderecosColeta.getEnderecos()){
            if(endereco.getTipoColeta() == tipo){
                retornoEnderecos.append("Endereco " + qtd + ": " + endereco.getEndereco());
                qtd++;
            }
        }

        return retornoEnderecos.toString();
    }

    public static void cadastrarEndereco(String opcaoColeta, String endereco){
        TipoColeta tipo;
        switch(opcaoColeta){
            case "1":
                tipo = TipoColeta.METAL;
                break;
            case "2":
                tipo = TipoColeta.VIDRO;
                break;
            case "3":
                tipo = TipoColeta.PAPEL;
                break;
            case "4":
                tipo = TipoColeta.PLASTICO;
                break;
            default:
                tipo = TipoColeta.PAPEL;
                break;
        }

        ServEnderecosColeta.cadastrarEnderecos(endereco, tipo);
    }

}
