package com.devlyn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devlyn.domain.ItemPedido;
import com.devlyn.domain.ItemPedidoPK;

@Repository
public interface ItemPedidoRepository extends JpaRepository <ItemPedido, ItemPedidoPK> {

}
