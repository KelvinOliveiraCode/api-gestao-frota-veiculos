package com.kelvinoliveira.gestaofrota.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Veículo da frota. A placa é a chave de negócio (única, ignorando caixa).
 */
@Entity
@Table(name = "veiculos", uniqueConstraints = @UniqueConstraint(columnNames = "placa"))
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A placa é obrigatória.")
    @Size(max = 10, message = "A placa deve ter no máximo 10 caracteres.")
    @Column(name = "placa", nullable = false, length = 10)
    private String placa;

    @NotBlank(message = "A marca é obrigatória.")
    @Size(max = 60, message = "A marca deve ter no máximo 60 caracteres.")
    @Column(name = "marca", nullable = false, length = 60)
    private String marca;

    @NotBlank(message = "O modelo é obrigatório.")
    @Size(max = 60, message = "O modelo deve ter no máximo 60 caracteres.")
    @Column(name = "modelo", nullable = false, length = 60)
    private String modelo;

    @Column(name = "ano")
    private Integer ano;

    @Size(max = 30, message = "A cor deve ter no máximo 30 caracteres.")
    @Column(name = "cor", length = 30)
    private String cor;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusVeiculo status = StatusVeiculo.ATIVO;

    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    protected Veiculo() {
    }

    public Veiculo(String placa, String marca, String modelo, Integer ano, String cor, StatusVeiculo status) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.cor = cor;
        this.status = status;
    }

    @PrePersist
    void aoCriar() {
        if (this.dataCadastro == null) {
            this.dataCadastro = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public StatusVeiculo getStatus() {
        return status;
    }

    public void setStatus(StatusVeiculo status) {
        this.status = status;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Veiculo)) {
            return false;
        }
        Veiculo veiculo = (Veiculo) o;
        return Objects.equals(id, veiculo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Veiculo{id=" + id + ", placa='" + placa + "', marca='" + marca + "', modelo='" + modelo + "'}";
    }
}
