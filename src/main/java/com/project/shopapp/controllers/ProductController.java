package com.project.shopapp.controllers;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.dtos.ProductDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {
    // post
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @Valid @ModelAttribute ProductDTO productDTO,
          //  @RequestPart("file") MultipartFile file,
            BindingResult result){
        try {
            if (result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            List<MultipartFile> files = productDTO.getFiles();
            files = files == null ? new ArrayList<MultipartFile>() : files;
            for(MultipartFile file: files){
                if(file.getSize() == 0){
                    continue;
                }
                //Kiểm tra kích thước và định dạng fike ảnh
                if (file.getSize() > 10*1024*1024){
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File is too large! Maximum size is 10MB");
                }
                String contenType = file.getContentType();
                if(contenType == null || !contenType.startsWith("image/")){
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body("File must be an image");
                }
                // Lưu file và cập nhập thumnail trong DTO
                String fileName = storeFile(file);
                // lưu vào dối tợng product tronG DB => làm sau
            }

            return ResponseEntity.ok("product created successfuly");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    private String storeFile(MultipartFile file) throws IOException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        // Thêm UUID vafo trước tên file để tên file là duy nhất
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        // Đường dẫn đến thư mục lưu file
        java.nio.file.Path upLoadDir = Paths.get("uplpads");
        // Kiểm tra và ta thư mục nêu nó k tồn tại
        if (!Files.exists(upLoadDir)){
            Files.createDirectories(upLoadDir);
        }
        // Đường dẫn đày đủ đên file
        java.nio.file.Path destination = Paths.get(upLoadDir.toString(), uniqueFilename);
        // Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    @GetMapping("")
    public ResponseEntity<String> getProducts(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        return ResponseEntity.ok("getProducts here");
    }
    // http://localhost:8088/api/v1/products

    @GetMapping("/{id}")
    public ResponseEntity<String> getProductById(@PathVariable("id") String productId){
        return ResponseEntity.ok("Product with ID"+ productId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable long id){
        return ResponseEntity.ok(String.format("Product with id = %d deleted successfully", id));
    }
}
