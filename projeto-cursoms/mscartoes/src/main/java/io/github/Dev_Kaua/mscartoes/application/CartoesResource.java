package io.github.Dev_Kaua.mscartoes.application;

import io.github.Dev_Kaua.mscartoes.domain.Cartao;
import io.github.Dev_Kaua.mscartoes.domain.ClienteCartao;
import io.github.Dev_Kaua.mscartoes.representation.CartaoSaveRequest;
import io.github.Dev_Kaua.mscartoes.representation.CartoesPorClienteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("cartoes")
@RequiredArgsConstructor
public class CartoesResource {

    private final CartaoService cartaoService;
    private final ClienteCartaoService clienteCartaoService;

    @GetMapping
    public String status(){
        return "Entrou no microsserviço de cartões";
    }

    @PostMapping
    public ResponseEntity cadastra(@RequestBody CartaoSaveRequest request ){
        Cartao cartao = request.toModel();
        cartaoService.save(cartao);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    // Se a requisição tiver um parametro de renda ele vai cair aqui (EX: /cartoes?renda=5000)
    @GetMapping(params = "renda")
    public ResponseEntity<List<Cartao>> getCartoesRendaAte(@RequestParam("renda") Long renda){
        List<Cartao> list = cartaoService.getCartoesRendaMenorIgual(renda);
        return ResponseEntity.ok(list);
    }
    //se tiver um parametro cpf, vai cair aqui.
    @GetMapping(params = "cpf")
    public ResponseEntity<List<CartoesPorClienteResponse>> getCartoesByCliente(@RequestParam("cpf") String cpf){
        List<ClienteCartao> lista = clienteCartaoService.listCartoesByCpf(cpf);
        List<CartoesPorClienteResponse> resultList = lista.stream()
                .map(CartoesPorClienteResponse::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultList);
    }
}
