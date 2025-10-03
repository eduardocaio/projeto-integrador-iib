import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) throws Exception {
        String host = "localhost"; // ou IP do servidor
        int port = 12345;

        try (Socket socket = new Socket(host, port);
             Scanner scanner = new Scanner(System.in);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Conectado ao servidor!");
            System.out.print("Olá, seja bem-vindo ao EcoColeta!\n"
                    + "Selecione a opção desejada:\n"
                    + "(0) Sair \n"
                    + "(1) Cadastrar \n"
                    + "(2) Entrar \n"
                    + "Opção: ");
            String opcao = scanner.nextLine();
            boolean logado = false;

            do {
                if (opcao.equals("0")) {
                    socket.close();
                    System.exit(0);
                } else if (opcao.equals("1")) {
                    if (realizarCadastro(scanner, in, out)) {
                        logado = true;
                    } else {
                        System.out.println("Houve uma falha ao realizar o cadastro. A conexão está sendo encerrada!");
                        socket.close();
                        System.exit(0);
                    }
                } else if (opcao.equals("2")) {
                    if (realizarLogin(scanner, in, out)) {
                        logado = true;
                    } else {
                        System.out.println("Houve uma falha ao realizar o login. A conexão está sendo encerrada!");
                        socket.close();
                        System.exit(0);
                    }
                } else {
                    System.out.println("Opção inválida. Digite uma opção válida!");
                }
            } while (!logado);

            String mensagem;
            while (true) {
                System.out.println(in.readLine());
                mensagem = scanner.nextLine();
                if (mensagem.equals("0")) {
                    out.println(opcao);
                    break;
                }
                out.println(mensagem);
            }

        } catch (IOException e) {
            System.out.println("Falha na conexão. Erro: " + e.getMessage());
        }
    }

    private static boolean realizarLogin(Scanner scanner, BufferedReader in, PrintWriter out) {
        String nomeUsuario;
        String senha;
        String retornoServidor = "";

        do {
            System.out.print("Nome de usuário: ");
            nomeUsuario = scanner.nextLine();
            System.out.print("Senha: ");
            senha = scanner.nextLine();

            String envioInfoLogin = "login|" + nomeUsuario + "|" + senha;
            out.println(envioInfoLogin);

            try {
                retornoServidor = in.readLine();
            } catch (IOException e) {
                System.out.println("Erro de conexão com o servidor. Erro: " + e.getMessage());
                return false;
            }

            if (!retornoServidor.equals("")) {
                System.out.println(retornoServidor);
                System.out.print("Digite 0 para sair ou 1 para tentar novamente: ");
                if (scanner.nextLine().equals("0")) return false;
                continue;
            }

        } while (!retornoServidor.equals(""));

        return true;
    }

    private static boolean realizarCadastro(Scanner scanner, BufferedReader in, PrintWriter out) {
        String nome;
        String nomeUsuario;
        String senha;
        String opcao;
        String retornoVerificacao;
        String retornoServidor = "";

        do {
            System.out.print("Nome completo: ");
            nome = scanner.nextLine();
            System.out.print("Nome de usuário: ");
            nomeUsuario = scanner.nextLine();
            System.out.print("Senha: ");
            senha = scanner.nextLine();
            System.out.println("Selecione uma opção: \n(0) Cliente \n(1) Administrador");
            opcao = scanner.nextLine();

            retornoVerificacao = verificarDadosCadastro(nome, nomeUsuario, senha, opcao);
            if (!retornoVerificacao.equals("")) {
                System.out.println(retornoVerificacao);
                System.out.print("Digite 0 para sair ou 1 para tentar novamente: ");
                if (scanner.nextLine().equals("0")) return false;
                continue;
            }

            String envioInfoCadastro = "cadastro|" + nome + "|" + nomeUsuario + "|" + senha + "|" + opcao;
            out.println(envioInfoCadastro);

            try {
                retornoServidor = in.readLine();
            } catch (IOException e) {
                System.out.println("Erro de conexão com o servidor. Erro: " + e.getMessage());
                return false;
            }

            if (!retornoServidor.equals("")) {
                System.out.println(retornoServidor);
                System.out.print("Digite 0 para sair ou 1 para tentar novamente: ");
                if (scanner.nextLine().equals("0")) return false;
                continue;
            }

        } while (!retornoVerificacao.equals("") || !retornoServidor.equals(""));

        return true;
    }

    private static String verificarDadosCadastro(String nome, String nomeUsuario, String senha, String opcao) {
        if (nome == null || nome.equals("")) return "Informe seu nome, repita o processo de cadastro!";
        else if (nomeUsuario == null || nomeUsuario.equals("")) return "Informe um nome de usuário, repita o processo de cadastro";
        else if (senha == null || senha.equals("")) return "Informe uma senha, repita o processo de cadastro";
        else if (opcao == null || opcao.equals("")) return "Selecione uma opção, repita o processo de cadastro";
        else if (!opcao.equals("0") && !opcao.equals("1")) return "Selecione uma opção válida, repita o processo de cadastro";
        return "";
    }
}
