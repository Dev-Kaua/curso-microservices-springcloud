package io.github.Dev_Kaua.msavaliadorcredito.application;

import feign.FeignException;
import io.github.Dev_Kaua.msavaliadorcredito.application.ex.DadosClienteNotFoundException;
import io.github.Dev_Kaua.msavaliadorcredito.application.ex.ErroComunicacaoMicrosservicesException;
import io.github.Dev_Kaua.msavaliadorcredito.domain.model.CartaoCliente;
import io.github.Dev_Kaua.msavaliadorcredito.domain.model.DadosCliente;
import io.github.Dev_Kaua.msavaliadorcredito.domain.model.SituacaoCliente;
import io.github.Dev_Kaua.msavaliadorcredito.infra.clients.CartoesResourceClient;
import io.github.Dev_Kaua.msavaliadorcredito.infra.clients.ClienteResourceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
