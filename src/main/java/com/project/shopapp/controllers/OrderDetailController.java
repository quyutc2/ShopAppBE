package com.project.shopapp.controllers;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.repositories.OrderDetailRepository;
import com.project.shopapp.responses.OrderDetailResponse;
import com.project.shopapp.services.IOrderDetailService;
import com.project.shopapp.services.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {
    private final OrderDetailService orderDetailService;
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
            OrderDetail orderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
            return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetail));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}") // thêm đường dẫn id
    //http://localhost:8088/api/v1/order_detail/{id}
    public ResponseEntity<?> getOrdersDetail(@Valid @PathVariable("id") Long id) throws DataNotFoundException {
        OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
            return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetail));
    }

    @GetMapping("/order/{orderId}") // Lấy ra danh sách orderDetail của 1 order
    //http://localhost:8088/api/v1/order_detail/{id}
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("orderId") Long orderId){
        List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);
        List<OrderDetailResponse> orderDetailResponses = orderDetails
                .stream().map(OrderDetailResponse::fromOrderDetail).toList();
        return ResponseEntity.ok(orderDetailResponses);
    }

    @PutMapping("/{id}")
    //http://localhost:8088/api/v1/
    public ResponseEntity<?> updateOrder(
            @Valid @PathVariable Long id,
            @Valid @RequestBody OrderDetailDTO orderDetailDTO
    ){
        try {
            OrderDetail orderDetail = orderDetailService.updateOrderDetail(id, orderDetailDTO);
         //   return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetail));
            return ResponseEntity.ok(orderDetail);
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(@Valid @PathVariable Long id){
        // xóa mềm => cập nhập trường active = false
        orderDetailService.deleteById(id);
        return ResponseEntity.ok().body("Delete Order detail with id : "+id +" successfully");

    }

}
