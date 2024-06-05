package com.project.shopapp.controllers;
import com.github.javafaker.Faker;
import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.models.Role;
import com.project.shopapp.responses.ProductListResponse;
import com.project.shopapp.responses.ProductResponse;
import com.project.shopapp.services.IProductService;
import com.project.shopapp.services.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/roles")
public class RoleController {
    private final RoleService roleService;
    @GetMapping
    public ResponseEntity<?> getAllRoles(){
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }
}
