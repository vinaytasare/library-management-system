package com.library.librarymanagement.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {

    private Long bookId;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @NotBlank(message = "ISBN is required")
    private String isbn;

    private String category;

    @Min(value = 1, message = "Total copies must be at least 1")
    private Integer totalCopies;

    private Integer availableCopies;
}