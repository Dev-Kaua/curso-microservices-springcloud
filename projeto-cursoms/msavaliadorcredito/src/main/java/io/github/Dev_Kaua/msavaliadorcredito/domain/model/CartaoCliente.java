package io.github.Dev_Kaua.msavaliadorcredito.domain.model;

import lombok.Data;

@Data
public class CartaoCliente {
    private String nome;
    private String bandeira;
    private String limiteLiberado;
}
