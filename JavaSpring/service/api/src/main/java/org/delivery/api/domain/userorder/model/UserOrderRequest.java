package org.delivery.api.domain.userorder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOrderRequest {
    @NotNull
    private List<Long> storeMenuIdList;
}
