package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.Product;
import com.project.shopapp.repositories.OrderDetailRepository;
import com.project.shopapp.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
        Order order = orderRepository
                .findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException
                        ("cannot find order with id: " + orderDetailDTO.getOrderId()));
        Product product = productRepository
                .findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException
                        ("cannot find product with id: " + orderDetailDTO.getProductId()));
        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .numberOfProducts(orderDetailDTO.getNumberOfProducts())
                .price(orderDetailDTO.getPrice())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .color(orderDetailDTO.getColor())
                .build();
        return orderDetailRepository.save(orderDetail);
    }

//    @Override
//    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
//        Order order = orderRepository
//                .findById(orderDetailDTO.getOrderId())
//                .orElseThrow(() -> new DataNotFoundException
//                        ("cannot find order with id: "+ orderDetailDTO.getOrderId()));
//        Product product = productRepository
//                .findById(orderDetailDTO.getProductId())
//                .orElseThrow(() -> new DataNotFoundException
//                        ("cannot find product with id: "+ orderDetailDTO.getProductId()));
//        modelMapper.typeMap(OrderDetailDTO.class, OrderDetail.class)
//                .addMappings(mapper -> mapper.skip(OrderDetail::setId));
//        OrderDetail orderDetail = new OrderDetail();
//        modelMapper.map(orderDetailDTO,orderDetail);
//        orderDetail.setOrder(order);
//        orderDetail.setProduct(product);
//        return orderDetailRepository.save(orderDetail);
//
//    }

    @Override
    public OrderDetail getOrderDetail(Long id) throws DataNotFoundException {
        return orderDetailRepository.findById(id).orElseThrow(()->
                new DataNotFoundException("Cannot find OrderDetail with id: "+id));
    }

    @Override
    public OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO)
            throws DataNotFoundException {
        // find orderDetails exits
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException
                        ("Cannot find orderDetail with id: "+id));
        Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(()-> new DataNotFoundException
                        ("Cannot find order with id: "+orderDetailDTO.getOrderId()));
        Product existingProduct = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(()-> new DataNotFoundException
                        ("Cannot find product with id: "+orderDetailDTO.getProductId()));
        existingOrderDetail.setPrice(orderDetailDTO.getPrice());
        existingOrderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
        existingOrderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
        existingOrderDetail.setColor(orderDetailDTO.getColor());
        existingOrderDetail.setOrder(existingOrder);
        existingOrderDetail.setProduct(existingProduct);
        return orderDetailRepository.save(existingOrderDetail);
    }

    @Override
    public List<OrderDetail> findByOrderId(Long id) {
        return orderDetailRepository.findByOrderId(id);
    }

    @Override
    public void deleteById(Long id) {
        orderDetailRepository.deleteById(id);
    }
}
