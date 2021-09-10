package br.com.zup.orangetalentschallenge.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FipeResponse {
    private String nome;
    private String codigo;
    @JsonProperty("Valor")
    private String Valor;
    private List<FipeResponse> modelos;
    private List<FipeResponse> anos;
}