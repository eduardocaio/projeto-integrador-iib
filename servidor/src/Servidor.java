import java.io.*;
import java.net.*;
import controladores.CtrEnderecosColeta;
import controladores.CtrUsuarios;

public class Servidor {
    public static void main(String[] args) throws Exception {
        int port = 12345; // porta do servidor

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor iniciado na porta " + port);

            // Loop infinito para aceitar múltiplos clientes
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Cliente conectado: " + socket.getInetAddress());

                // Cria uma thread para tratar esse cliente
                new Thread(new ClienteHandler(socket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Classe interna que mantém toda a lógica do cliente
    static class ClienteHandler implements Runnable {
        private Socket socket;

        public ClienteHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                // **Aqui vai toda a lógica do seu while do cliente**
                String mensagem;
                boolean usuarioLogado = false;
                boolean exibindoMenu = false;
                boolean exibindoMenuColetaCliente = false;
                boolean exibindoMenuColetaAdm = false;
                String nomeUsuarioLogado = "";
                String opcaoColetaCadastro = "";
                boolean cadastrandoEndereco = false;

                while ((mensagem = in.readLine()) != null) {
                    if(!usuarioLogado){
                        String[] partes = mensagem.split("\\|");

                        if(partes[0].equalsIgnoreCase("cadastro")){
                            String nome = partes[1];
                            String nomeUsuario = partes[2];
                            String senha = partes[3];
                            String opcao = partes[4];
                            String retornoCadastro = CtrUsuarios.realizarCadastro(nome, nomeUsuario, senha, opcao);

                            if(!retornoCadastro.equals("")){
                                out.println(retornoCadastro);
                                continue;
                            }

                            out.println("");
                            usuarioLogado = true;
                            exibindoMenu = true;
                            nomeUsuarioLogado = nomeUsuario;
                            out.println("Usuário logado com sucesso. Seja bem-vindo, " + nomeUsuarioLogado + "!" + exibirMenu(nomeUsuarioLogado));
                            continue;
                        }
                        else{
                            String nomeUsuario = partes[1];
                            String senha = partes[2];

                            if(CtrUsuarios.loginUsuario(nomeUsuario, senha)){
                                out.println("");
                                usuarioLogado = true;
                                exibindoMenu = true;
                                nomeUsuarioLogado = nomeUsuario;
                                out.println("Usuário logado com sucesso. Seja bem-vindo, " + nomeUsuarioLogado + "!" + exibirMenu(nomeUsuarioLogado));
                                continue;
                            } else{
                                out.println("Usuário ou senha inválidos!");
                                continue;
                            }
                        }
                    }

                    if(mensagem.equals("0"))
                        break;

                    if(exibindoMenu){
                        if(mensagem.equals("1")){
                            out.println(opcoesColeta());
                            exibindoMenu = false;
                            exibindoMenuColetaCliente = true;
                            continue;
                        }
                        else if(mensagem.equals("2") && CtrUsuarios.isAdministrador(nomeUsuarioLogado)){
                            out.println(opcoesColeta());
                            exibindoMenu = false;
                            exibindoMenuColetaAdm = true;
                            continue;
                        }
                        else{
                            out.println(opcoesColeta());
                            exibindoMenu = false;
                            exibindoMenuColetaCliente = true;
                            continue;
                        }
                    }

                    if(exibindoMenuColetaCliente){
                        out.println(CtrEnderecosColeta.getEnderecosColeta(mensagem) + " " + exibirMenu(nomeUsuarioLogado));
                        exibindoMenuColetaCliente = false;
                        exibindoMenu = true;
                        continue;
                    }

                    if(exibindoMenuColetaAdm){
                        out.println("Digite o endereço do ponto de coleta: ");
                        opcaoColetaCadastro = mensagem;
                        exibindoMenuColetaAdm = false;
                        exibindoMenu = false;
                        cadastrandoEndereco = true;
                        continue;
                    }

                    if(cadastrandoEndereco){
                        CtrEnderecosColeta.cadastrarEndereco(opcaoColetaCadastro, mensagem);
                        out.println("Endereço cadastrado com sucesso! " + exibirMenu(nomeUsuarioLogado));
                        cadastrandoEndereco = false;
                        exibindoMenu = true;
                        continue;
                    }
                }

                socket.close();
                System.out.println("Cliente desconectado.");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String exibirMenu(String nomeUsuario){
            return (
                "Selecione a opção desejada: (0) Selecione essa opção para sair a qualquer momento (1) Encontrar pontos de coletas" +
                (CtrUsuarios.isAdministrador(nomeUsuario) ? " (2) Cadastrar pontos de coletas" : "") +
                " Opção: "
            );
        }

        private String opcoesColeta(){
            return "Selecione a opção desejada de ponto de coleta: (0) Selecione essa opção para sair a qualquer momento (1) Metal (2) Vidro (3) Papel (4) Plástico";
        }
    }
}
