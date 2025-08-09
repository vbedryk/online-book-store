package com.example.bookstore.dto.order;

import com.example.bookstore.model.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderUpdateRequestDto {
    @NotNull
    private Status status;
}
