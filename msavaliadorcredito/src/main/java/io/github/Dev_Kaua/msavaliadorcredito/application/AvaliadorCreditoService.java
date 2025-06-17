package io.github.Dev_Kaua.msavaliadorcredito.application;

import feign.FeignException;
import io.github.Dev_Kaua.msavaliadorcredito.application.ex.DadosClienteNotFoundException;
import io.github.Dev_Kaua.msavaliadorcredito.application.ex.ErroComunicacaoMicrosservicesException;
import io.github.Dev_Kaua.msavaliadorcredito.domain.model.*;
import io.github.Dev_Kaua.msavaliadorcredito.infra.clients.CartoesResourceClient;
import io.github.Dev_Kaua.msavaliadorcredito.infra.clients.ClienteResourceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient clientesClient;

    private final CartoesResourceClient cartoesClient;

    public SituacaoCliente obterSituacaoCliente(String cpf)
            throws DadosClienteNotFoundException, ErroComunicacaoMicrosservicesException {
        //objetivo: obter dados do msclientes e obter cartões do mscartoes
        //para fazer essa comunicação, irei utilizar o OpenFeign
        try {
            ResponseEntity<DadosCliente> dadosClienteResponse = clientesClient.dadosCliente(cpf);
            //P.Erro: Se eu não tiver o cliente que estou tentando encontrar cadastrado vou receber error404
            ResponseEntity<List<CartaoCliente>> cartoesResponse = cartoesClient.getCartoesByCliente(cpf);

            return SituacaoCliente
                    .builder()
                    .cliente(dadosClienteResponse.getBody())
                    .cartoes(cartoesResponse.getBody())
                    .build();
        }catch(FeignException.FeignClientException e){
            int status = e.status();
            if(HttpStatus.NOT_FOUND.value() == status){ //NOT_FOUND é o erro 404
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicrosservicesException(e.getMessage(), status);
        }
    }

    public RetornoAvaliacaoCliente realizarAvaliacao(String cpf, Long renda)
            throws DadosClienteNotFoundException, ErroComunicacaoMicrosservicesException{
        try {
            ResponseEntity<DadosCliente> dadosClienteResponse = clientesClient.dadosCliente(cpf);
            ResponseEntity<List<Cartao>> cartoesResponse = cartoesClient.getCartoesRendaAte(renda);

            List<Cartao> cartoes = cartoesResponse.getBody();
            var listaCartoesAprovados = cartoes.stream().map(cartao -> {
                DadosCliente dadosCliente = dadosClienteResponse.getBody();

                BigDecimal limiteBasico = cartao.getLimiteBasico();
                BigDecimal rendaBD = BigDecimal.valueOf(renda);
                BigDecimal idadeBD = BigDecimal.valueOf(dadosCliente.getIdade());
                var fator = idadeBD.divide(BigDecimal.valueOf(10));
                BigDecimal limiteAprovado = fator.multiply(limiteBasico);

                CartaoAprovado aprovado = new CartaoAprovado();
                aprovado.setCartao(cartao.getNome());
                aprovado.setBandeira(cartao.getBandeira());
                aprovado.setLimiteAprovado(limiteAprovado);

                return aprovado;
            }).collect(Collectors.toList());

            return new RetornoAvaliacaoCliente(listaCartoesAprovados);

        }catch(FeignException.FeignClientException e){
            int status = e.status();
            if(HttpStatus.NOT_FOUND.value() == status){ //NOT_FOUND é o erro 404
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicrosservicesException(e.getMessage(), status);
        }
    }
}
