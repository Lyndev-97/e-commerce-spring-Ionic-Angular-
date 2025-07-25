package com.devlyn.domain;

import java.io.Serializable;
import java.util.Objects;

import com.devlyn.domain.enums.EstadoPagamento;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type") 
public abstract class Pagamento implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer Id;
	private Integer estado;
	
	@JsonIgnore
	@OneToOne
	@JoinColumn(name="pedido_id")
	@MapsId
	private Pedido pedido;
	
	
	public Pagamento() {
	}

	public Pagamento(Integer id, EstadoPagamento estado, Pedido pedido) {
		super();
		this.Id = id;
		this.estado = (estado==null) ? null : estado.getCod();
		this.pedido = pedido;
	}

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public EstadoPagamento getEstado() {
		return EstadoPagamento.toEnum(estado);
	}

	public void setEstado(EstadoPagamento estado) {
		this.estado = estado.getCod();
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}


	@Override
	public int hashCode() {
		return Objects.hash(Id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pagamento other = (Pagamento) obj;
		return Objects.equals(Id, other.Id);
	}
	
	
}
