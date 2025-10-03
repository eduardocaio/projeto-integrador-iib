package servicos;

import java.util.ArrayList;

import entidades.Usuario;
import enums.TipoUsuario;

public class ServUsuarios {

    private static ArrayList<Usuario> usuarios = new ArrayList<>();

    public static ArrayList<Usuario> getUsuariosAdmin(){
        ArrayList<Usuario> admins = new ArrayList<>();
        for(Usuario usuario : usuarios){
            if(usuario.getTipoUsuario() == TipoUsuario.ADMINISTRADOR)
                admins.add(usuario);
        }

        return admins;
    }

    public static ArrayList<Usuario> getUsuariosClientes(){
        ArrayList<Usuario> clientes = new ArrayList<>();
        for(Usuario usuario : usuarios){
            if(usuario.getTipoUsuario() == TipoUsuario.CLIENTE)
                clientes.add(usuario);
        }

        return clientes;
    }

    public static String cadastrarUsuario(String nomeUsuario, String nome, String senhaUsuario, TipoUsuario tipoUsuario){
        if(!nomeUsuario.matches("[A-Za-z0-9]+"))
            return "Nome de usuário inválido, utilize apenas letras e números!";

        for(Usuario usuario : usuarios){
            if(usuario.getNomeUsuario().equalsIgnoreCase(nomeUsuario))
                return "Nome de usuário já cadastrado!";
        }

        usuarios.add(new Usuario(nome, nomeUsuario, senhaUsuario, tipoUsuario));

        return "";
    }

    public static Usuario loginUsuario(String nomeUsuario, String senhaUsuario){
        for(Usuario usuario : usuarios){
            if(usuario.getNomeUsuario().equals(nomeUsuario) && usuario.getSenhaUsuario().equals(senhaUsuario))
                return usuario;
        }

        return null;
    }
}
