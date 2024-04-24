/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

//bibliotecas importadas
import java.util.ArrayList;
import model.CadastroUser;
import view.Cadastro;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;

import view.ExibirPessoa;



/**
 *
 * @author rafae
 */
public class Controller {
    
    //array para armazenamento dos usuários cadastrados
    ArrayList <CadastroUser> cadastroU = new ArrayList <>();
    
    //Atributos para uso da view Exibir Usuário
    private ExibirPessoa e;
    //Atributos para uso da view Cadastro
    private Cadastro c;
    //Atributos usados para criação de usuário e busca de usuário
    private String nome,sobrenome,sexo,cpf,idade;
    
    
    
    
    public Controller(Cadastro c){
        this.c = c;
    }
    
    public Controller (ExibirPessoa e){
        this.e = e;
    }
    
    //metodo usado quando o botão cadastrar for acionado
    public void btCadastrar(){
        
        //atributos que foram recebidos na view cadastro
        nome = c.getTextNome().getText();
        sobrenome = c.getTextSobrenome().getText();
        idade = c.getTextIdade().getText();
        cpf = c.getTextCPF().getText();
        
        //Abre o arquivo em modo de leitura
        try (BufferedReader br = new BufferedReader(new FileReader
                                                   ("usuarios.txt"))) {
            String linha;
            //passa pelo arquivo linha por linha
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(",");
                //verifica na posição do cpf na array
                String cpfP = dados[3];
                
                //cpf for igual ao que foi digitado, ele imprime no terminal que
                //o cpf já é existente e abre uma janela informando que ja existe.
                if (cpfP.equals(cpf)) {
                    
                    //abre a janela
                    JOptionPane.showMessageDialog(null, "CPF já existente !", "Erro", JOptionPane.ERROR_MESSAGE);
                    //imprime no terminal
                    System.err.println("CPF já existente!");
                    return;
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }
        
        
        //Exceção para quantidade de numeros digitados no campo do cpf
        try {
            if (cpf.length() != 11) {
                throw new IllegalArgumentException("CPF inválido."
                                         +" Deve conter exatamente 11 dígitos.");
            }
            
        } catch (IllegalArgumentException e) {
            System.err.println("Exceção: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "CPF inválido. Deve conter exatamente 11 dígitos.", "Erro", JOptionPane.ERROR_MESSAGE);

            return;
        }
        
        
        //Exceção para identificar digitos alem de numeros 
        try {
            Long.parseLong(cpf); 
            
        } catch (NumberFormatException e) {
            System.err.println("Exceção: " + e.getMessage());
            JOptionPane.showMessageDialog(null,"CPF invalido, Digite apenas números !","Erro",JOptionPane.ERROR_MESSAGE);
//            
            return;
        }
        
        
        //logica para adicionar o genero do usuário 
        if(c.getRadioFemi().isSelected()){
            sexo = "Feminino";
        } else{
            sexo = "Masculino";
        }
        
        //salva o usuário na array 
        cadastroU.add(new CadastroUser(nome,sobrenome,idade,cpf,sexo));

        JOptionPane.showMessageDialog(null, "Cadastrado com sucesso !", "Sucesso",JOptionPane.INFORMATION_MESSAGE);
        
        //imprime no terminal que o usuario foi criado 
        System.out.println("User Created!");
        
        
        //Logica utulizada para identificar e criar o arquivo para salvar 
        //o usuario no arquivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter
                                         ("usuarios.txt", true))) {
            //Caminha pela a array de usuarios cadastratos 
            for (CadastroUser user : cadastroU) {
                //escreve na linha os atributos do usuário criado e separa 
                //todos por uma ",".
                writer.write(user.getNome() + "," + user.getSobrenome() + "," +
                             user.getIdade() + "," + user.getCpf() + "," + 
                             user.getSexo());
                writer.newLine(); // Adiciona uma quebra de linha
            }
            //retorna a exceção caso contenha algum erro
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
        
        //imprime as informações que foram salvas na array de usuario 
        System.out.println(cadastroU);
        
        //limpa os campos preenchidos na view para poder criar um novo usuário
        c.getTextNome().setText("");
        c.getTextSobrenome().setText("");
        c.getTextIdade().setText("");
        c.getTextCPF().setText("");
        
    }
    
    //metodo para exibir as informações do usuario quando digitado o cpf solicitado
    public void btExibir(){
        //Atributo que recebe o cpf do usuario para pesquisa
        cpf = e.getTextCPFProcurar().getText();
        
        boolean cpfEncontrado = false;
        //Logica para identificar e ler o arquivo para informas as 
        //informações do usuario 
        try (BufferedReader br = new BufferedReader(new FileReader
                                                   ("usuarios.txt"))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] infors = linha.split(",");
                //imprime no terminal os atributos encontrados no arquivo 
                if (infors.length == 5 && infors[3].equals(cpf)) {
                    System.out.println("Nome: " + infors[0]);
                    System.out.println("Sobrenome: " + infors[1]);
                    System.out.println("Idade: " + infors[2]);
                    System.out.println("CPF: " + infors[3]);
                    System.out.println("Gênero: " + infors[4]);
                    
                    //imprime no view as informações encontradas no arquivo, em
                    //cada campo identificado
                    e.getTxtNameUser().setText(infors[0]);                         
                    e.getTxtSobreUser().setText(infors[1]);
                    e.getTxtIdadeUser().setText(infors[2]);
                    e.getTxtCPFUser().setText(infors[3]);
                    e.getTxtGenUser().setText(infors[4]);
                    
                    cpfEncontrado = true;
                    break;
                }
            }
            //exibe o erro encontrado na exceção, caso tenha
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }
     
    }
        
    
}
