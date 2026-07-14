package com.lalit.order.service;

import com.lalit.order.client.ProductClient;
import com.lalit.order.exceptions.CartEmptyException;
import com.lalit.order.exceptions.OrderIdNotFoundException;
import lombok.RequiredArgsConstructor;
import com.lalit.order.dto.OrderItemDTO;
import com.lalit.order.dto.OrderResponse;
import com.lalit.order.entity.CartItem;
import com.lalit.order.entity.Order;
import com.lalit.order.entity.OrderItem;
import com.lalit.order.enums.OrderStatus;
import com.lalit.order.repository.OrderRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private  final CartService cartService;
    private final OrderRepo orderRepo;

    private final ProductClient productClient;

    public OrderResponse createOrder(Long userId) {
        //validating for cart Items
        List<CartItem> cartItems = cartService.fetchCartItems(userId);
        if (cartItems.isEmpty()){
            throw new CartEmptyException(
                    "Cannot create order, cart is empty for userId : " + userId
            );
        }
        //validate for user
        BigDecimal totalPrice = cartItems.stream()
                .map(item ->
                        item.getPrice().multiply(
                                BigDecimal.valueOf(item.getQuantity())
                        ))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //reducing inventory
        for(CartItem item : cartItems)
            productClient.reduceStock(
                    item.getProductId(),
                    item.getQuantity()
            );
        //create order
        Order order= new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);

        List<OrderItem> orderItems = cartItems.stream()
                .map(item -> {
                    OrderItem orderItem = new OrderItem();

                    orderItem.setQuantity(item.getQuantity());
                    orderItem.setPrice(item.getPrice());
                    orderItem.setProductId(item.getProductId());

                    orderItem.setOrder(order);

                    return orderItem;
                })
                .toList();

        order.setItems(orderItems);
        Order savedOrder= orderRepo.save(order);

        //clear the cart
        cartService.clearCart(userId);

        return mapToOrderResponse(savedOrder);

    }

    private OrderResponse mapToOrderResponse(Order savedOrder) {

        return new OrderResponse(
                        savedOrder.getId(),
                savedOrder.getTotalAmount(),
                savedOrder.getStatus(),
                        savedOrder.getItems().stream()
                                .map(orderItem -> new OrderItemDTO(
                                        orderItem.getId(),
                                        orderItem.getProductId(),
                                        orderItem.getQuantity(),
                                        orderItem.getPrice(),
                                        orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity()))

                                ))
                                .toList(),
                savedOrder.getCreatedAt()
                );
    }

    public OrderResponse updateOrderStatus(Long orderId, OrderStatus status) {
         java.util.Optional<Order> optOrderResponse = orderRepo.findById(orderId);

         if(optOrderResponse.isPresent()){
             Order order = optOrderResponse.get();
             order.setStatus(status);
             Order updatedOrder = orderRepo.save(order);
             return mapToOrderResponse(updatedOrder);
         }
         else{
             throw  new OrderIdNotFoundException("No order found with this id:" + orderId);
         }
    }

    public List<OrderResponse> orderHistory(Long userId) {
        return orderRepo.findByUserId(userId)
                .stream()
                .map(this::mapToOrderResponse)
                .toList();
    }
}
