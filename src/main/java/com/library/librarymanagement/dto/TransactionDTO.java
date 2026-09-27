package com.library.librarymanagement.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDTO {

    private Long transactionId;
    private Long bookId;
    private String bookTitle;       // Extra info for response
    private Long memberId;
    private String memberName;      // Extra info for response
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status;
}