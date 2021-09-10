package br.com.zup.orangetalentschallenge.repository;

import br.com.zup.orangetalentschallenge.models.FipeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "fipe", url = "https://parallelum.com.br/fipe/api/v1/carros")
public interface FipeClient {

    @GetMapping(value = "/marcas")
    List<FipeResponse> getBrands();

    @GetMapping("/marcas/{brandId}/modelos")
    FipeResponse getModels(@PathVariable String brandId);

    @GetMapping("/marcas/{brandId}/modelos/{modelId}/anos/{year}")
    FipeResponse getValue(@PathVariable String brandId, @PathVariable String modelId, @PathVariable String year);
}