package br.gov.sp.etec.estacionamento.model;

import java.time.LocalDate;

public class Usuario {
    private String nome;
    private String cpf;
    private String email;
    private String senha;
    private String telefone;
    private LocalDate datadenasc;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDate getDatadenasc() {
        return datadenasc;
    }

    public void setDatadenasc(LocalDate datadenasc) {
        this.datadenasc = datadenasc;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                ", datadenasc=" + datadenasc +
                '}';
    }
}
