package io.github.Dev_Kaua.msavaliadorcredito.application;

import io.github.Dev_Kaua.msavaliadorcredito.domain.model.DadosCliente;
import io.github.Dev_Kaua.msavaliadorcredito.domain.model.SituacaoCliente;
import io.github.Dev_Kaua.msavaliadorcredito.infra.clients.ClienteResourceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient clientesClient;

    public SituacaoCliente obterSituacaoCliente(String cpf){
        //objetivo: obter dados do msclientes e obter cartões do mscartoes
        //para fazer essa comunicação, irei utilizar o OpenFeign

        ResponseEntity<DadosCliente> dadosClienteResponse = clientesClient.dadosCliente(cpf);

        return SituacaoCliente.builder().cliente(dadosClienteResponse.getBody()).build();
    }
}
