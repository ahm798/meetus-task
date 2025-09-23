package ahm.dev.tasktrix.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.domain.Page;

import java.util.List;

// @Schema(description = "Paginated response")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PageResponse<T>(
    // @Schema(description = "Content items")
    List<T> content,

    // @Schema(description = "Pagination metadata")
    PageMetadata page
) {
    public record PageMetadata(
        int number,
        int size,
        int totalPages,
        long totalElements,
        boolean first,
        boolean last,
        boolean hasNext,
        boolean hasPrevious
    ) {}

    public static <T> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(
            page.getContent(),
            new PageMetadata(
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.isFirst(),
                page.isLast(),
                page.hasNext(),
                page.hasPrevious()
            )
        );
    }
}