package com.project.shopapp.controllers;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.User;
import com.project.shopapp.services.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final IOrderService orderService;
    @PostMapping("")
    public ResponseEntity<?> createOrder(
            @RequestBody @Valid OrderDTO orderDTO,
            BindingResult result
    ){
        try {
            if (result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Order order = orderService.createOrder(orderDTO);
            return ResponseEntity.ok(order);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("user/{user_id}") // thêm đường dẫn user_id
    //http://localhost:8088/api/v1/orders/user/user_id
    public ResponseEntity<?> getOrders(@Valid @PathVariable("user_id") User userId){
        try {
            List<Order> orders = orderService.findByUserId(userId);
            return ResponseEntity.ok(orders);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}") // thêm đường dẫn user_id
    //http://localhost:8088/api/v1/order/{id}
    public ResponseEntity<?> getOrder(@Valid @PathVariable("id") Long orderId){
        try {
            Order existingOrder = orderService.getOrder(orderId);
            return ResponseEntity.ok(existingOrder);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("/{id}")
    //http://localhost:8088/api/v1/orders/id
    public ResponseEntity<?> updateOrder(
            @Valid @PathVariable Long id,
            @Valid @RequestBody OrderDTO orderDTO
    ){
        try {
            Order updateOrder = orderService.updateOrder(id, orderDTO);
            return ResponseEntity.ok(updateOrder);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@Valid @PathVariable Long id){
        // xóa mềm => cập nhập trường active = false
        try {
             orderService.deleteOrder(id);
            return ResponseEntity.ok(String.format("Order with id = %d deleted successfully", id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
