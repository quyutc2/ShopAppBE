package com.project.shopapp.controllers;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.dtos.OrderDetailDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {
    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(
            @RequestBody @Valid OrderDetailDTO orderDetailDTO,
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
            return ResponseEntity.ok("CreateOrderDetail successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}") // thêm đường dẫn id
    //http://localhost:8088/api/v1/order_detail/{id}
    public ResponseEntity<?> getOrdersDetail(@Valid @PathVariable("id") Long id){
            return ResponseEntity.ok("GetOrderDetail with id" + id);
    }

    @GetMapping("/order/{orderId}") // Lấy ra danh sách orderDetail của 1 order
    //http://localhost:8088/api/v1/order_detail/{id}
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("orderId") Long orderId){
        return ResponseEntity.ok("getOrderDetails with orderId"+ orderId);
    }

    @PutMapping("/{id}")
    //http://localhost:8088/api/v1/
    public ResponseEntity<?> updateOrder(
            @Valid @PathVariable Long id,
            @Valid @RequestBody OrderDetailDTO newOrderDetailData
    ){
        return ResponseEntity.ok("UpdateOrderDetail with id = "+ id + " "
                +"newOrderDetailData" + newOrderDetailData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderDetail(@Valid @PathVariable Long id){
        // xóa mềm => cập nhập trường active = false
        return ResponseEntity.noContent().build();
    }

}
