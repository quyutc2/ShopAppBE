package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderStatus;
import com.project.shopapp.models.User;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    @Override
    public Order createOrder(OrderDTO orderDTO) throws Exception {
        // tìm xem userId có tòn tại kh
        User user = userRepository
                .findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("cannot find user with id: "+ orderDTO.getUserId()));
        //convert OrderDTO -> Order
        // use thư viện model mapper
        // Tạo 1 luồng = ánh xạ riêng để kiểm soát việc ánh xạ
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        // update các trường của đơn hàng từ OrderDTO
        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUserId(user);
        order.setOrderDate(new Date()); // time now
        order.setStatus(OrderStatus.PENDING);
        // check shipping date >= now
        LocalDate shippingDate = orderDTO.getShippingDate() == null
                ? LocalDate.now() : orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())){
            throw new DataNotFoundException("Date must be at least today !");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        orderRepository.save(order);
        return order;
    }

    @Override
    public Order  getOrder(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

//    @Override
//    public Order updateOrder(
//            Long id,
//            OrderDTO orderDTO)
//            throws Exception
//    {
//        Order exitstingOrder = getOrder(id);
//        if (exitstingOrder != null){
//            User exitsingUser = userRepository
//                    .findById(orderDTO.getUserId())
//                    .orElseThrow(() -> new DataNotFoundException(
//                            "Cannot find user with id: "+ orderDTO.getUserId()));
//            exitstingOrder.setUserId(exitsingUser);
//            exitstingOrder.setFullName(orderDTO.getFullName());
//            exitstingOrder.setEmail(orderDTO.getEmail());
//            exitstingOrder.setPhoneNumber(orderDTO.getPhoneNumber());
//            exitstingOrder.setAddress(orderDTO.getAddress());
//            exitstingOrder.setNote(orderDTO.getNote());
//            exitstingOrder.setOrderDate(exitstingOrder.getOrderDate());
//            exitstingOrder.setStatus(exitstingOrder.getStatus());
//            exitstingOrder.setTotalMoney(orderDTO.getTotalMoney());
//            exitstingOrder.setShippingMethod(orderDTO.getShippingMethod());
//            exitstingOrder.setShippingDate(orderDTO.getShippingDate());
//            exitstingOrder.setTrackingNumber(exitstingOrder.getTrackingNumber());
//            exitstingOrder.setPaymentMethod(orderDTO.getPaymentMethod());
//            exitstingOrder.setActive(exitstingOrder.getActive());
//            return orderRepository.save(exitstingOrder);
//        }
//        return null;
//    }

    @Override
    public Order updateOrder(
           Long id, OrderDTO orderDTO
    ) throws DataNotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Cannot find user with id: "+id));
        User exitsingUser = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(()-> new DataNotFoundException("Cannot find user with id: "+id));
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        modelMapper.map(orderDTO,order);
        order.setUserId(exitsingUser);
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        // no hard-delete => soft-delete
        if (order != null){
            order.setActive(false);
            orderRepository.save(order);
        }
    }

    @Override
    public List<Order> findByUserId(User userId) {
        return orderRepository.findByUserId(userId);
    }

}
