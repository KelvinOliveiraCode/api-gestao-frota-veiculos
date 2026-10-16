package com.kelvinoliveira.gestaofrota.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Custo da frota vinculado a um veículo (combustível, seguro, IPVA, pneu, etc.).
 */
@Entity
@Table(name = "custos")
public class Custo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 30)
    private TipoCusto tipo;

    @NotBlank(message = "A descrição é obrigatória.")
    @Size(max = 200, message = "A descrição deve ter no máximo 200 caracteres.")
    @Column(name = "descricao", nullable = false, length = 200)
    private String descricao;

    @NotNull(message = "O valor é obrigatório.")
    @Column(name = "valor", nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    @NotNull
    @Column(name = "data_custo", nullable = false)
    private LocalDate data;

    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    protected Custo() {
    }

    public Custo(Veiculo veiculo, TipoCusto tipo, String descricao, BigDecimal valor, LocalDate data) {
        this.veiculo = veiculo;
        this.tipo = tipo;
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
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

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public TipoCusto getTipo() {
        return tipo;
    }

    public void setTipo(TipoCusto tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
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
        if (!(o instanceof Custo)) {
            return false;
        }
        Custo custo = (Custo) o;
        return Objects.equals(id, custo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Custo{id=" + id + ", veiculoId=" + (veiculo != null ? veiculo.getId() : null) + ", valor=" + valor + "}";
    }
}
