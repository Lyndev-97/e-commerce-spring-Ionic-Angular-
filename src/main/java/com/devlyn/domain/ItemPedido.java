package com.devlyn.domain;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

@Entity
public class ItemPedido implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@EmbeddedId
	private ItemPedidoPK id = new ItemPedidoPK();
	
	private Double desconto;
	private Integer quatidade;
	private Double preco;

	public ItemPedido() {
	}

	public ItemPedido(Pedido pedido, Produto produto, Double desconto, Integer quatidade, Double preco) {
		super();
		id.setPedido(pedido);
		id.setProduto(produto);
		this.desconto = desconto;
		this.quatidade = quatidade;
		this.preco = preco;
	}
	
	@JsonIgnore
	public Pedido getPedido() {
		return id.getPedido();
	}
	@JsonIgnore
	public Produto getProduto() {
		return id.getProduto();
	}

	public ItemPedidoPK getId() {
		return id;
	}

	public void setId(ItemPedidoPK id) {
		this.id = id;
	}

	public Double getDesconto() {
		return desconto;
	}

	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}

	public Integer getQuatidade() {
		return quatidade;
	}

	public void setQuatidade(Integer quatidade) {
		this.quatidade = quatidade;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemPedido other = (ItemPedido) obj;
		return Objects.equals(id, other.id);
	}
	
	
}
