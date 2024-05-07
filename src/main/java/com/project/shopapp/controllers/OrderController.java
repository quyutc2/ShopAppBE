package com.project.shopapp.controllers;

import com.project.shopapp.dtos.OrderDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
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
            return ResponseEntity.ok("CreateOrder successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{user_id}") // thêm đường dẫn user_id
    //http://localhost:8088/api/v1/orders/user_id
    public ResponseEntity<?> getOrders(@Valid @PathVariable("user_id") Long userId){
        try {
            return ResponseEntity.ok("Lấy ra danh sách order từ userId");
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
        return ResponseEntity.ok("Update infor Order");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@Valid @PathVariable Long id){
        // xóa mềm => cập nhập trường active = false
        return ResponseEntity.ok("order deleted successfully");
    }
}
