package controladores;

import entidades.Usuario;
import enums.TipoUsuario;
import servicos.ServUsuarios;

public class CtrUsuarios {

    public static boolean loginUsuario(String nomeUsuario, String senha){
        return ServUsuarios.loginUsuario(nomeUsuario, senha) == null ? false : true;
    }

    public static boolean isAdministrador(String nomeUsuario){
        for(Usuario usuario : ServUsuarios.getUsuariosAdmin()){
            if(usuario.getNomeUsuario().equals(nomeUsuario))
                return true;
        }

        return false;
    }

    public static String realizarCadastro(String nome, String nomeUsuario, String senha, String opcao){
        TipoUsuario tipoUsuario;
        if(opcao.equals("0"))
            tipoUsuario = TipoUsuario.CLIENTE;
        else
            tipoUsuario = TipoUsuario.ADMINISTRADOR;
        
        return ServUsuarios.cadastrarUsuario(nomeUsuario, nome, senha, tipoUsuario);
    }
}
