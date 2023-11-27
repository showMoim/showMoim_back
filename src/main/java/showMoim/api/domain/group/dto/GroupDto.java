package showMoim.api.domain.group.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class GroupDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupCreateForm {
        private String title;

        private String description;

        private Long groupCategoryId;
    }
}
