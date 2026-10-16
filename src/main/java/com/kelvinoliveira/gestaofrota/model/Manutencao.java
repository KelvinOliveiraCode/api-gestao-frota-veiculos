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
 * Ordem de manutenção de um veículo.
 */
@Entity
@Table(name = "manutencoes")
public class Manutencao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private TipoManutencao tipo;

    @NotBlank(message = "A descrição é obrigatória.")
    @Size(max = 200, message = "A descrição deve ter no máximo 200 caracteres.")
    @Column(name = "descricao", nullable = false, length = 200)
    private String descricao;

    @NotNull
    @Column(name = "data_manutencao", nullable = false)
    private LocalDate data;

    @Column(name = "custo", precision = 12, scale = 2)
    private BigDecimal custo;

    @Column(name = "km")
    private Integer km;

    @Size(max = 100, message = "A oficina deve ter no máximo 100 caracteres.")
    @Column(name = "oficina", length = 100)
    private String oficina;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusManutencao status = StatusManutencao.AGENDADA;

    @Column(name = "data_conclusao")
    private LocalDate dataConclusao;

    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    protected Manutencao() {
    }

    public Manutencao(Veiculo veiculo, TipoManutencao tipo, String descricao, LocalDate data,
                      BigDecimal custo, Integer km, String oficina, StatusManutencao status) {
        this.veiculo = veiculo;
        this.tipo = tipo;
        this.descricao = descricao;
        this.data = data;
        this.custo = custo;
        this.km = km;
        this.oficina = oficina;
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

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public TipoManutencao getTipo() {
        return tipo;
    }

    public void setTipo(TipoManutencao tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public BigDecimal getCusto() {
        return custo;
    }

    public void setCusto(BigDecimal custo) {
        this.custo = custo;
    }

    public Integer getKm() {
        return km;
    }

    public void setKm(Integer km) {
        this.km = km;
    }

    public String getOficina() {
        return oficina;
    }

    public void setOficina(String oficina) {
        this.oficina = oficina;
    }

    public StatusManutencao getStatus() {
        return status;
    }

    public void setStatus(StatusManutencao status) {
        this.status = status;
    }

    public LocalDate getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDate dataConclusao) {
        this.dataConclusao = dataConclusao;
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
        if (!(o instanceof Manutencao)) {
            return false;
        }
        Manutencao that = (Manutencao) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Manutencao{id=" + id + ", veiculoId=" + (veiculo != null ? veiculo.getId() : null) + "}";
    }
}
